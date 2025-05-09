package com.rich.sol_bot.ex;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.entity.RayComputeAmountOut;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.tool.MapTool;
import jakarta.annotation.Resource;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeService {
    //https://api.anyview.io/api-mix/instruments/latest?exchange=BINANCE&symbol=SOL-USDT-SPOT
    public BigDecimal getPx() {
        try {
            ResultData result = new Gson().fromJson(httpClientTool.doGet("https://api.anyview.io/api-mix/instruments/latest?exchange=BINANCE&symbol=SOL-USDT-SPOT").getContent(), ResultData.class);
            return result.getData();
        }catch (Exception e) {
            return null;
        }
    }


    @Data
    @Builder
    public static class ResultData {
        private String code;
        private BigDecimal data;
    }


    @Resource
    private HttpClientTool httpClientTool;
}
