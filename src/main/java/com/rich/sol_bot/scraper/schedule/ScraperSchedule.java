package com.rich.sol_bot.scraper.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.scraper.ScraperService;
import com.rich.sol_bot.scraper.mapper.UserScraperTask;
import com.rich.sol_bot.scraper.mapper.UserScraperTaskRepository;
import com.rich.sol_bot.sol.Base58;
import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.twitter.TwitterSearchService;
import com.rich.sol_bot.twitter.mapper.*;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScraperSchedule {
    private static final Logger log = LoggerFactory.getLogger(ScraperSchedule.class);
    @Autowired
    private UserScraperTaskRepository userScraperTaskRepository;
    @Autowired
    private TwitterUserRepository twitterUserRepository;
    @Autowired
    private TwitterSearchService twitterSearchService;
    @Autowired
    private TwitterContentRepository twitterContentRepository;
    @Autowired
    private ScraperService scraperService;
    @Autowired
    private SystemConfigRepository systemConfigRepository;

    public String generateLockKey(String type) {
        return redisKeyGenerateTool.generateName("ScraperSchedule", type);
    };

    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 3 * 60 * 1000)
    public void startScraper() {
        String enable = systemConfigRepository.value(SystemConfigConstant.SCRAPER_STATUS);
        log.info("scraper status: {}", enable);
        if(!"enable".equals(enable)) return;
        if(lockKey("startScraper")) {
            List<UserScraperTask> task = userScraperTaskRepository.list(new LambdaQueryWrapper<UserScraperTask>().eq(UserScraperTask::getDeleted, 0));
            List<Long> twitterUids = task.stream().map(UserScraperTask::getTwitterUserId).toList();
            if(!twitterUids.isEmpty()) {
                List<TwitterUser> twitterUsers = twitterUserRepository.list(new LambdaQueryWrapper<TwitterUser>().in(TwitterUser::getId, twitterUids));
                List<String> usernames = twitterUsers.stream().map(TwitterUser::getUsername).toList();
                if(!usernames.isEmpty()) {
                    TwitterScraperTask scraperTask = twitterSearchService.submitTask(usernames);
                    if(scraperTask != null) {
                        twitterSearchService.checkResult2(scraperTask);
                    }
                }
            }
        }
        unlock("startScraper");
    }

    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 10 * 1000)
    public void contentConfirm() {
        if(lockKey("contentConfirm")) {
            List<TwitterContent> content = twitterContentRepository.list(new LambdaQueryWrapper<TwitterContent>().eq(TwitterContent::getConfirmed, 0));
            for(TwitterContent t: content) {
                List<String> addresses = pickOutAddress(t.getFullText());
                if(!addresses.isEmpty()) {
                    List<UserScraperTask> tasks = userScraperTaskRepository.listByTwitterUserId(t.getTwitterUserId());
                    if(tasks.isEmpty()) continue;
                    for(String address: addresses) {
                        scraperService.sendTaskAndAddress(address, tasks);
                    }
                }
                twitterContentRepository.confirm(t.getId());
            }
        }
        unlock("contentConfirm");
    }


    public boolean lockKey(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        return lock.setIfAbsent(true, Duration.ofSeconds(60 * 3));
    }

    public void unlock(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        lock.delete();
    }

    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private RedissonClient redissonClient;


    public static List<String> pickOutAddress(String input) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{30,}");
        Matcher matcher = pattern.matcher(input);
        List<String> longWords = new ArrayList<>();
        while (matcher.find()) {
            String addr = matcher.group();
            if(isAddressFormat(addr)) {
                longWords.add(addr);
            }
        }
        return longWords;
    }

    public static Boolean isAddressFormat(String input) {
        byte[] res = Base58.decode(input);
        return res != null && res.length == 32;
    }

//    public static void main(String[] args) {
//        System.out.println(pickOutWords("NjordRPSzFs8XQUKMjGrhPcmGo9yfC9HP3VHmh8xZpZ   "));
//    }
}
