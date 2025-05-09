package com.rich.sol_bot.trade.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TokenBaseInfoRepository extends ServiceImpl<TokenBaseInfoMapper, TokenBaseInfo> {
    public TokenBaseInfo getByAddress(String address) {
        return this.getOne(new LambdaQueryWrapper<TokenBaseInfo>()
                .eq(TokenBaseInfo::getAddress, address)
                .last("limit 1"));

    }

    public void updatePx(Long id, BigDecimal px) {
        this.update(new LambdaUpdateWrapper<TokenBaseInfo>().set(TokenBaseInfo::getPrice, px).eq(TokenBaseInfo::getId, id));
    }

    public List<TokenBaseInfo> listByIds(List<Long> ids) {
        if(ids.isEmpty()) return new ArrayList<>();
        return this.list(new LambdaQueryWrapper<TokenBaseInfo>().in(TokenBaseInfo::getId, ids));
    }
}
