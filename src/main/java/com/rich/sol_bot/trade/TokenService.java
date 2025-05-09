package com.rich.sol_bot.trade;

import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.pump.PumpInfoService;
import com.rich.sol_bot.pump.mapper.PumpPoolInfo;
import com.rich.sol_bot.sol.entity.MintUmi;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    /**
     * 是否可以直接交易
     * @param address
     * @return
     */
    public Boolean canDeal(String address) {
        TokenBaseInfo tokenBaseInfo = tokenInfoService.geneTokenBaseInfo(address);
        if(tokenBaseInfo.getPoolStartTime() != null) {
            return tokenBaseInfo.getPoolStartTime() < TimestampUtil.now().getTime() / 1000;
        }
        else {
            PumpPoolInfo pumpPoolInfo = pumpInfoService.getPumpInfo(address);
            return pumpPoolInfo != null;
        }
    }

    public Boolean isToken(String address) {
        MintUmi mintUmi = solQueryService.mintUmi(address);
        return mintUmi.getMint() != null;
    }


    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private PumpInfoService pumpInfoService;
    @Resource
    private SolQueryService solQueryService;
}
