package com.rich.sol_bot.user.balance.log;

import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.user.balance.log.mapper.UserBalanceLog;
import com.rich.sol_bot.user.balance.log.mapper.UserBalanceLogRepository;
import com.rich.sol_bot.user.balance.mapper.UserBalance;
import com.rich.sol_bot.user.enums.BalanceLogTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserBalanceLogService {

    @Transactional
    public void generateLog(UserBalance balance, BigDecimal amount, BalanceLogTypeEnum type, Long bizId) {
        userBalanceLogRepository.save(UserBalanceLog.builder()
                        .id(IdUtil.nextId()).uid(balance.getUid()).amount(amount)
                        .coin(balance.getCoin()).remain(balance.getRemain()).freeze(balance.getFreeze())
                        .type(type).createdAt(TimestampUtil.now()).bizId(bizId)
                .build());
    }

    @Resource
    private UserBalanceLogRepository userBalanceLogRepository;
}
