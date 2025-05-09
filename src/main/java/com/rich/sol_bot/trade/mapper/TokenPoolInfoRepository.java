package com.rich.sol_bot.trade.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TokenPoolInfoRepository extends ServiceImpl<TokenPoolInfoMapper, TokenPoolInfo> {
    public void saveOrNothing(TokenPoolInfo tokenPoolInfo) {
        if(this.count(new LambdaQueryWrapper<TokenPoolInfo>().eq(TokenPoolInfo::getAmmKey, tokenPoolInfo.getAmmKey())) > 0){
            return;
        };
        this.save(tokenPoolInfo);
    }

    public TokenPoolInfo getByBaseMint(String baseMint) {
        return this.getOne(new LambdaQueryWrapper<TokenPoolInfo>()
                .eq(TokenPoolInfo::getBaseMint, baseMint).eq(TokenPoolInfo::getQuoteMint, TokenBaseInfo.mainAddress()).last("limit 1"));
    }
    
    public TokenPoolInfo buildTokenAddress(TokenPoolInfo poolInfo, String baseTokenAddress, String quoteTokenAddress) {
        this.update(new LambdaUpdateWrapper<TokenPoolInfo>()
                .set(TokenPoolInfo::getBaseTokenAddress, baseTokenAddress).set(TokenPoolInfo::getQuoteTokenAddress, quoteTokenAddress)
                .eq(TokenPoolInfo::getId, poolInfo.getId())
        );
        return this.getById(poolInfo.getId());
    }

    public TokenPoolInfo getBiggestPoolByBaseMint(String baseMint, String mainMint) {
        return this.getOne(new LambdaQueryWrapper<TokenPoolInfo>()
                .eq(TokenPoolInfo::getBaseMint, baseMint)
                .eq(TokenPoolInfo::getQuoteMint, mainMint)
                .last("limit 1"));
    }
}
