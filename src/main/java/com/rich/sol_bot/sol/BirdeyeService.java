package com.rich.sol_bot.sol;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.entity.TokenOverview;
import com.rich.sol_bot.sol.entity.TokenSecurity;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.trade.dto.OHLCVTDTO;
import com.rich.sol_bot.trade.params.OHLCVTParam;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangqiyun
 * @since 2024/3/17 15:13
 */

@Service
public class BirdeyeService {
//    /**
//     * 在birdeye获取代币信息
//     *
//     * @param mint 代币地址
//     */
//    public TokenOverview tokenOverview(String mint) throws Exception {
//        Map<String, String> header = new HashMap<>();
//        header.put("X-API-KEY", birdeye_secret);
//        Map<String, String> query = new HashMap<>();
//        query.put("address", mint);
//        return new Gson().fromJson(httpClientTool.doGetWithHeaderAndQuery("https://public-api.birdeye.so/defi/token_overview", header, query).getContent(), TokenOverview.class);
//    }
//
//
//    public TokenSecurity tokenSecurity(String mint) throws Exception {
//        Map<String, String> header = new HashMap<>();
//        header.put("X-API-KEY", birdeye_secret);
//        Map<String, String> query = new HashMap<>();
//        query.put("address", mint);
//        return new Gson().fromJson(httpClientTool.doGetWithHeaderAndQuery("https://public-api.birdeye.so/defi/token_security", header, query).getContent(), TokenSecurity.class);
//    }

//    public OHLCVTDTO getOHLCVInfo(OHLCVTParam param) {
//        try {
//            Map<String, String> header = new HashMap<>();
//            header.put("X-API-KEY", birdeye_secret);
//            Map<String, String> query = new HashMap<>();
//            query.put("address", param.getAddress());
//            query.put("type", param.getType().getValue());
//            query.put("time_from", param.getTimeFrom().toString());
//            query.put("time_to", param.getTimeTo().toString());
//            return new Gson().fromJson(httpClientTool.doGetWithHeaderAndQuery(
//                    "https://public-api.birdeye.so/defi/ohlcv", header, query
//            ).getContent(), OHLCVTDTO.class);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return OHLCVTDTO.builder().data(OHLCVTDTO.DataItem.builder().items(new ArrayList<>()).build()).build();
//    }

    @Resource
    private HttpClientTool httpClientTool;
    @Value("${sol.birdeye}")
    private String birdeye_secret;
}
