package com.rich.sol_bot.sol;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.sniper.mapper.*;
import com.rich.sol_bot.sol.entity.SignSendResult;
import com.rich.sol_bot.sol.queue.Message;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.trade.enums.TradeDexEnum;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoMapper;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.mapper.TradeInfoMapper;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.config.UserConfigService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangqiyun
 * @since 2024/3/20 17:18
 */

@Service
@Slf4j
public class SubmitTransactionService {

    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private UserConfigRepository userConfigRepository;

    public void handle(Message msg) {
        try {
            SignSendResult signSendResult = solOperator.signSend(List.of(msg.getPri_key()), msg.getTx(), false);
            String txid = signSendResult.getTxid();
            SniperPlanTx message = sniperPlanTxMapper.selectById(msg.getSniper_plan_tx_id());
            UserWallet userWallet = userWalletMapper.selectById(message.getWalletId());
            if (userWallet == null) return;
            sniperPlanTxMapper.update(null, new LambdaUpdateWrapper<SniperPlanTx>()
                            .set(SniperPlanTx::getSuccess, 1)
                    .set(SniperPlanTx::getTxId, txid).eq(SniperPlanTx::getId, message.getId()));
            SniperPlan sniperPlan = sniperPlanMapper.selectById(message.getPlanId());
            sniperPlanRepository.setDelete(sniperPlan.getId());
            TokenBaseInfo tokenBaseInfo = baseInfoMapper.selectById(sniperPlan.getTokenId());
            TokenBaseInfo main = tokenInfoService.getMain();
            UserConfig userConfig = userConfigRepository.getById(sniperPlan.getUid());
            tradeInfoMapper.insert(TradeInfo.builder().createdAt(TimestampUtil.now()).submitAt(TimestampUtil.now()).state(TradeStateEnum.submit)
                    .uid(message.getUid()).tokenInId(tokenBaseInfo.getId()).tokenIn(tokenBaseInfo.getSymbol()).tokenInAmount(BigDecimal.ZERO)
                    .tokenOutId(main.getId()).tokenOut(main.getSymbol()).tokenOutAmount(sniperPlan.getMainAmount())
                    .extraGas(sniperPlan.getExtraGas())
                    .slippage(sniperPlan.getSlippage())
                    .walletId(userWallet.getId()).platformFee(sniperPlan.getMainAmount()
                            .multiply(userConfig.getFeeRate())).source(TradeDexEnum.ray)
                    .tx(txid).id(IdUtil.nextId()).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private TokenBaseInfoMapper baseInfoMapper;
    @Resource
    private SniperPlanTxMapper sniperPlanTxMapper;
    @Resource
    private UserWalletMapper userWalletMapper;
    @Resource
    private SolOperator solOperator;
    @Resource
    private RaydiumOperator raydiumOperator;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private SniperPlanMapper sniperPlanMapper;
    @Resource
    private SniperPlanWalletMapper sniperPlanWalletMapper;
    @Resource
    private TradeInfoMapper tradeInfoMapper;
    @Resource
    private UserWalletService userWalletService;
    @Resource
    private SniperPlanRepository sniperPlanRepository;
}
