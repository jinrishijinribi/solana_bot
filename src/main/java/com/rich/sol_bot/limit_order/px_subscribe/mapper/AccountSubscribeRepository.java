package com.rich.sol_bot.limit_order.px_subscribe.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.common.IdUtil;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AccountSubscribeRepository extends ServiceImpl<AccountSubscribeMapper, AccountSubscribe> {
    public AccountSubscribe generateOrUpdate(Long tokenId,String ammkey, String baseMint, String quoteMint, BigInteger basePnl, BigInteger quotePnl) {
        AccountSubscribe one = this.getOne(new LambdaQueryWrapper<AccountSubscribe>().eq(AccountSubscribe::getAmmkey, ammkey));
        if(one == null) {
            one = AccountSubscribe.builder().id(IdUtil.nextId())
                    .tokenId(tokenId)
                    .ammkey(ammkey).baseMint(baseMint).quoteMint(quoteMint)
                    .basePnl(basePnl).quotePnl(quotePnl)
                    .build();
            this.save(one);
        } else {
            one.setBasePnl(basePnl);
            one.setQuotePnl(quotePnl);
            this.updateById(one);
        }
        return one;
    }
}
