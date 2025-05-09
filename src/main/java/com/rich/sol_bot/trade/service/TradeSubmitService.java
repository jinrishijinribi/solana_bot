package com.rich.sol_bot.trade.service;

import com.rich.sol_bot.pump.PumpInfoService;
import com.rich.sol_bot.pump.mapper.PumpPoolInfo;
import com.rich.sol_bot.sol.RaydiumOperator;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.*;
import com.rich.sol_bot.sol.pump.PumpOperator;
import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.trade.dto.ConfirmTradeDTO;
import com.rich.sol_bot.trade.dto.PumpPriceDTO;
import com.rich.sol_bot.trade.enums.TradeDexEnum;
import com.rich.sol_bot.trade.error.TransactionErrorService;
import com.rich.sol_bot.trade.mapper.*;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class TradeSubmitService {
    @Autowired
    private PumpInfoService pumpInfoService;
    @Autowired
    private PumpOperator pumpOperator;
    @Autowired
    private SolOperator solOperator;
    @Autowired
    private TokenPxService tokenPxService;
    @Autowired
    private RaydiumOperator raydiumOperator;
    @Autowired
    private UserConfigRepository userConfigRepository;

    public TradeInfo submitTrade(TradeInfo tradeInfo) {
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        TokenBaseInfo baseToken = null;
        if(tokenMain.getId().equals(tradeInfo.getTokenOutId())){
            baseToken = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
        } else {
            baseToken = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
        }
        TokenPoolInfo tokenPoolInfo = tradePoolService.getPoolInfo(baseToken.getAddress());
        if(tokenPoolInfo != null) {
            return this.submitTradeToRay(tradeInfo);
        }
        PumpPoolInfo poolInf = pumpInfoService.getPumpInfo(baseToken.getAddress());
        if(poolInf != null) {
            return this.submitTradeToPump(tradeInfo);
        }
        return null;
    }


    /**
     * 提交交易到ray
     * @param tradeInfo
     * @return
     */
    public TradeInfo submitTradeToRay(TradeInfo tradeInfo) {
        try {
            UserWallet userWallet = userWalletRepository.getById(tradeInfo.getWalletId());
            if(userWallet == null) return null;
            TokenBaseInfo main = tokenInfoService.getMain();
            BigDecimal fee = BigDecimal.ZERO;
            String feeAddress = systemConfigRepository.value(SystemConfigConstant.TRADE_FEE_ADDRESS);
            UserConfig userConfig = userConfigRepository.getById(tradeInfo.getUid());
            BigDecimal feeRate = userConfig.getFeeRate();
            BigInteger minTokenInAmount = tradeInfo.getMinTokenInAmount();
            RayComputeAmountOut outputAmount = null;
            String inputMint = null;
            String outputMint = null;
            BigInteger inputAmount = BigInteger.ZERO;
            String ammkey = null;
            boolean dedicated = tradeInfo.getDedicated() == 1;
            if(main.getId().equals(tradeInfo.getTokenOutId())){
                // 买
                TokenBaseInfo token = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
                ammkey = tradePoolService.getAmKey(token.getAddress());
                if(ammkey == null) return null;
                inputMint = main.getAddress();
                outputMint = token.getAddress();
                inputAmount = BigInteger.valueOf(tradeInfo.getTokenOutAmount().movePointRight(main.getDecimals()).longValue());
                outputAmount = solQueryService.computeAmountOut(main.getAddress(), token.getAddress(),
                        ammkey,
                        inputAmount.longValue(),
                        tradeInfo.getSlippage().movePointRight(2).intValue()
                        );

                fee = new BigDecimal(tradeInfo.getTokenOutAmount().movePointRight(main.getDecimals()).longValue()).multiply(feeRate);
            }
            if(main.getId().equals(tradeInfo.getTokenInId())){
                // 卖
                TokenBaseInfo token = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
                ammkey = tradePoolService.getAmKey(token.getAddress());
                if(ammkey == null) return null;
                inputMint = token.getAddress();
                outputMint = main.getAddress();
                inputAmount = BigInteger.valueOf(tradeInfo.getTokenOutAmount().movePointRight(token.getDecimals()).longValue());
                outputAmount = solQueryService.computeAmountOut(token.getAddress(), main.getAddress(),
                        ammkey,
                        inputAmount.longValue(),
                        tradeInfo.getSlippage().movePointRight(2).intValue()
                );
                fee = new BigDecimal(outputAmount.getAmountOut()).multiply(feeRate);
            }
            if(outputAmount != null && outputAmount.getMinAmountOut() != null) {
                if(minTokenInAmount == null) {
                    minTokenInAmount = new BigInteger(outputAmount.getMinAmountOut());
                }
                String txId = null;
                // 不执行 快速模式的逻辑
                if(tradeInfo.getDedicated() == 0) {
                    txId = solQueryService.raySend(Collections.singletonList(userWalletService.getPriKey(userWallet)),
                        userWallet.getAddress(), tradeInfo.getExtraGas().movePointRight(main.getDecimals()).longValue(),
                        feeAddress, fee.longValue(), inputMint, outputMint, ammkey,
                        inputAmount.longValue(), minTokenInAmount.longValue(), dedicated
                    );
                } else {
                    // 所有的交易都走jito
                    String transaction = raydiumOperator.rayCalc(userWallet.getAddress(), 0L,
                            feeAddress, fee.longValue(), inputMint, outputMint, ammkey,
                            inputAmount.longValue(), minTokenInAmount.longValue()
                    );
                    SignSendJitoResult result = solOperator.jitoSignSend(
                            Collections.singletonList(userWalletService.getPriKey(userWallet)), Collections.singletonList(transaction), userWallet.getAddress(),
                            tradeInfo.getExtraGas().movePointRight(main.getDecimals()).toBigInteger()
                    );
                    if(result.getTxid().size() == 2) {
                        txId = result.getTxid().get(0);
                    }
                }
                tradeInfo.setTx(txId);
                tradeInfo.setPlatformFee(fee.movePointLeft(main.getDecimals()));
                tradeInfo.setSource(TradeDexEnum.ray);
                return tradeInfo;
            }
        } catch (Exception e) {
            log.error("send tx", e);
        }
        return tradeInfo;
    }


    /**
     * 提交交易到pump
     * @param tradeInfo
     * @return
     */
    public TradeInfo submitTradeToPump(TradeInfo tradeInfo) {
        try {
            UserWallet userWallet = userWalletRepository.getById(tradeInfo.getWalletId());
            if(userWallet == null) return null;
            TokenBaseInfo main = tokenInfoService.getMain();
            BigDecimal fee = BigDecimal.ZERO;
            String feeAddress = systemConfigRepository.value(SystemConfigConstant.TRADE_FEE_ADDRESS);
            UserConfig userConfig = userConfigRepository.getById(tradeInfo.getUid());
            BigDecimal feeRate = userConfig.getFeeRate();
            BigDecimal solAmount = BigDecimal.ZERO;
            BigDecimal tokenAmount = BigDecimal.ZERO;
            PumpPoolInfo poolInfo = null;
            boolean dedicated = tradeInfo.getDedicated() == 1;
            TokenBaseInfo token = null;
            boolean buy = true;

            if(main.getId().equals(tradeInfo.getTokenOutId())){
                // 买
                token = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
                poolInfo = pumpInfoService.getPumpInfo(token.getAddress());
                PumpPriceDTO pumpPriceDTO = tokenPxService.getPumpPx(token, poolInfo.getBondingCurve());

                solAmount = tradeInfo.getTokenOutAmount();
                tokenAmount = solAmount.divide(pumpPriceDTO.getPx().divide(new BigDecimal(main.getPrice()), RoundingMode.HALF_DOWN), RoundingMode.HALF_DOWN);
                fee = solAmount.multiply(feeRate);
                solAmount = solAmount.multiply(BigDecimal.ONE.add(tradeInfo.getSlippage()));
                buy = true;
            }
            if(main.getId().equals(tradeInfo.getTokenInId())){
                // 卖
                token = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
                poolInfo = pumpInfoService.getPumpInfo(token.getAddress());
                PumpPriceDTO pumpPriceDTO = tokenPxService.getPumpPx(token, poolInfo.getBondingCurve());
                tokenAmount = tradeInfo.getTokenOutAmount();
                solAmount = tokenAmount.multiply(pumpPriceDTO.getPx().divide(new BigDecimal(main.getPrice()), RoundingMode.HALF_DOWN)).multiply(BigDecimal.ONE.subtract(tradeInfo.getSlippage()));
                fee = solAmount.multiply(feeRate);
                buy = false;
            }
            if(poolInfo != null) {
                String txHash = pumpOperator.calPump(PumpCal.builder()
                                .priorityFee(tradeInfo.getExtraGas().movePointRight(main.getDecimals()).setScale(0, RoundingMode.HALF_DOWN).toPlainString())
                                .owner(userWallet.getAddress())
                                .mint(token.getAddress())
                                .hold(poolInfo.getBondingCurve())
                                .amount(tokenAmount.movePointRight(token.getDecimals()).setScale(0, RoundingMode.HALF_DOWN).toPlainString())
                                .sol(solAmount.movePointRight(main.getDecimals()).setScale(0, RoundingMode.HALF_DOWN).toPlainString())
                                .feeAccount(feeAddress)
                                .fee(fee.movePointRight(main.getDecimals()).setScale(0, RoundingMode.HALF_DOWN).toPlainString())
                                .buy(buy)
                        .build());
                SignSendResult result = solOperator.signSend(Collections.singletonList(userWalletService.getPriKey(userWallet)), txHash, dedicated);
                tradeInfo.setTx(result.getTxid());
                tradeInfo.setPlatformFee(fee);
                tradeInfo.setSource(TradeDexEnum.pump);
                return tradeInfo;
            }
        } catch (Exception e) {
            log.error("send tx", e);
        }
        return tradeInfo;
    }


//    /**
//     * 确认交易
//     */
//    public ConfirmTradeDTO confirmTrade(TradeInfo tradeInfo) {
//        Transaction transaction = solQueryService.getTransaction(tradeInfo.getTx());
//        if(transaction == null) return null;
//        if(transaction.getMeta().getErr() != null) {
//            String errorMsg = transactionErrorService.getError(transaction.getMeta().getLogMessages());
//            return ConfirmTradeDTO.builder().success(false).tradeId(tradeInfo.getId()).errorMsg(errorMsg).build();
//        }
//        TokenBaseInfo tokenMain = tokenInfoService.getMain();
//        TokenBaseInfo baseToken = null;
//        if(tradeInfo.getTokenInId().equals(tokenMain.getId())){
//            baseToken = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
//        } else {
//            baseToken = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
//        }
//        UserWallet userWallet = userWalletRepository.getById(tradeInfo.getWalletId());
//        BigDecimal gas = new BigDecimal(transaction.getMeta().getFee());
//        BigDecimal mainChange = BigDecimal.ZERO;
//        BigDecimal tokenChange = BigDecimal.ZERO;
//
//        Transaction.TransactionDetail.Message message = transaction.getTransaction().getMessage();
//        List<Transaction.TransactionDetail.Message.AccountKeys> keys = message.getAccountKeys();
//        List<Long> preMain = transaction.getMeta().getPreBalances();
//        List<Long> postMain = transaction.getMeta().getPostBalances();
//        gas = gas.movePointLeft(tokenMain.getDecimals());
//        for (int i = 0; i < keys.size(); i ++ ) {
//            if(keys.get(i).getPubkey().equals(userWallet.getAddress())){
//                mainChange = mainChange.add(new BigDecimal(postMain.get(i) - preMain.get(i)));
//            }
//        }
//        if(tokenMain.getId().equals(tradeInfo.getTokenOutId())){
//            mainChange = mainChange.movePointLeft(tokenMain.getDecimals()).add(tradeInfo.getPlatformFee()).add(gas);
//        } else {
//            mainChange = mainChange.movePointLeft(tokenMain.getDecimals()).add(tradeInfo.getPlatformFee()).add(gas);
//        }
//        List<Transaction.Meta.TokenBalances> preBalances = transaction.getMeta().getPreTokenBalances();
//        List<Transaction.Meta.TokenBalances> postBalances = transaction.getMeta().getPostTokenBalances();
//        BigDecimal before = BigDecimal.ZERO;
//        BigDecimal after = BigDecimal.ZERO;
//        for(Transaction.Meta.TokenBalances i : preBalances) {
//            if(i.getMint().equalsIgnoreCase(baseToken.getAddress()) && i.getOwner().equalsIgnoreCase(userWallet.getAddress())){
//                before = new BigDecimal(i.getUiTokenAmount().getAmount());
//            }
//        }
//        for(Transaction.Meta.TokenBalances i : postBalances) {
//            if(i.getMint().equalsIgnoreCase(baseToken.getAddress()) && i.getOwner().equalsIgnoreCase(userWallet.getAddress())){
//                after = new BigDecimal(i.getUiTokenAmount().getAmount());
//            }
//        }
//        tokenChange = after.subtract(before).movePointLeft(baseToken.getDecimals());
//
//        if(tokenMain.getId().equals(tradeInfo.getTokenInId())) {
//            tradeService.tradeSuccess(tradeInfo.getId(), mainChange.abs(), tokenChange.abs(), gas, new BigDecimal(tokenMain.getPrice()));
//        }
//        if(tokenMain.getId().equals(tradeInfo.getTokenOutId())){
//            tradeService.tradeSuccess(tradeInfo.getId(), tokenChange.abs(), mainChange.abs(), gas, new BigDecimal(tokenMain.getPrice()));
//        }
//
//        return ConfirmTradeDTO.builder().success(true).tradeId(tradeInfo.getId()).build();
//    }


    /**
     * 确认交易
     */
    public ConfirmTradeDTO confirmTrade(TradeInfo tradeInfo) {
        Transaction transaction = solQueryService.getTransaction(tradeInfo.getTx());
        if(transaction == null) return null;
        if(transaction.getMeta().getErr() != null) {
            String errorMsg = transactionErrorService.getError(transaction.getMeta().getLogMessages());
            return ConfirmTradeDTO.builder().success(false).tradeId(tradeInfo.getId()).errorMsg(errorMsg).build();
        }
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        TokenBaseInfo baseToken = null;
        if(tradeInfo.getTokenInId().equals(tokenMain.getId())){
            baseToken = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
        } else {
            baseToken = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
        }


        UserWallet userWallet = userWalletRepository.getById(tradeInfo.getWalletId());
        BigDecimal gas = new BigDecimal(transaction.getMeta().getFee());
        BigDecimal mainChange = BigDecimal.ZERO;
        BigDecimal tokenChange = BigDecimal.ZERO;

        Transaction.TransactionDetail.Message message = transaction.getTransaction().getMessage();
        List<Transaction.TransactionDetail.Message.AccountKeys> keys = message.getAccountKeys();
        List<Long> preMain = transaction.getMeta().getPreBalances();
        List<Long> postMain = transaction.getMeta().getPostBalances();
        gas = gas.movePointLeft(tokenMain.getDecimals());
//        TokenPoolInfo tokenPoolInfo = tradePoolService.getPoolInfo(baseToken.getAddress());
        String mainPoolAddress = null;
        if(TradeDexEnum.pump.equals(tradeInfo.getSource())){
            PumpPoolInfo poolInfo = pumpInfoService.getPumpInfo(baseToken.getAddress());
            mainPoolAddress = poolInfo.getBondingCurve();
        }
        if(TradeDexEnum.ray.equals(tradeInfo.getSource())) {
            TokenPoolInfo tokenPoolInfo = tradePoolService.getPoolInfo(baseToken.getAddress());
            mainPoolAddress = tokenPoolInfo.getQuoteTokenAddress();
        }
        // 计算主币变化, 直接计算和池子sol交互的地址
        for (int i = 0; i < keys.size(); i ++ ) {
            if(keys.get(i).getPubkey().equals(mainPoolAddress)){
                mainChange = mainChange.add(new BigDecimal(postMain.get(i) - preMain.get(i)));
            }
        }
        mainChange = mainChange.movePointLeft(tokenMain.getDecimals());

        List<Transaction.Meta.TokenBalances> preBalances = transaction.getMeta().getPreTokenBalances();
        List<Transaction.Meta.TokenBalances> postBalances = transaction.getMeta().getPostTokenBalances();
        BigDecimal before = BigDecimal.ZERO;
        BigDecimal after = BigDecimal.ZERO;
        for(Transaction.Meta.TokenBalances i : preBalances) {
            if(i.getMint().equalsIgnoreCase(baseToken.getAddress()) && i.getOwner().equalsIgnoreCase(userWallet.getAddress())){
                before = new BigDecimal(i.getUiTokenAmount().getAmount());
            }
        }
        for(Transaction.Meta.TokenBalances i : postBalances) {
            if(i.getMint().equalsIgnoreCase(baseToken.getAddress()) && i.getOwner().equalsIgnoreCase(userWallet.getAddress())){
                after = new BigDecimal(i.getUiTokenAmount().getAmount());
            }
        }
        tokenChange = after.subtract(before).movePointLeft(baseToken.getDecimals());

        if(tokenMain.getId().equals(tradeInfo.getTokenInId())) {
            tradeService.tradeSuccess(tradeInfo.getId(), mainChange.abs(), tokenChange.abs(), gas, new BigDecimal(tokenMain.getPrice()));
        }
        if(tokenMain.getId().equals(tradeInfo.getTokenOutId())){
            tradeService.tradeSuccess(tradeInfo.getId(), tokenChange.abs(), mainChange.abs(), gas, new BigDecimal(tokenMain.getPrice()));
        }

        return ConfirmTradeDTO.builder().success(true).tradeId(tradeInfo.getId()).build();
    }



    @Resource
    private SolQueryService solQueryService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private TradeService tradeService;
    @Resource
    private UserWalletService userWalletService;
    @Resource
    @Lazy
    private TradePoolService tradePoolService;
    @Resource
    private TransactionErrorService transactionErrorService;


}
