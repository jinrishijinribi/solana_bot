package com.rich.sol_bot.bot.service;

import com.rich.sol_bot.bot.BotManager;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.BotDealHandler;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyDealConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyInviteConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyWalletConstants;
import com.rich.sol_bot.bot.handler.enums.KeyBoardType;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.limit_order.mapper.LimitOrder;
import com.rich.sol_bot.limit_order.mapper.LimitOrderRepository;
import com.rich.sol_bot.iceberg.IcebergService;
import com.rich.sol_bot.iceberg.mapper.IcebergTask;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.UserRepository;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLog;
import com.rich.sol_bot.wallet.WalletBalanceStatService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BotNotifyService {

    @Autowired
    private LimitOrderRepository limitOrderRepository;
    @Autowired
    private I18nTranslator i18nTranslator;
    @Autowired
    private UserRepository userRepository;

    /**
     * 常规交易成功
     * @param tradeInfo
     */
    public void pushNormalTradeSuccess(TradeInfo tradeInfo) {
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        BigDecimal pxPer = tradeInfo.getTokenInAmount().divide(tradeInfo.getTokenOutAmount(), RoundingMode.HALF_DOWN);
        if(tradeInfo.getTokenOutId().equals(tokenMain.getId())){
            pxPer = tradeInfo.getTokenOutAmount().divide(tradeInfo.getTokenInAmount(), RoundingMode.HALF_DOWN);
        }
        I18nLanguageEnum language = userRepository.getLanguage(tradeInfo.getUid());
        TokenBaseInfo tokenIn = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
        TokenBaseInfo tokenOut = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
        UserWallet wallet = userWalletRepository.getById(tradeInfo.getWalletId());

        WalletBalanceStat tokenInStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenIn, true);
        WalletBalanceStat tokenOutStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenOut, true);
        this.successPanelAndPin(tradeInfo);

        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealSuccessNormalContent),
                "https://solscan.io/tx/" + tradeInfo.getTx(),
                NumberFormatTool.formatNumber(tradeInfo.getTokenOutAmount(), 4),
                tradeInfo.getTokenOut(),
                NumberFormatTool.formatNumber(tradeInfo.getTokenInAmount(), 4),
                tradeInfo.getTokenIn(),
                NumberFormatTool.formatNumber(pxPer.multiply(tradeInfo.getMainPx()), 4),
                NumberFormatTool.formatNumber(tokenInStat.getAmount(), 4),
                tradeInfo.getTokenIn(),
                NumberFormatTool.formatNumber(tokenOutStat.getAmount(), 4),
                tradeInfo.getTokenOut()
        );
        botManager.pushMsg(tradeInfo.getUid(), content);
    }

    /**
     * 限价单交易成功
     * @param tradeInfo
     * @param limitOrder
     */
    public void pushLimitTradeSuccess(TradeInfo tradeInfo, LimitOrder limitOrder) {
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        BigDecimal pxPer = tradeInfo.getTokenInAmount().divide(tradeInfo.getTokenOutAmount(), RoundingMode.HALF_DOWN);
        if(tradeInfo.getTokenOutId().equals(tokenMain.getId())){
            pxPer = tradeInfo.getTokenOutAmount().divide(tradeInfo.getTokenInAmount(), RoundingMode.HALF_DOWN);
        }
        TokenBaseInfo tokenIn = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
        TokenBaseInfo tokenOut = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
        UserWallet wallet = userWalletRepository.getById(tradeInfo.getWalletId());
        I18nLanguageEnum language = userRepository.getLanguage(tradeInfo.getUid());

        WalletBalanceStat tokenInStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenIn, true);
        WalletBalanceStat tokenOutStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenOut, true);
        this.successPanelAndPin(tradeInfo);

        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealSuccessLimitContent),
                limitOrder.getTokenName(),
                "https://solscan.io/tx/" + tradeInfo.getTx(),
                NumberFormatTool.formatNumber(tradeInfo.getTokenOutAmount(), 4),
                tradeInfo.getTokenOut(),
                NumberFormatTool.formatNumber(tradeInfo.getTokenInAmount(), 4),
                tradeInfo.getTokenIn(),
                NumberFormatTool.formatNumber(pxPer.multiply(tradeInfo.getMainPx()), 4),
                NumberFormatTool.formatNumber(tokenInStat.getAmount(), 4),
                tradeInfo.getTokenIn(),
                NumberFormatTool.formatNumber(tokenOutStat.getAmount(), 4),
                tradeInfo.getTokenOut()
        );
        botManager.pushMsg(tradeInfo.getUid(), content);
    }

    /**
     * 冰山委托交易成功
     * @param tradeInfo
     * @param task
     */
    public void pushIcePartSuccess(TradeInfo tradeInfo, IcebergTask task) {
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        BigDecimal pxPer = tradeInfo.getTokenInAmount().divide(tradeInfo.getTokenOutAmount(), RoundingMode.HALF_DOWN);
        if(tradeInfo.getTokenOutId().equals(tokenMain.getId())){
            pxPer = tradeInfo.getTokenOutAmount().divide(tradeInfo.getTokenInAmount(), RoundingMode.HALF_DOWN);
        }
        String icebergContent = String.format("冰山策略（%s/%s）", task.getSuccessCount() + task.getFailCount(), task.getAllCount());
        I18nLanguageEnum language = userRepository.getLanguage(task.getUid());

        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealSuccessIcePart),
                icebergContent,
                "https://solscan.io/tx/" + tradeInfo.getTx(),
                NumberFormatTool.formatNumber(tradeInfo.getTokenOutAmount(), 4),
                tradeInfo.getTokenOut(),
                NumberFormatTool.formatNumber(tradeInfo.getTokenInAmount(), 4),
                tradeInfo.getTokenIn(),
                NumberFormatTool.formatNumber(pxPer.multiply(tradeInfo.getMainPx()), 4)
        );
        botManager.pushMsg(tradeInfo.getUid(), content);
    }

    /**
     * 冰山委托全部成功
     * @param tradeInfo
     */
    public void pushIceAllSuccess(TradeInfo tradeInfo) {
        TokenBaseInfo tokenIn = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
        TokenBaseInfo tokenOut = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
        UserWallet wallet = userWalletRepository.getById(tradeInfo.getWalletId());

        WalletBalanceStat tokenInStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenIn, true);
        WalletBalanceStat tokenOutStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenOut, true);
        this.successPanelAndPin(tradeInfo);
        I18nLanguageEnum language = userRepository.getLanguage(tradeInfo.getUid());

        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.iceDealSuccessContentLast),
                NumberFormatTool.formatNumber(tokenInStat.getAmount(), 4),
                tradeInfo.getTokenIn(),
                NumberFormatTool.formatNumber(tokenOutStat.getAmount(), 4),
                tradeInfo.getTokenOut()
        );
        botManager.pushMsg(tradeInfo.getUid(), content);
    }


//    public void pushSuccessTrade(TradeInfo tradeInfo, IcebergTask task) {
//        TokenBaseInfo tokenMain = tokenInfoService.getMain();
//        BigDecimal pxPer = tradeInfo.getTokenInAmount().divide(tradeInfo.getTokenOutAmount(), RoundingMode.HALF_DOWN);
//        if(tradeInfo.getTokenOutId().equals(tokenMain.getId())){
//            pxPer = tradeInfo.getTokenOutAmount().divide(tradeInfo.getTokenInAmount(), RoundingMode.HALF_DOWN);
//        }
//        TokenBaseInfo tokenIn = tokenBaseInfoRepository.getById(tradeInfo.getTokenInId());
//        TokenBaseInfo tokenOut = tokenBaseInfoRepository.getById(tradeInfo.getTokenOutId());
//        UserWallet wallet = userWalletRepository.getById(tradeInfo.getWalletId());
//
//        WalletBalanceStat tokenInStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenIn, true);
//        WalletBalanceStat tokenOutStat = walletBalanceStatService.getBalanceStatAndSync(tradeInfo.getUid(), wallet, tokenOut, true);
//        String icebergContent = "";
//        String walletSuffix = "";
//        if(task != null) {
//            icebergContent = String.format("冰山策略（%s/%s）", task.getSuccessCount() + task.getFailCount(), task.getAllCount());
//            if(task.getSuccessCount() + task.getFailCount() == task.getAllCount()){
//                this.successPanelAndPin(tradeInfo);
//            }
//            walletSuffix = "(冰山策略完成后)";
//        } else {
//            this.successPanelAndPin(tradeInfo);
//        }
//
//        String content = String.format(BotReplyDealConstants.dealSuccessContent,
//                icebergContent,
//                "https://solscan.io/tx/" + tradeInfo.getTx(),
//                NumberFormatTool.formatNumber(tradeInfo.getTokenOutAmount(), 4),
//                tradeInfo.getTokenOut(),
//                NumberFormatTool.formatNumber(tradeInfo.getTokenInAmount(), 4),
//                tradeInfo.getTokenIn(),
//                NumberFormatTool.formatNumber(pxPer.multiply(tradeInfo.getMainPx()), 4),
//                walletSuffix,
//                NumberFormatTool.formatNumber(tokenInStat.getAmount(), 4),
//                tradeInfo.getTokenIn(),
//                NumberFormatTool.formatNumber(tokenOutStat.getAmount(), 4),
//                tradeInfo.getTokenOut()
//        );
//        botManager.pushMsg(tradeInfo.getUid(), content);
//    }


    // 确认成功的，重新推一个交易面板，染后置顶
    public void successPanelAndPin(TradeInfo tradeInfo) {
        TokenBaseInfo tokenBaseInfo = null;
        Long tokenId = null;
        if("sol".equalsIgnoreCase(tradeInfo.getTokenIn())){
            // 卖的交易
            return;
        } else {
            tokenId = tradeInfo.getTokenInId();
        }
        Long uid = tradeInfo.getUid();
        Long walletId = tradeInfo.getWalletId();
        botDealHandler.showKeyBoard(uid, null, tokenId, walletId, 1L, null, true, KeyBoardType.common);
    }

    /**
     * 交易失败面板
     * @param tradeInfo
     * @param errorInfo
     */
    public void pushFailTrade(TradeInfo tradeInfo, String errorInfo) {
        String icebergContent = "";
        if(tradeInfo.getIceId() != null) {
            IcebergTask task = icebergService.getById(tradeInfo.getIceId());
            icebergContent = String.format("冰山策略（%s/%s）", task.getSuccessCount() + task.getFailCount(), task.getAllCount());
        }
        LimitOrder order = limitOrderRepository.getById(tradeInfo.getId());
        String limitOrderPrefix = "";
        I18nLanguageEnum language = userRepository.getLanguage(tradeInfo.getUid());
        if(order != null) {
            limitOrderPrefix = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealFailLimitPrefix), order.getTokenName());
        }
        String content = limitOrderPrefix + String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealFailContentPrefix), icebergContent) + errorInfo;
        botManager.pushMsg(tradeInfo.getUid(), content);
    }

    /**
     * 提现成功
     * @param log
     */
    public void pushSuccessWithdraw(UserWithdrawLog log) {
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        UserWallet wallet = userWalletRepository.getByAddress(log.getFromAddress());
        I18nLanguageEnum language = userRepository.getLanguage(log.getUid());
        String content = null;
        switch (log.getType()){
            case rebate -> {
                content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.inviteTransferSuccessText),
                        "https://solscan.io/tx/" + log.getTxid(),
                        log.getAmount().stripTrailingZeros().toPlainString()
                );
            }
            case self_wallet -> {
                WalletBalanceStat stat = walletBalanceStatService.getBalanceStatAndSync(log.getUid(), wallet, mainToken, true);
                content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.walletTransferSuccessText),
                        "https://solscan.io/tx/" + log.getTxid(),
                        NumberFormatTool.formatNumber(log.getAmount(), 4),
                        NumberFormatTool.formatNumber(stat.getAmount(), 4)
                );
            }
        }
        botManager.pushMsg(log.getUid(), content);
    }

    /**
     * 提现失败
     * @param log
     */
    public void pushFailWithdraw(UserWithdrawLog log) {
        I18nLanguageEnum language = userRepository.getLanguage(log.getUid());
        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.walletTransferFailText)
        );
        botManager.pushMsg(log.getUid(), content);
    }

    @Resource
    @Lazy
    private BotManager botManager;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private WalletBalanceStatService walletBalanceStatService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    @Lazy
    private BotDealHandler botDealHandler;
    @Resource
    private IcebergService icebergService;
}
