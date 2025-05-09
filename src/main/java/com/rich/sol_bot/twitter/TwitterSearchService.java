package com.rich.sol_bot.twitter;

import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.twitter.request.QueryTaskRequest;
import com.rich.sol_bot.twitter.request.SearchUserRequest;
import com.rich.sol_bot.twitter.request.SubmitTaskRequest;
import com.rich.sol_bot.twitter.response.SearchUserResponse;
import com.rich.sol_bot.twitter.response.TaskResultResponse;
import com.rich.sol_bot.twitter.mapper.*;
import com.rich.sol_bot.twitter.response.TaskResultResponse2;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwitterSearchService {

    public TwitterUser generateTwitterUser(String username) {
        if(StringUtils.isBlank(username)) return null;
        TwitterUser twitterUser = twitterUserRepository.findByUsername(username);
        if(twitterUser == null) {
            SearchUserResponse result = twitterFeignClient.searchUser(SearchUserRequest.builder().username(username).build());
            if(result != null && result.getId() != null) {
                twitterUser = TwitterUser.builder()
                        .id(IdUtil.nextId()).username(username).name(result.getName()).twitterUid(result.getId())
                        .watched(0).updatedAt(TimestampUtil.now())
                        .build();
                twitterUserRepository.save(twitterUser);
            }
        }
        return twitterUser;
    }

    public TwitterScraperTask submitTask(List<String> usernames) {
        String taskId = twitterFeignClient.submitTask(SubmitTaskRequest.builder().usernames(usernames).count(5L).build());
        if(taskId != null) {
            TwitterScraperTask task = TwitterScraperTask.builder()
                    .id(IdUtil.nextId()).taskId(taskId).createdAt(TimestampUtil.now())
                    .finishedAt(TimestampUtil.now())
                    .build();
            twitterScraperTaskRepository.save(task);
            return task;
        }
        return null;
    }

    public List<TwitterContent> checkResult(TwitterScraperTask task) {
        List<TaskResultResponse> taskResults = twitterFeignClient.queryTask(QueryTaskRequest.builder().datasetId(task.getTaskId()).build());
        if(taskResults.isEmpty()) return new ArrayList<>();
        List<TwitterContent> newContents = new ArrayList<>();
        for(TaskResultResponse item: taskResults) {
            if(!twitterContentRepository.existByPostId(item.getId())){
                TwitterUser twitterUser = twitterUserRepository.findByUsername(item.getAuthor().getUserName());
                if(twitterUser == null) continue;
                TwitterContent content = TwitterContent.builder()
                        .id(IdUtil.nextId()).postId(item.getId()).twitterUserId(twitterUser.getId()).twitterUsername(twitterUser.getUsername())
                        .fullText(item.getFullText()).createdAt(TimestampUtil.convertFormat(item.getCreatedAt()))
                        .confirmed(0)
                        .build();
                newContents.add(content);
            }
        }
        if(!newContents.isEmpty()) {
            twitterContentRepository.saveBatch(newContents);
        }
        return newContents;
    }

    public List<TwitterContent> checkResult2(TwitterScraperTask task) {
        List<TaskResultResponse2> taskResults = twitterFeignClient.queryTask2(QueryTaskRequest.builder().datasetId(task.getTaskId()).build());
        if(taskResults.isEmpty()) return new ArrayList<>();
        List<TwitterContent> newContents = new ArrayList<>();
        for(TaskResultResponse2 item: taskResults) {
            if(!twitterContentRepository.existByPostId(item.getConversation_id_str())){
                TwitterUser twitterUser = twitterUserRepository.findByTwitterUid(item.getUser_id_str());
                if(twitterUser == null) continue;
                TwitterContent content = TwitterContent.builder()
                        .id(IdUtil.nextId()).postId(item.getConversation_id_str()).twitterUserId(twitterUser.getId()).twitterUsername(twitterUser.getUsername())
                        .fullText(item.getFull_text()).createdAt(TimestampUtil.convertFormat(item.getCreated_at()))
                        .confirmed(0)
                        .build();
                newContents.add(content);
            }
        }
        if(!newContents.isEmpty()) {
            twitterContentRepository.saveBatch(newContents);
        }
        return newContents;
    }

//    public static void main(String[] args) {
//        System.out.println(TimestampUtil.convertFormat("Wed Nov 13 05:15:07 +0000 2024"));
//    }



    @Resource
    private TwitterFeignClient twitterFeignClient;
    @Resource
    private TwitterUserRepository twitterUserRepository;
    @Resource
    private TwitterScraperTaskRepository twitterScraperTaskRepository;
    @Resource
    private TwitterContentRepository twitterContentRepository;
}
