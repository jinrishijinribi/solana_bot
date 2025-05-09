package com.rich.sol_bot.wallet.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletBalanceStatRepository extends ServiceImpl<WalletBalanceStatMapper, WalletBalanceStat> {
    public WalletBalanceStat getOrCreate(Long uid, Long walletId, Long tokenId) {
        WalletBalanceStat stat = this.getOne(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getWalletId, walletId).eq(WalletBalanceStat::getUid, uid)
                .eq(WalletBalanceStat::getTokenId, tokenId).last("limit 1")
        );
        if(stat == null) {
            stat = WalletBalanceStat.builder()
                    .id(IdUtil.nextId()).tokenId(tokenId).walletId(walletId).uid(uid)
                    .px(BigDecimal.ZERO)
                    .amount(BigDecimal.ZERO).val(BigDecimal.ZERO).updatedAt(TimestampUtil.now())
                    .build();
            this.save(stat);
        }
        return stat;
    }

    public WalletBalanceStat createOrUpdate(Long uid, Long walletId, Long tokenId, BigDecimal amount) {
        WalletBalanceStat stat = this.getOne(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getWalletId, walletId).eq(WalletBalanceStat::getUid, uid)
                .eq(WalletBalanceStat::getTokenId, tokenId).last("limit 1")
        );
        if(stat == null) {
            stat = WalletBalanceStat.builder()
                    .id(IdUtil.nextId()).tokenId(tokenId).walletId(walletId).uid(uid)
                    .amount(amount).val(BigDecimal.ZERO).updatedAt(TimestampUtil.now())
                    .build();
        } else {
            this.update(new LambdaUpdateWrapper<WalletBalanceStat>().set(WalletBalanceStat::getAmount, amount).eq(WalletBalanceStat::getId, stat.getId()));
        }
        return stat;
    }

    @Transactional
    public Long decreaseBalance(Long id, BigDecimal amount, BigDecimal val, BigDecimal avgPx) {
        return walletBalanceStatMapper.decreaseBalance(id, amount, val, avgPx);
    }

    @Transactional
    public Long increaseBalance(Long id, BigDecimal amount, BigDecimal val, BigDecimal avgPx) {
        return walletBalanceStatMapper.increaseBalance(id, amount, val, avgPx);
    }

    @Transactional
    public Long increaseBalanceAndSetStart(Long id, BigDecimal amount, BigDecimal val, BigDecimal avgPx) {
        return walletBalanceStatMapper.increaseBalanceAndSetStart(id, amount, val, avgPx, TimestampUtil.now());
    }

    public void resetBalance(Long id) {
        this.update(new LambdaUpdateWrapper<WalletBalanceStat>()
                .set(WalletBalanceStat::getAmount, BigDecimal.ZERO)
                .set(WalletBalanceStat::getVal, BigDecimal.ZERO).set(WalletBalanceStat::getPx, BigDecimal.ZERO)
                        .set(WalletBalanceStat::getHoldStartAt, null)
                .eq(WalletBalanceStat::getId, id));
    }

    public List<WalletBalanceStat> listByUid(Long uid) {
        return this.list(new LambdaQueryWrapper<WalletBalanceStat>().eq(WalletBalanceStat::getUid, uid));
    }

    public List<WalletBalanceStat> listExistByUidAndWalletId(Long uid, Long walletId) {
        return this.list(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getUid, uid).eq(WalletBalanceStat::getWalletId, walletId)
                .gt(WalletBalanceStat::getAmount, 0)
                .orderByDesc(WalletBalanceStat::getUpdatedAt)
        );
    }

    public List<WalletBalanceStat> listExistByTokenId(Long tokenId) {
        return this.list(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getTokenId, tokenId)
                .gt(WalletBalanceStat::getAmount, 0)
        );
    }

    public List<WalletBalanceStat> listExistByUidAndWalletIdWithoutMain(Long uid, Long walletId) {
        return this.list(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getUid, uid).eq(WalletBalanceStat::getWalletId, walletId)
                .gt(WalletBalanceStat::getAmount, 0)
                .orderByDesc(WalletBalanceStat::getUpdatedAt)
        );
    }


    @Resource
    private WalletBalanceStatMapper walletBalanceStatMapper;
}
