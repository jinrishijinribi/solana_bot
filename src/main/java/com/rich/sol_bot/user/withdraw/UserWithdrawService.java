package com.rich.sol_bot.user.withdraw;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.bot.service.BotNotifyService;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.config.SystemConfig;
import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.balance.UserBalanceService;
import com.rich.sol_bot.user.balance.mapper.UserBalance;
import com.rich.sol_bot.user.balance.mapper.UserBalanceRepository;
import com.rich.sol_bot.user.enums.BalanceLogTypeEnum;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLog;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLogRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserWithdrawService {
    @Transactional
    public Boolean withdrawRebate(Long uid,  String toAddress) {
        UserBalance balance = userBalanceRepository.getOrCreateMain(uid);
        TokenBaseInfo mainInfo = tokenInfoService.getMain();
        String rebateAddress = systemConfigRepository.value(SystemConfigConstant.TRADE_REBATE_ADDRESS);
        if(balance.getRemain().compareTo(BigDecimal.ZERO) <= 0) return false;
        UserWithdrawLog log = UserWithdrawLog.builder().id(IdUtil.nextId())
                .uid(uid).amount(balance.getRemain()).type(WithdrawLogType.rebate)
                .token(mainInfo.getSymbol())
                .fromAddress(rebateAddress).toAddress(toAddress).state(TradeStateEnum.created)
                .createdAt(TimestampUtil.now())
                .build();
        if(userBalanceService.changBalanceByType(uid, mainInfo.getSymbol(), balance.getRemain(), BalanceLogTypeEnum.withdraw_freeze, log.getId())){
            userWithdrawLogRepository.save(log);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean withdrawWallet(Long uid, Long walletId, BigDecimal amount,  String toAddress) {
        UserWallet wallet = userWalletRepository.ownedByUid(walletId, uid);
        TokenBaseInfo mainInfo = tokenInfoService.getMain();
        UserWithdrawLog log = UserWithdrawLog.builder().id(IdUtil.nextId())
                .uid(uid).amount(amount).type(WithdrawLogType.self_wallet)
                .token(mainInfo.getSymbol())
                .fromAddress(wallet.getAddress()).toAddress(toAddress).state(TradeStateEnum.created)
                .createdAt(TimestampUtil.now())
                .build();
        userWithdrawLogRepository.save(log);
        return true;
    }

    public void confirmTransfer(UserWithdrawLog log) {
        if(userWithdrawLogRepository.update(new LambdaUpdateWrapper<UserWithdrawLog>()
                .set(UserWithdrawLog::getState, TradeStateEnum.success)
                .eq(UserWithdrawLog::getId, log.getId()).eq(UserWithdrawLog::getState, TradeStateEnum.submit))){
            if(log.getType().equals(WithdrawLogType.rebate)){
                userBalanceService.changBalanceByType(log.getUid(), log.getToken(), log.getAmount(), BalanceLogTypeEnum.withdraw_success, log.getId());
            }
            botNotifyService.pushSuccessWithdraw(log);
        };
    }

    public void failTransfer(UserWithdrawLog log) {
        if(userWithdrawLogRepository.update(new LambdaUpdateWrapper<UserWithdrawLog>()
                .set(UserWithdrawLog::getState, TradeStateEnum.fail)
                .eq(UserWithdrawLog::getId, log.getId()).eq(UserWithdrawLog::getState, TradeStateEnum.submit))){
            if(log.getType().equals(WithdrawLogType.rebate)){
                userBalanceService.changBalanceByType(log.getUid(), log.getToken(), log.getAmount(), BalanceLogTypeEnum.withdraw_fail, log.getId());
            }
            botNotifyService.pushFailWithdraw(log);
        };
    }

    @Resource
    private UserWithdrawLogRepository userWithdrawLogRepository;
    @Resource
    private UserBalanceService userBalanceService;
    @Resource
    private UserBalanceRepository userBalanceRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private BotNotifyService botNotifyService;
}
