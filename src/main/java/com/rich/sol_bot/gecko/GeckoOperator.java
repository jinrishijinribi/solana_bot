package com.rich.sol_bot.gecko;

import com.google.gson.Gson;
import com.rich.sol_bot.gecko.gecko_entity.*;
import com.rich.sol_bot.system.http.HttpClientTool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GeckoOperator {

//    @Value("${ton.url}")
    private static final String url = "https://pro-api.coingecko.com/api/v3/onchain";
    private static final String apiKey = "CG-C8wwhyjFzDzK63L6aUKvXpK7";
    /**
     * 获取基础代币信息
     */
    public GeckoTokenBaseInfo getBaseInfo(String address) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("x-cg-pro-api-key", apiKey);
            GeckoResult result = new Gson().fromJson(
                    httpClientTool.doGetWithHeaderAndQuery(url + "/networks/solana/tokens/" + address,
                            headers, new HashMap<>()).getContent(), GeckoResult.class);
            return new Gson().fromJson(new Gson().toJson(result.getData()), GeckoTokenBaseInfo.class);
        }catch (Exception e) {
            log.error("getBaseInfo error", e);
        }
        return null;
    }

    /**
     * 获取代币池子信息
     */
    public GeckoPoolBaseInfo getPoolInfo(String address) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("x-cg-pro-api-key", apiKey);
            GeckoResult result = new Gson().fromJson(
                    httpClientTool.doGetWithHeaderAndQuery(url + "/networks/solana/pools/" + address,
                            headers, new HashMap<>()
                    ).getContent(), GeckoResult.class);
            return new Gson().fromJson(new Gson().toJson(result.getData()), GeckoPoolBaseInfo.class);
        }catch (Exception e) {
            log.error("getPoolInfo error", e);
        }
        return null;
    }

    /**
     * 获取代币k线数据
     */
    public GeckoOHLCVData getPHLCVData(GeckoOHLCVParam param) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("x-cg-pro-api-key", apiKey);
            Map<String, String> query = new HashMap<>();
            query.put("aggregate", param.getAggregate());
            query.put("before_timestamp", param.getBeforeTimestamp().toString());
            query.put("limit", param.getLimit().toString());

            GeckoResult result = new Gson().fromJson(
                    httpClientTool.doGetWithHeaderAndQuery(url + "/networks/solana/pools/"+param.getPoolAddress()+"/ohlcv/" + param.getTimeframe(),
                            headers, query
                    ).getContent(), GeckoResult.class);
            return new Gson().fromJson(new Gson().toJson(result.getData()), GeckoOHLCVData.class);
        }catch (Exception e) {
            log.error("getPoolInfo error", e);
        }
        return null;
    }


    @Resource
    private HttpClientTool httpClientTool;
}
