package com.rich.sol_bot.twitter;

import com.google.gson.Gson;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.json.JacksonOperator;
import com.rich.sol_bot.system.tool.MapTool;
import com.rich.sol_bot.twitter.response.SearchUserResponse;
import com.rich.sol_bot.twitter.response.TaskResultResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TwitterOperator {

    @Autowired
    private JacksonOperator jacksonOperator;

    public SearchUserResponse searchUser(String username) {
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/twitter/user/query",
                    new Gson().toJson(MapTool.Map().put("username", username))).getContent(), SearchUserResponse.class);
        }catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

    public String submitTask(List<String> usernames) {
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/twitter/task/submit",
                    new Gson().toJson(MapTool.Map().put("usernames", usernames))).getContent(), String.class);
        }catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

    public List<TaskResultResponse> queryTask(String taskId) {
        try {
            ArrayList response = new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/twitter/task/query",
                    new Gson().toJson(MapTool.Map().put("datasetId", taskId))).getContent(), ArrayList.class);
            List<TaskResultResponse> result = new ArrayList<>();
            if(response.isEmpty()) return result;
            response.forEach(o -> {
                result.add(new Gson().fromJson(new Gson().toJson(0), TaskResultResponse.class));
            });
            return result;
        }catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }


    @Value("${sol.pxurl}")
    private String url;
    @Resource
    private HttpClientTool httpClientTool;
}
