package com.rich.sol_bot.wallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rich.sol_bot.sol.entity.SolBalance;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.cache.TokenBalanceCacheService;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenBalanceService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.trade.service.TokenPxService;
import com.rich.sol_bot.wallet.dto.WalletProfitStaticDTO;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStatRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class WalletBalanceStatService {
    @Transactional
    public void loadTrade(TradeInfo tradeInfo) {
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        if(mainToken.getId().equals(tradeInfo.getTokenOutId())){
            // buy
            BigDecimal mainPx = new BigDecimal(mainToken.getPrice());
            BigDecimal avgPx = BigDecimal.ZERO;
            WalletBalanceStat stat = walletBalanceStatRepository.getOrCreate(tradeInfo.getUid(), tradeInfo.getWalletId(), tradeInfo.getTokenInId());
            BigDecimal avgPxBefore = stat.getPx();
            if(stat.getPx().compareTo(BigDecimal.ZERO) == 0 && stat.getAmount().compareTo(BigDecimal.ZERO) > 0 && stat.getVal().compareTo(BigDecimal.ZERO) > 0) {
                avgPxBefore = stat.getVal().multiply(mainPx).divide(stat.getAmount(), RoundingMode.HALF_DOWN);
            }
            if(stat.getAmount().compareTo(BigDecimal.ZERO) == 0) {

            }
            BigDecimal nowAmount = stat.getAmount().add(tradeInfo.getTokenInAmount());
            if(nowAmount.compareTo(BigDecimal.ZERO) > 0) {
                // （之前u价值 + 变化u价值） / 当前持仓量
                avgPx = stat.getAmount().multiply(avgPxBefore)
                        .add(tradeInfo.getTokenOutAmount().multiply(mainPx))
                        .divide(nowAmount, RoundingMode.HALF_DOWN);
            }
            if(stat.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                walletBalanceStatRepository.increaseBalanceAndSetStart(stat.getId(), tradeInfo.getTokenInAmount(), tradeInfo.getTokenOutAmount(), avgPx);
            } else {
                walletBalanceStatRepository.increaseBalance(stat.getId(), tradeInfo.getTokenInAmount(), tradeInfo.getTokenOutAmount(), avgPx);
            }
        }
        if(mainToken.getId().equals(tradeInfo.getTokenInId())) {
            BigDecimal mainPx = new BigDecimal(mainToken.getPrice());
            BigDecimal avgPx = BigDecimal.ZERO;
            WalletBalanceStat stat = walletBalanceStatRepository.getOrCreate(tradeInfo.getUid(), tradeInfo.getWalletId(), tradeInfo.getTokenOutId());
            BigDecimal avgPxBefore = stat.getPx();
            // sell
            if(stat.getAmount().compareTo(tradeInfo.getTokenOutAmount()) <= 0) {
                walletBalanceStatRepository.resetBalance(stat.getId());
            } else {
                if(stat.getPx().compareTo(BigDecimal.ZERO) == 0 && stat.getAmount().compareTo(BigDecimal.ZERO) > 0 && stat.getVal().compareTo(BigDecimal.ZERO) > 0) {
                    avgPxBefore = stat.getVal().multiply(mainPx).divide(stat.getAmount(), RoundingMode.HALF_DOWN);
                }
                BigDecimal nowAmount = stat.getAmount().subtract(tradeInfo.getTokenOutAmount());
                if(nowAmount.compareTo(BigDecimal.ZERO) > 0) {
                    // （之前u价值 - 变化u价值） / 当前持仓量
                    avgPx = stat.getAmount().multiply(avgPxBefore)
                            .subtract(tradeInfo.getTokenInAmount().multiply(mainPx))
                            .divide(nowAmount, RoundingMode.HALF_DOWN);
                }
                walletBalanceStatRepository.decreaseBalance(stat.getId(), tradeInfo.getTokenOutAmount(), tradeInfo.getTokenInAmount(), avgPx);
            }
        }
    }

    public WalletBalanceStat getBalanceStatFromCache(Long uid, UserWallet wallet, TokenBaseInfo tokenInfo, Boolean refresh) {
        return walletBalanceStatRepository.getOne(
                new LambdaQueryWrapper<WalletBalanceStat>().eq(WalletBalanceStat::getUid, uid)
                        .eq(WalletBalanceStat::getTokenId, tokenInfo.getId()).eq(WalletBalanceStat::getWalletId, wallet.getId())
        );
    }

    public WalletBalanceStat getBalanceStatAndSync(Long uid, UserWallet wallet, TokenBaseInfo tokenInfo, Boolean refresh) {
        if(!refresh) {
            WalletBalanceStat exist = tokenBalanceCacheService.getTokenBalance(uid, wallet.getId(), tokenInfo.getId());
            if(exist != null){
                return exist;
            }
        }
        WalletBalanceStat stat = walletBalanceStatRepository.getOne(
                new LambdaQueryWrapper<WalletBalanceStat>().eq(WalletBalanceStat::getUid, uid)
                        .eq(WalletBalanceStat::getTokenId, tokenInfo.getId()).eq(WalletBalanceStat::getWalletId, wallet.getId())
        );
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal val = null;
        if(stat == null) {
            if(tokenInfo.isMain()){
                SolBalance balance = solQueryService.solBalance(wallet.getAddress());
                amount = new BigDecimal(balance.getBalance()).movePointLeft(tokenInfo.getDecimals());
//                amount = tokenBalanceService.getMainBalance(uid, wallet.getAddress());
            } else {
                BigDecimal tokenBalance = solQueryService.tokenBalance(tokenInfo.getAddress(), wallet.getAddress());
                amount = tokenBalance.movePointLeft(tokenInfo.getDecimals());
//                amount = tokenBalanceService.getTokenBalance(uid, wallet, tokenInfo);
                if(amount.compareTo(BigDecimal.ZERO) == 0) {
                    val = BigDecimal.ZERO;
                }
            }
            stat = WalletBalanceStat.builder().id(IdUtil.nextId()).uid(uid).walletId(wallet.getId())
                    .amount(amount).val(BigDecimal.ZERO).updatedAt(TimestampUtil.now())
                    .tokenId(tokenInfo.getId()).build();
            walletBalanceStatRepository.save(stat);
        }
        else {
            if(tokenInfo.isMain()){
                SolBalance balance = solQueryService.solBalance(wallet.getAddress());
                amount = new BigDecimal(balance.getBalance()).movePointLeft(tokenInfo.getDecimals());
//                amount = tokenBalanceService.getMainBalance(uid, wallet.getAddress());
            } else {
                BigDecimal tokenBalance = solQueryService.tokenBalance(tokenInfo.getAddress(), wallet.getAddress());
                amount = tokenBalance.movePointLeft(tokenInfo.getDecimals());
//                amount = tokenBalanceService.getTokenBalance(uid, wallet, tokenInfo);
                if(amount.compareTo(BigDecimal.ZERO) == 0) {
                    val = BigDecimal.ZERO;
                }
            }
            stat.setAmount(amount);
            walletBalanceStatRepository.update(new LambdaUpdateWrapper<WalletBalanceStat>()
                    .set(WalletBalanceStat::getUpdatedAt, TimestampUtil.now())
                    .set(WalletBalanceStat::getAmount, amount)
                    // 如果是没有持仓，那么就重置val
                    .set(val != null, WalletBalanceStat::getVal, val)
                    .eq(WalletBalanceStat::getId, stat.getId()));
        }
        tokenBalanceCacheService.cacheTokenBalance(uid, wallet.getId(), tokenInfo.getId(), stat);
        return stat;
    }

    public WalletProfitStaticDTO countProfit(TokenBaseInfo baseInfo) {
        Long profitCount = walletBalanceStatRepository.count(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getTokenId, baseInfo.getId()).gt(WalletBalanceStat::getAmount, 0)
                .ne(WalletBalanceStat::getPx, BigDecimal.ZERO)
                .lt(WalletBalanceStat::getPx, baseInfo.getPrice()));
        Long loseCount = walletBalanceStatRepository.count(new LambdaQueryWrapper<WalletBalanceStat>()
                .eq(WalletBalanceStat::getTokenId, baseInfo.getId()).gt(WalletBalanceStat::getAmount, 0)
                .ne(WalletBalanceStat::getPx, BigDecimal.ZERO)
                .gt(WalletBalanceStat::getPx, baseInfo.getPrice()));

        return WalletProfitStaticDTO.builder()
                .profitCount(profitCount)
                .loseCount(loseCount).build();
    }

    @Resource
    private WalletBalanceStatRepository walletBalanceStatRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private TokenBalanceCacheService tokenBalanceCacheService;
    @Resource
    private TokenBalanceService tokenBalanceService;
}
