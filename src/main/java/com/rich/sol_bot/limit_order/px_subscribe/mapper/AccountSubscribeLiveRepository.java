package com.rich.sol_bot.limit_order.px_subscribe.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class AccountSubscribeLiveRepository extends ServiceImpl<AccountSubscribeLiveMapper, AccountSubscribeLive> {

    public AccountSubscribeLive generateAccountSubscribeLive(String ammkey, Long pid, String mint, Long amount, String address) {
        AccountSubscribeLive live = this.getOne(new LambdaQueryWrapper<AccountSubscribeLive>()
                .eq(AccountSubscribeLive::getAmmkey, ammkey).eq(AccountSubscribeLive::getMint, mint));
        if(live == null) {
            live = AccountSubscribeLive.builder()
                    .id(IdUtil.nextId()).ammkey(ammkey).accountSubscribeId(pid)
                    .mint(mint).address(address).amount(amount).submitId(null)
                    .updatedAt(TimestampUtil.now()).lastFailAt(null).checkFailCount(0)
                    .liveUntil(TimestampUtil.plus(1, TimeUnit.HOURS))
                    .build();
            this.save(live);
        } else {
            live.setLiveUntil(TimestampUtil.plus(1, TimeUnit.HOURS));
            this.updateById(live);
        }
        return live;
    }

    public void setSubmitId(Long id, Integer submitId) {
        this.update(new LambdaUpdateWrapper<AccountSubscribeLive>()
                .set(AccountSubscribeLive::getSubmitId, submitId)
                .eq(AccountSubscribeLive::getId, id));
    }

    public void finishCheck(Long id, Long amount, Integer failCount) {
        this.update(new LambdaUpdateWrapper<AccountSubscribeLive>()
                        .set(AccountSubscribeLive::getAmount, amount)
                        .set(AccountSubscribeLive::getLastFailAt, TimestampUtil.now())
                .set(AccountSubscribeLive::getCheckFailCount, failCount)
                .eq(AccountSubscribeLive::getId, id));
    }
}
