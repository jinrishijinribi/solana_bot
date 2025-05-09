package com.rich.sol_bot.wallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.CreateWallet;
import com.rich.sol_bot.sol.entity.TokenAccount;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.AesTool;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStatRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserWalletService {
    @Autowired
    private WalletBalanceStatRepository walletBalanceStatRepository;

    public UserWallet generateWallet(Long uid, String name) {
        CreateWallet wal = userWalletGenerateService.generateWallet();
        UserWallet wallet = UserWallet.builder()
                .id(IdUtil.nextId()).uid(uid)
                .name(name).address(wal.getAddress()).priKey(aesTool.encrypt(wal.getSecret()))
                .createdAt(TimestampUtil.now()).deleted(0)
                .build();
        userWalletRepository.save(wallet);
        return wallet;
    }

    public UserWallet generateWallet(Long uid, String name, String priKey) {
        CreateWallet wal = userWalletGenerateService.generateWalletByPri(priKey);
        UserWallet wallet = UserWallet.builder()
                .id(IdUtil.nextId()).uid(uid)
                .name(name).address(wal.getAddress()).priKey(aesTool.encrypt(priKey))
                .createdAt(TimestampUtil.now()).deleted(0)
                .build();
        userWalletRepository.save(wallet);
        return wallet;
    }

    public boolean walletExist(Long uid) {
        return userWalletRepository.count(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUid, uid)
                .eq(UserWallet::getDeleted, 0)) > 0;
    }

    public List<UserWallet> walletList(Long uid) {
        return userWalletRepository.list(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUid, uid)
                .eq(UserWallet::getDeleted, 0)
                .last("limit 10")
        );
    }

    public void removeWallet(Long id) {
        userWalletRepository.update(new LambdaUpdateWrapper<UserWallet>().set(UserWallet::getDeleted, 1)
                .eq(UserWallet::getId, id));
    }

    public void setWalletName(Long uid, Long id, String name) {
        userWalletRepository.update(new LambdaUpdateWrapper<UserWallet>().set(UserWallet::getName, name)
                        .eq(UserWallet::getUid, uid)
                .eq(UserWallet::getId, id));
    }

    public String getPriKey(Long uid, Long id) {
//        UserWallet wallet = userWalletRepository.getById(id);
        UserWallet wallet = userWalletRepository.getOne(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getId, id).eq(UserWallet::getUid, uid));
        if(wallet == null) return null;
        else return aesTool.decrypt(wallet.getPriKey());
    }

    public String getPriKey(Long uid, String address) {
        UserWallet wallet = userWalletRepository.ownedByAddressUid(address, uid);
        if(wallet == null) return null;
        else return aesTool.decrypt(wallet.getPriKey());
    }

    public String getPriKey(UserWallet wallet) {
        return aesTool.decrypt(wallet.getPriKey());
    }

    public BigDecimal checkBalance(String tokenAddress, String walletAddress) {
        try {
            TokenAccount account = solOperator.tokenAccount(tokenAddress, walletAddress);
            return new BigDecimal(account.getAmount());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }


    public UserWallet getPreferOne(Long uid) {
        UserConfig userConfig = userConfigRepository.getById(uid);
        return userWalletRepository.getPreferWallet(uid, userConfig.getPreferWallet());
    }

    public Long getMostHoldWalletId(Long uid, Long tokenId) {
        WalletBalanceStat stat = walletBalanceStatRepository.getOne(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getUid, uid).eq(WalletBalanceStat::getTokenId, tokenId).gt(WalletBalanceStat::getAmount, 0)
                .orderByDesc(WalletBalanceStat::getAmount).last("limit 1"));
        if(stat == null) return null;
        return stat.getWalletId();
    }


    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private UserWalletGenerateService userWalletGenerateService;
    @Resource
    private AesTool aesTool;
    @Resource
    private SolOperator solOperator;
    @Resource
    private UserConfigRepository userConfigRepository;
}
