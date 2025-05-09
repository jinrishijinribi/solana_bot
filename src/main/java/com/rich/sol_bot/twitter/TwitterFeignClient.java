package com.rich.sol_bot.twitter;

import com.rich.sol_bot.twitter.request.QueryTaskRequest;
import com.rich.sol_bot.twitter.request.SearchUserRequest;
import com.rich.sol_bot.twitter.request.SubmitTaskRequest;
import com.rich.sol_bot.twitter.response.SearchUserResponse;
import com.rich.sol_bot.twitter.response.TaskResultResponse;
import com.rich.sol_bot.twitter.response.TaskResultResponse2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(name = "TwitterClientInterceptor", url = "${sol.pxurl}", configuration = {
        TwitterClientInterceptor.class
})
public interface TwitterFeignClient {

    // 发送邮件验证码
//    @PostMapping("/api/email/otp")
//    NxCloudResponse sendEmail(NxCloudSendRequest param);
    @PostMapping("/api/twitter/user/query")
    SearchUserResponse searchUser(SearchUserRequest request);

    @PostMapping("/api/twitter/task/submit")
    String submitTask(SubmitTaskRequest request);

    @PostMapping("/api/twitter/task/query")
    List<TaskResultResponse> queryTask(QueryTaskRequest request);

    @PostMapping("/api/twitter/task/query2")
    List<TaskResultResponse2> queryTask2(QueryTaskRequest request);

}
