package com.rich.sol_bot.chain;

import com.rich.sol_bot.chain.dto.ChainBalance;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.SolBalance;
import com.rich.sol_bot.sol.entity.TokenAccount;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SolChainOperator implements ChainOperator{

    @Override
    public ChainBalance mainBalance(String userAddress) {
        SolBalance solBalance = solQueryService.solBalance(userAddress);
        if(solBalance == null) return ChainBalance.builder().amount(BigDecimal.ZERO).build();
        TokenBaseInfo tokenBaseInfo = tokenInfoService.getMain();
        return ChainBalance.builder().amount(new BigDecimal(solBalance.getBalance()).movePointLeft(tokenBaseInfo.getDecimals())).build();
    }

    @Override
    public ChainBalance tokenBalance(String mint, String userAddress) {
        TokenAccount account = solOperator.tokenAccount(mint, userAddress);
        if(account == null) return ChainBalance.builder().amount(BigDecimal.ZERO).build();
        TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getByAddress(mint);
        return ChainBalance.builder().amount(new BigDecimal(account.getAmount()).movePointLeft(tokenBaseInfo.getDecimals())).build();
    }

    @Resource
    private SolQueryService solQueryService;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private SolOperator solOperator;
    @Resource
    private TokenInfoService tokenInfoService;

}
