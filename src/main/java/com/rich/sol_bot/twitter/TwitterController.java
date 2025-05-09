package com.rich.sol_bot.twitter;

import com.rich.sol_bot.limit_order.param.LimitOrderPxParam;
import com.rich.sol_bot.twitter.mapper.TwitterContent;
import com.rich.sol_bot.twitter.mapper.TwitterScraperTask;
import com.rich.sol_bot.twitter.mapper.TwitterScraperTaskRepository;
import com.rich.sol_bot.twitter.mapper.TwitterUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "twitter")
@RestController
@RequestMapping("/api/twitter")
@Slf4j
public class TwitterController {

    @Autowired
    private TwitterScraperTaskRepository twitterScraperTaskRepository;

    @Operation(summary = "查看用户")
    @GetMapping("/username")
    public TwitterUser username(String username) {
        return twitterSearchService.generateTwitterUser(username);
    }

    @Operation(summary = "提交任务")
    @PostMapping("/submit")
    public TwitterScraperTask submit(@RequestBody List<String> usernames) {
        return twitterSearchService.submitTask(usernames);
    }

    @Operation(summary = "提交任务")
    @GetMapping("/check/result")
    public List<TwitterContent> checkResult(Long taskId) {
        TwitterScraperTask task = twitterScraperTaskRepository.getById(taskId);
        if(task == null) return new ArrayList<>();
        return twitterSearchService.checkResult2(task);
    }

    @Resource
    private TwitterSearchService twitterSearchService;
}
