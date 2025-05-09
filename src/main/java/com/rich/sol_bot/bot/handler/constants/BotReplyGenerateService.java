package com.rich.sol_bot.bot.handler.constants;

import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.pump.PumpInfoService;
import com.rich.sol_bot.pump.mapper.PumpPoolInfo;
import com.rich.sol_bot.sniper.mapper.SniperPlan;
import com.rich.sol_bot.sol.entity.TokenOverview;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenPoolInfo;
import com.rich.sol_bot.trade.mapper.TokenPoolInfoRepository;
import com.rich.sol_bot.trade.service.TokenBalanceService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.trade.service.TokenPxService;
import com.rich.sol_bot.trade.service.TradePoolService;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.UserRepository;
import com.rich.sol_bot.wallet.WalletBalanceStatService;
import com.rich.sol_bot.wallet.dto.WalletProfitStaticDTO;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Service
public class BotReplyGenerateService {

    @Autowired
    private TokenPxService tokenPxService;
    @Autowired
    private TokenPoolInfoRepository tokenPoolInfoRepository;
    @Autowired
    private I18nTranslator i18nTranslator;
    @Autowired
    private UserRepository userRepository;

    /**
     * 生成代币信息的头部
     * @param tokenBaseInfo
     * @return
     */
    public String generateTokenStaticHead(TokenBaseInfo tokenBaseInfo, I18nLanguageEnum language) {
        TokenPoolInfo tokenPoolInfo = tradePoolService.getPoolInfo(tokenBaseInfo.getAddress());
        PumpPoolInfo pumpPoolInfo = null;
        if(tokenPoolInfo == null) {
            pumpPoolInfo = pumpInfoService.getPumpInfo(tokenBaseInfo.getAddress());
        }
        String countDown = "未知时间";
        String poolSource = "RAY";
        String bondingCurve = "";
        if(pumpPoolInfo == null || pumpPoolInfo.getComplete() == 1) {
            if(tokenPoolInfo == null) {
                tokenPoolInfo = tradePoolService.getPoolInfo(tokenBaseInfo.getAddress());
            }
            countDown = "";
            if(tokenPoolInfo != null) {
                // 如果是立即开始，则开始时间是池子创建时间
                Long openTime = tokenPoolInfo.getPoolOpenTime() == 0 ? tokenPoolInfo.getPoolCreateTime() : tokenPoolInfo.getPoolOpenTime();
                countDown = generateCountdown(openTime, language);
            }
        } else {
            poolSource = "PUMP";
            countDown = generateCountdown(pumpPoolInfo.getCreatedAt() / 1000, language);
            BigDecimal bondingCurvePer = tokenPxService.getPumpPercent(tokenBaseInfo.getAddress(), pumpPoolInfo.getBondingCurve());
            bondingCurve = "(" + bondingCurvePer.movePointRight(2).setScale(2, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString() + "%)";
        }
        return String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.tokenStaticContentPart1Sniper),
                NumberFormatTool.toShort(tokenBaseInfo.getMkValue(), 4),
                NumberFormatTool.formatNumber(tokenBaseInfo.getPrice(), 4),
                tokenBaseInfo.showTop10Percent(),
                NumberFormatTool.formatNumber(tokenBaseInfo.getLiquidity(), 4),
                poolSource,
                bondingCurve,
                countDown,
                tokenBaseInfo.showMint(), tokenBaseInfo.showDropPermission(),
                tokenBaseInfo.showLiquidityLock()
                );
    }

    /**
     * 生成代币信息的链接
     * @param tokenBaseInfo
     * @return
     */
    public String generateTokenStaticSocial(TokenBaseInfo tokenBaseInfo, I18nLanguageEnum language) {
        return String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.tokenStaticContentPart3),
                tokenBaseInfo.getTwitterUrl(),
                tokenBaseInfo.getTgUrl(),
                "https://dexscreener.com/solana/" + tokenBaseInfo.getAddress(),
                "https://birdeye.so/token/"+ tokenBaseInfo.getAddress() +"?chain=solana",
                "https://www.dextools.io/app/cn/solana/pair-explorer/" + tokenBaseInfo.getAddress(),
                "https://pump.fun/" + tokenBaseInfo.getAddress(),
                "https://gmgn.ai/sol/token/" + tokenBaseInfo.getAddress()
                );
    }

    public String generateTokenGroup(TokenBaseInfo tokenBaseInfo) {
        WalletProfitStaticDTO dto = walletBalanceStatService.countProfit(tokenBaseInfo);
        return String.format(BotReplyWalletConstants.tokenStaticContentPart2,
                tokenBaseInfo.getOnbusCount(),
                dto.getProfitCount(), dto.getLoseCount()
        );
    }

    /**
     * 生成代币信息的狙击任务内容
     * @param sniperPlan
     * @param userWallet
     * @return
     */
    public String generateWalletStaticSniper(SniperPlan sniperPlan, UserWallet userWallet) {
        if(userWallet == null) return "";
        I18nLanguageEnum language = userRepository.getLanguage(userWallet.getUid());
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        WalletBalanceStat stat = walletBalanceStatService.getBalanceStatAndSync(sniperPlan.getUid(), userWallet, mainToken, true);
        return String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.tokenWalletContentPart4Sniper),
                NumberFormatTool.formatNumber(stat.getAmount(), 4),
                NumberFormatTool.formatNumber(sniperPlan.getMainAmount(), 4),
                NumberFormatTool.formatNumber(sniperPlan.getExtraGas(), 4)
                );
    }

    /**
     * 生成代币信息的钱包持有相关
     * @param uid
     * @param tokenBaseInfo
     * @param wallet
     * @return
     */
    public String generateWalletStatic2(Long uid, TokenBaseInfo tokenBaseInfo, UserWallet wallet) {
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        BigDecimal mainPx = new BigDecimal(mainToken.getPrice());
        I18nLanguageEnum language = userRepository.getLanguage(wallet.getUid());
        WalletBalanceStat tokenStat = walletBalanceStatService.getBalanceStatAndSync(uid, wallet, tokenBaseInfo, true);
        BigDecimal tokenPx = BigDecimal.ZERO;
        if(tokenBaseInfo.getPrice() != null){
            tokenPx = new BigDecimal(tokenBaseInfo.getPrice());
        }
        BigDecimal solValue = tokenStat.solValue(tokenPx, mainPx);
        BigDecimal upRate = BigDecimal.ZERO;
        if(tokenStat.getVal().compareTo(BigDecimal.ZERO) > 0) {
            upRate = solValue.subtract(tokenStat.getVal()).divide(tokenStat.getVal(), RoundingMode.HALF_DOWN).setScale(6, RoundingMode.HALF_DOWN);
        }
        BigDecimal mainAmount = tokenBalanceService.getTokenBalance(uid, wallet, mainToken);
        BigDecimal tokenAvgPx = BigDecimal.ZERO;
        if(tokenStat.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            tokenAvgPx = tokenStat.getVal().multiply(mainPx).divide(tokenStat.getAmount(), RoundingMode.HALF_DOWN);
        }
        if(tokenStat.getPx() != null) {
            tokenAvgPx = tokenStat.getPx();
        }
        return String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.tokenWalletContentPart4Deal),
                NumberFormatTool.formatNumber(mainAmount, 4),
                NumberFormatTool.formatNumber(tokenAvgPx, 4),
                NumberFormatTool.formatNumber(solValue, 4),
                NumberFormatTool.formatNumber(upRate.movePointRight(2), 2) + "%"
        );
    }

    /**
     * 计算倒计时
     * @param time
     * @return
     */
    private String generateCountdown(Long time, I18nLanguageEnum language) {
        Long now = TimestampUtil.now().getTime() / 1000;
        long day = 0L;
        long hour = 0L;
        long minute = 0L;
        if(time > now){
            long diffInSeconds = time - now;
            day = TimeUnit.SECONDS.toDays(diffInSeconds);
            hour = TimeUnit.SECONDS.toHours(diffInSeconds) % 24;
            minute = TimeUnit.SECONDS.toMinutes(diffInSeconds) % 60;
            return String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealTokenInfoTimeAfter), day, hour, minute);
        } else {
            long diffInSeconds = now - time;
            day = TimeUnit.SECONDS.toDays(diffInSeconds);
            hour = TimeUnit.SECONDS.toHours(diffInSeconds) % 24;
            minute = TimeUnit.SECONDS.toMinutes(diffInSeconds) % 60;
            return String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealTokenInfoTimeBefore), day, hour, minute);
        }
    }


    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private WalletBalanceStatService walletBalanceStatService;
    @Resource
    private TokenBalanceService tokenBalanceService;
    @Resource
    private TradePoolService tradePoolService;
    @Resource
    private PumpInfoService pumpInfoService;

}
