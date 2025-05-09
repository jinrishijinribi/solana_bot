package com.rich.sol_bot.sol;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.entity.DexscreenerTokenPair;
import com.rich.sol_bot.sol.entity.TokenOverview;
import com.rich.sol_bot.system.http.HttpClientTool;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DexscreenerService {

    public DexscreenerTokenPair getDexPairInfo(String mint) throws Exception {
        Map<String, String> header = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        return new Gson().fromJson(httpClientTool.doGetWithHeaderAndQuery(
                "https://api.dexscreener.io/latest/dex/tokens/" + mint, header, query
        ).getContent(), DexscreenerTokenPair.class);
    }




    @Resource
    private HttpClientTool httpClientTool;
}
