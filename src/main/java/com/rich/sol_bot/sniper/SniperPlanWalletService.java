package com.rich.sol_bot.sniper;

import com.rich.sol_bot.sniper.mapper.SniperPlanWallet;
import com.rich.sol_bot.sniper.mapper.SniperPlanWalletRepository;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SniperPlanWalletService {
    public void bindAllWallet(Long uid, Long planId) {
        List<UserWallet> wallets = userWalletService.walletList(uid);
        sniperPlanWalletRepository.unbindAll(uid, planId);
        List<SniperPlanWallet> planWallets = new ArrayList<>();
        wallets.forEach(o -> {
            planWallets.add(SniperPlanWallet.builder().id(IdUtil.nextId())
                            .walletId(o.getId()).planId(planId).uid(uid)
                    .createdAt(TimestampUtil.now())
                    .build());
        });
        sniperPlanWalletRepository.saveBatch(planWallets);
    }

    @Resource
    private UserWalletService userWalletService;
    @Resource
    private SniperPlanWalletRepository sniperPlanWalletRepository;
}
