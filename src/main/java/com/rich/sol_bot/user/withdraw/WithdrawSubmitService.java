package com.rich.sol_bot.user.withdraw;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.CreateTransaction;
import com.rich.sol_bot.sol.entity.SignSendResult;
import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.balance.UserBalanceService;
import com.rich.sol_bot.user.enums.BalanceLogTypeEnum;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLog;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLogRepository;
import com.rich.sol_bot.wallet.UserWalletService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.Collections;

@Service
public class WithdrawSubmitService {

    public String submitTransfer(UserWithdrawLog log) {
        try {
            String fromAddress = null;
            String fromSecret = null;
            TokenBaseInfo main = tokenInfoService.getMain();

            switch (log.getType()){
                case rebate -> {
                    fromAddress = systemConfigRepository.value(SystemConfigConstant.TRADE_REBATE_ADDRESS);
                    fromSecret = systemConfigRepository.valueDecode(SystemConfigConstant.TRADE_REBATE_SECRET);
                }
                case self_wallet -> {
                    fromAddress = log.getFromAddress();
                    fromSecret = userWalletService.getPriKey(log.getUid(), fromAddress);
                }
            }
            if(fromAddress == null || fromSecret == null) {
                return null;
            }
            CreateTransaction transaction = solOperator.solTransfer(fromAddress, log.getToAddress(), log.getAmount().movePointRight(main.getDecimals()).longValue(), fromAddress);
            SignSendResult result = solOperator.signSend(Collections.singletonList(fromSecret), transaction.getTransaction(), false);
            return result.getTxid();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void confirmTransfer(UserWithdrawLog log) {
//        if(userWithdrawLogRepository.update(new LambdaUpdateWrapper<UserWithdrawLog>()
//                        .set(UserWithdrawLog::getState, TradeStateEnum.success)
//                .eq(UserWithdrawLog::getId, log.getId()).eq(UserWithdrawLog::getState, TradeStateEnum.submit))){
//            userBalanceService.changBalanceByType(log.getUid(), log.getToken(), log.getAmount(), BalanceLogTypeEnum.withdraw_success, log.getId());
//        };
//    }


    @Resource
    private SolOperator solOperator;
    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private UserWithdrawLogRepository userWithdrawLogRepository;
    @Resource
    private UserBalanceService userBalanceService;
    @Resource
    private UserWalletService userWalletService;
}
