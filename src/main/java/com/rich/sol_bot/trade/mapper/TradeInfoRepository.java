package com.rich.sol_bot.trade.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.sniper.enums.SniperStateEnum;
import com.rich.sol_bot.trade.enums.TradeSideEnum;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TradeInfoRepository extends ServiceImpl<TradeInfoMapper, TradeInfo> {
    public Long countUid(Long tokenId) {
        return tradeInfoMapper.countUidToken(tokenId);
    }

    public boolean isProtect(Long uid, Long tokenId) {
        TradeInfo info = this.getOne(new LambdaQueryWrapper<TradeInfo>().eq(TradeInfo::getUid, uid)
                .eq(TradeInfo::getTokenInId, tokenId).last("limit 1"));
        if(info == null) return false;
        return info.getDedicated() == 1;
    }

    @Resource
    private TradeInfoMapper tradeInfoMapper;
}
