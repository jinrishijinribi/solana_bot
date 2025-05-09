package com.rich.sol_bot.wallet.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserWalletRepository extends ServiceImpl<UserWalletMapper, UserWallet> {
    public UserWallet ownedByUid(Long id, Long uid) {
        return this.getOne(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUid, uid).eq(UserWallet::getId, id)
        );
    }

    public UserWallet ownedByAddressUid(String address, Long uid) {
        return this.getOne(new LambdaQueryWrapper<UserWallet>().eq(UserWallet::getAddress, address)
                .eq(UserWallet::getUid, uid).last("limit 1"));
    }

    public UserWallet getByAddress(String address) {
        return this.getOne(new LambdaQueryWrapper<UserWallet>().eq(UserWallet::getAddress, address).last("limit 1"));
    }

    public List<UserWallet> listByUid(Long uid) {
        return this.list(new LambdaQueryWrapper<UserWallet>().eq(UserWallet::getUid, uid)
                .eq(UserWallet::getDeleted, 0).orderByAsc(UserWallet::getId)
        );
    }

    public UserWallet getPreferWallet(Long uid, Long walletId) {
        List<UserWallet> wallets = this.listByUid(uid);
        if(wallets.isEmpty()) return null;
        if(walletId == null) return wallets.get(0);
        List<UserWallet> prefers = wallets.stream().filter(o -> o.getId().equals(walletId)).toList();
        if(!prefers.isEmpty()) return prefers.get(0);
        return wallets.get(0);
    }

}
