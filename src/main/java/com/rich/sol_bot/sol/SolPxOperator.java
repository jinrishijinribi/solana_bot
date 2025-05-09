package com.rich.sol_bot.sol;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.entity.RaydiumMint;
import com.rich.sol_bot.sol.entity.SolPxSubscribeResult;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.tool.MapTool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@Slf4j
public class SolPxOperator {

    /**
     * 开始订阅
     * @param ammKey
     * @return
     * @throws Exception
     */
    public SolPxSubscribeResult subscribe(String ammKey) {
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/raydium/subscribe",
                    new Gson().toJson(MapTool.Map().put("ammKey", ammKey))).getContent(), SolPxSubscribeResult.class);
        }catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

    public Boolean unsubscribe(Integer id, String address) {
        try {
            httpClientTool.doPostWithBody(url + "/api/raydium/unsubscribe",
                    new Gson().toJson(MapTool.Map()
                            .put("ids", Collections.singletonList(id)).put("address", address)));
            return true;
        }catch (Exception e) {
            log.error("error", e);
        }
        return false;
    }

    public SolPxSubscribeResult.SubscribeItem reSubscribe(String ammkey, String mint, String address) {
        try {
            return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/raydium/resubscribe",
                    new Gson().toJson(MapTool.Map()
                            .put("ammkey", ammkey).put("mint", mint).put("address", address))
            ).getContent(), SolPxSubscribeResult.SubscribeItem.class);
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
