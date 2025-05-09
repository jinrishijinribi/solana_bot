package com.rich.sol_bot.sol;


import com.rich.sol_bot.sol.entity.*;
import com.rich.sol_bot.sol.pump.PumpInfo;
import com.rich.sol_bot.sol.pump.PumpOperator;
import com.rich.sol_bot.trade.service.TokenPxService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/sol/test")
public class SolOperatorTestController {

    @Operation(summary = "pump info")
    @GetMapping("/pump/info")
    public PumpInfo pumpInfo(String mint) {
        try {
            return pumpOperator.getPumpInfo(mint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Operation(summary = "pump 计算")
    @PostMapping("/pump/cal")
    public String pumpCal(@RequestBody PumpCal cal) {
        try {
            return pumpOperator.calPump(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Operation(summary = "pump 价格")
    @GetMapping("/pump/price")
    public BigDecimal pumpPrice(String mint, String bondingCurve) {
        try {
            return tokenPxService.getPumpPercent(mint, bondingCurve);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Operation(summary = "发送交易")
    @PostMapping("/sign/send")
    public SignSendResult signSend(@RequestBody SignSend signSend) {
        try {
            return solOperator.signSend(signSend.getSecrets(), signSend.getTransaction(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Operation(summary = "持仓账户top20")
    @GetMapping("/largest/account")
    public LargestAccounts largestAccounts(String mint) {
        try {
            return solOperator.largestAccounts(mint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Resource
    private SolOperator solOperator;
    @Resource
    private PumpOperator pumpOperator;
    @Resource
    private TokenPxService tokenPxService;
}
