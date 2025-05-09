package com.rich.sol_bot.sol.pump;

import com.google.gson.Gson;
import com.rich.sol_bot.sol.entity.PumpCal;
import com.rich.sol_bot.sol.entity.PumpCalResult;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.tool.MapTool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;

@Service
@Slf4j
public class PumpOperator {


    public String calPump(PumpCal cal) throws Exception {
        return new Gson().fromJson(httpClientTool.doPostWithBody(url + "/api/pump/calc",
                new Gson().toJson(MapTool.Map().put("priorityFee", cal.getPriorityFee()).put("owner", cal.getOwner())
                        .put("mint", cal.getMint()).put("hold", cal.getHold())
                        .put("amount", cal.getAmount()).put("sol", cal.getSol())
                        .put("feeAccount", cal.getFeeAccount()).put("fee", cal.getFee())
                        .put("buy", cal.getBuy())
                )
        ).getContent(), PumpCalResult.class).getTransaction();
    }


    public PumpInfo getPumpInfo(String mint) {
        try {
            return new Gson().fromJson(httpClientTool.doGet("https://frontend-api.pump.fun/coins/" + mint)
                    .getContent(), PumpInfo.class);
        }catch (Exception e) {
            log.error("getPumpInfo", e);
        }
        return null;
    }


    @Resource
    private HttpClientTool httpClientTool;
    @Value("${sol.url}")
    private String url;
}
