package com.rich.sol_bot.limit_order.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.limit_order.enums.OrderStatEnum;
import org.springframework.stereotype.Service;

@Service
public class LimitOrderRepository extends ServiceImpl<LimitOrderMapper, LimitOrder> {
    public boolean submitOrder(Long id){
        return this.update(new LambdaUpdateWrapper<LimitOrder>()
                .set(LimitOrder::getState, OrderStatEnum.submit)
                .eq(LimitOrder::getId, id)
                .eq(LimitOrder::getState, OrderStatEnum.created));
    }

    public boolean failOrder(Long id) {
        return this.update(new LambdaUpdateWrapper<LimitOrder>()
                .set(LimitOrder::getState, OrderStatEnum.fail)
                .eq(LimitOrder::getId, id)
                .eq(LimitOrder::getState, OrderStatEnum.submit));
    }

    public boolean successOrder(Long id) {
        return this.update(new LambdaUpdateWrapper<LimitOrder>()
                .set(LimitOrder::getState, OrderStatEnum.success)
                .eq(LimitOrder::getId, id)
                .eq(LimitOrder::getState, OrderStatEnum.submit));
    }

    public boolean cancelAll(Long uid) {
        return this.update(new LambdaUpdateWrapper<LimitOrder>()
                .set(LimitOrder::getState, OrderStatEnum.cancel)
                .eq(LimitOrder::getUid, uid)
                .eq(LimitOrder::getState, OrderStatEnum.created));
    }

    public boolean cancelOne(Long id) {
        return this.update(new LambdaUpdateWrapper<LimitOrder>()
                .set(LimitOrder::getState, OrderStatEnum.cancel)
                .eq(LimitOrder::getId, id)
                .eq(LimitOrder::getState, OrderStatEnum.created));
    }

    public boolean cancelAll(Long uid, Long tokenId) {
        return this.update(new LambdaUpdateWrapper<LimitOrder>()
                .set(LimitOrder::getState, OrderStatEnum.cancel)
                .eq(LimitOrder::getUid, uid).eq(LimitOrder::getTokenId, tokenId)
                .eq(LimitOrder::getState, OrderStatEnum.created));
    }
}
