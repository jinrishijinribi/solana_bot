package com.rich.sol_bot.user.config;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.mapper.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserConfigService {

    public void updateFast(Long uid, BigDecimal value) {
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>()
                        .set(UserConfig::getFastSlippage, value)
                .eq(UserConfig::getId, uid));
    }

    public void updateProtect(Long uid, BigDecimal value) {
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>()
                .set(UserConfig::getProtectSlippage, value)
                .eq(UserConfig::getId, uid));
    }

    public void updateBuyFee(Long uid, BigDecimal value) {
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>()
                .set(UserConfig::getBuyFee, value)
                .eq(UserConfig::getId, uid));
    }

    public void updateSellFee(Long uid, BigDecimal value) {
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>()
                .set(UserConfig::getSellFee, value)
                .eq(UserConfig::getId, uid));
    }

    public void updateSniperFee(Long uid, BigDecimal value) {
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>()
                .set(UserConfig::getSniperFee, value)
                .eq(UserConfig::getId, uid));
    }

    public void updateJitoFee(Long uid, BigDecimal value) {
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>()
                .set(UserConfig::getJitoFee, value)
                .eq(UserConfig::getId, uid)
        );
    }

    public void updatePreferWallet(Long uid, Long walletId) {
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>()
                .set(UserConfig::getPreferWallet, walletId)
                .eq(UserConfig::getId, uid)
        );
    }

    public void switchAutoSell(Long uid) {
        UserConfig userConfig = userConfigRepository.getById(uid);
        if(userConfig.getAutoSell() == 1) {
            userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>().set(UserConfig::getAutoSell, 0)
                    .eq(UserConfig::getId, uid)
                    .eq(UserConfig::getAutoSell,1));
        } else {
            userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>().set(UserConfig::getAutoSell, 1)
                    .eq(UserConfig::getId, uid)
                    .eq(UserConfig::getAutoSell,0));
        }
    }

    @Resource
    private UserConfigRepository userConfigRepository;
}
