package com.rich.sol_bot.sol.controller;

import com.rich.sol_bot.grpc_compile.CompileService;
import com.rich.sol_bot.sol.RouteScanService;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.RaydiumMint;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoin;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoinMapper;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.service.TradePoolService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Map;

/**
 * @author wangqiyun
 * @since 2024/3/23 22:42
 */

@RestController
@RequestMapping("/api/route")
public class RouteScanController {
    @Autowired
    private TradePoolService tradePoolService;
    @Autowired
    private TokenBaseInfoRepository tokenBaseInfoRepository;

    @PostMapping("/scan")
    public Map<String, String> scan(@RequestBody @Valid RouteScanDTO routeScanDTO) throws Exception {
        System.out.println("添加池子发现1:" + TimestampUtil.now().getTime() + ":" + routeScanDTO.getId() + ":" + routeScanDTO.getBaseMint() + ":" + routeScanDTO.getQuoteMint());
        if (newCoinMapper.selectById(routeScanDTO.getId()) != null) return Map.of("code", "0");
        RaydiumMint raydiumMint = solOperator.raydiumMint(routeScanDTO.getId());
        if (StringUtils.hasLength(raydiumMint.getPoolOpenTime())) {
            NewCoin newCoin = new NewCoin().setAmmKey(routeScanDTO.getId()).setBaseMint(routeScanDTO.getBaseMint())
                    .setPoolOpenTime(Long.valueOf(raydiumMint.getPoolOpenTime()))
                    .setQuoteMint(routeScanDTO.getQuoteMint()).setBaseAmount(new BigInteger(raydiumMint.getBaseVault().getAmount()))
                    .setQuoteAmount(new BigInteger(raydiumMint.getQuoteVault().getAmount()));
            newCoinMapper.insert(newCoin);
            ThreadAsyncUtil.execAsync(() -> {
                tradePoolService.buildTokenPoolInfoFromNewCoin(newCoin);
            });
        }
//        routeScanService.scan(routeScanDTO.getId(), routeScanDTO.getBaseMint(), routeScanDTO.getQuoteMint());
        if (routeScanDTO.getBaseMint().equals("So11111111111111111111111111111111111111112")) {
            routeScanService.newCoinScan(routeScanDTO.getQuoteMint());
        } else if (routeScanDTO.getQuoteMint().equals("So11111111111111111111111111111111111111112")) {
            routeScanService.newCoinScan(routeScanDTO.getBaseMint());
        }

        return Map.of("code", "0");
    }


    @PostMapping("/scan2")
    public Map<String, String> scan(@RequestBody @Valid String msg) throws Exception {
        RouteScanDTO routeScanDTO = CompileService.compile(msg);
        if(routeScanDTO == null) return Map.of("code", "0");
        System.out.println("添加池子发现2:" + TimestampUtil.now().getTime() + ":" + routeScanDTO.getId() + ":" + routeScanDTO.getBaseMint() + ":" + routeScanDTO.getQuoteMint());
        if (newCoinMapper.selectById(routeScanDTO.getId()) != null) return Map.of("code", "0");
        RaydiumMint raydiumMint = solOperator.raydiumMint(routeScanDTO.getId());
        if (StringUtils.hasLength(raydiumMint.getPoolOpenTime())) {
            NewCoin newCoin = new NewCoin().setAmmKey(routeScanDTO.getId()).setBaseMint(routeScanDTO.getBaseMint())
                    .setPoolOpenTime(Long.valueOf(raydiumMint.getPoolOpenTime()))
                    .setQuoteMint(routeScanDTO.getQuoteMint()).setBaseAmount(new BigInteger(raydiumMint.getBaseVault().getAmount()))
                    .setQuoteAmount(new BigInteger(raydiumMint.getQuoteVault().getAmount()));
            newCoinMapper.insert(newCoin);
            ThreadAsyncUtil.execAsync(() -> {
                tradePoolService.buildTokenPoolInfoFromNewCoin(newCoin);
            });
        }
//        routeScanService.scan(routeScanDTO.getId(), routeScanDTO.getBaseMint(), routeScanDTO.getQuoteMint());
        if (routeScanDTO.getBaseMint().equals("So11111111111111111111111111111111111111112")) {
            routeScanService.newCoinScan(routeScanDTO.getQuoteMint());
        } else if (routeScanDTO.getQuoteMint().equals("So11111111111111111111111111111111111111112")) {
            routeScanService.newCoinScan(routeScanDTO.getBaseMint());
        }

        return Map.of("code", "0");
    }


    @Resource
    private RouteScanService routeScanService;
    @Resource
    private SolOperator solOperator;
    @Resource
    private NewCoinMapper newCoinMapper;
}
