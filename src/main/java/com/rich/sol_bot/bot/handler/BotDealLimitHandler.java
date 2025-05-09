package com.rich.sol_bot.bot.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyDealConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyGenerateService;
import com.rich.sol_bot.bot.handler.constants.BotReplyStartConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.limit_order.enums.OrderStatEnum;
import com.rich.sol_bot.limit_order.service.LimitOrderQueueService;
import com.rich.sol_bot.trade.enums.TradeSideEnum;
import com.rich.sol_bot.limit_order.mapper.LimitOrder;
import com.rich.sol_bot.limit_order.mapper.LimitOrderRepository;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.user.action.mapper.UserActionRepository;
import com.rich.sol_bot.user.config.mapper.ConfigAutoSell;
import com.rich.sol_bot.user.config.mapper.ConfigAutoSellRepository;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.WalletBalanceStatService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BotDealLimitHandler {

    @Autowired
    private BotStateService botStateService;
    @Autowired
    private TokenInfoService tokenInfoService;
    @Autowired
    private UserConfigRepository userConfigRepository;
    @Autowired
    private WalletBalanceStatService walletBalanceStatService;
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private UserWalletRepository userWalletRepository;
    @Autowired
    private UserActionRepository userActionRepository;
    @Autowired
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Autowired
    private ConfigAutoSellRepository configAutoSellRepository;
    @Autowired
    private I18nTranslator i18nTranslator;

    /**
     * 当前持仓的限价单keyboard
     * @param bot
     * @param uid
     * @param messageId
     * @param limitOid
     */
    public void limitOrderKeyBoardFromOne(TokenBot bot, Long uid, Integer messageId, Long limitOid) {
        LimitOrder limitOrder = limitOrderRepository.getById(limitOid);
        TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getById(limitOrder.getTokenId());
        String content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitKeyboardHead),
                tokenBaseInfo.getSymbol(),
                tokenBaseInfo.getAddress()
        );

        content += botReplyGenerateService.generateTokenStaticHead(tokenBaseInfo, bot.getLanguage(uid));
        content += botReplyGenerateService.generateTokenStaticSocial(tokenBaseInfo, bot.getLanguage(uid));
        content += String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderKeyBoardFoot),
                limitOrder.getSide().getZhValue(),
                NumberFormatTool.formatNumber(limitOrder.getPx(), 4),
                NumberFormatTool.formatNumber(limitOrder.getAmount(), 4),
                limitOrder.getSide().equals(TradeSideEnum.buy) ? "SOL" : tokenBaseInfo.getSymbol(),
                NumberFormatTool.formatNumber(limitOrder.getExtraGas(), 4),
                generateCountdown(limitOrder.getExpiredAt().getTime() / 1000)
        );
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(
                        i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderRefreshText)
                        )
                        .callbackData(BotReplyDealConstants.dealLimitOrderRefreshFromOneCli + limitOid
                        ).build()));
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(
                                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderDeleteText)
                        )
                        .callbackData(BotReplyDealConstants.dealLimitOrderDeleteOneCli + limitOid
                        ).build()));
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(
                                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderReturnAllListText)
                        )
                        .callbackData(BotReplyDealConstants.dealLimitOrderReturnOneCli
                        ).build()));
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId).inlineKeyboardMarkup(markup)
                .text(content)
                .build());
    }

    /**
     * 限价单keyboard
     * @param bot
     * @param uid
     * @param messageId
     * @param limitOid
     */
    public void limitOrderKeyBoard(TokenBot bot, Long uid, Integer messageId, Long limitOid) {
        LimitOrder limitOrder = limitOrderRepository.getById(limitOid);
        TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getById(limitOrder.getTokenId());
        String content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitKeyboardHead),
                tokenBaseInfo.getSymbol(),
                tokenBaseInfo.getAddress()
        );

        content += botReplyGenerateService.generateTokenStaticHead(tokenBaseInfo, bot.getLanguage(uid));
        content += botReplyGenerateService.generateTokenStaticSocial(tokenBaseInfo, bot.getLanguage(uid));
        content += String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderKeyBoardFoot),
                limitOrder.getSide().getZhValue(),
                NumberFormatTool.formatNumber(limitOrder.getPx(), 4),
                NumberFormatTool.formatNumber(limitOrder.getAmount(), 4),
                limitOrder.getSide().equals(TradeSideEnum.buy) ? "SOL" : tokenBaseInfo.getSymbol(),
                NumberFormatTool.formatNumber(limitOrder.getExtraGas(), 4),
                generateCountdown(limitOrder.getExpiredAt().getTime() / 1000)
        );
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(
                                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderRefreshText)
                        )
                        .callbackData(BotReplyDealConstants.dealLimitOrderRefreshCli + limitOid
                        ).build()));
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(BotReplyDealConstants.dealLimitOrderDeleteText)
                        .callbackData(BotReplyDealConstants.dealLimitOrderDeleteCli + limitOid
                        ).build()));
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(
                                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderReturnAllListText)
                        )
                        .callbackData(BotReplyDealConstants.dealLimitOrderReturnAllListCli
                        ).build()));
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId).inlineKeyboardMarkup(markup)
                .text(content)
                .build());
    }

    public void startPxBuy(TokenBot bot, Long uid, Integer messageId) {
        userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
        String address = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(address);
        botStateService.lockState(uid, LockStateEnum.deal_to_limit_px_buy);
        String content = String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitPxBuyContent),
                NumberFormatTool.formatNumber(baseInfo.getPrice(), 4));
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(content)
                .build());
    }

    public void startPxSell(TokenBot bot, Long uid, Integer messageId) {
        userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
        String address = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(address);
        botStateService.lockState(uid, LockStateEnum.deal_to_limit_px_sell);
        String content = String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitPxSellContent),
                NumberFormatTool.formatNumber(baseInfo.getPrice(), 4));
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(content)
                .build());
    }

    public void startRateBuy(TokenBot bot, Long uid, Integer messageId) {
        userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
        String address = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(address);
        botStateService.lockState(uid, LockStateEnum.deal_to_limit_rate_buy);
        String content = String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitRateBuyContent),
                NumberFormatTool.formatNumber(baseInfo.getPrice(),4));
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(content)
                .build());
    }

    public void startRateSell(TokenBot bot, Long uid, Integer messageId) {
        userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
        String address = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(address);
        botStateService.lockState(uid, LockStateEnum.deal_to_limit_rate_sell);
        String content = String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitRateSellContent),
                NumberFormatTool.formatNumber(baseInfo.getPrice(), 4));
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(content)
                .build());
    }

    /**
     * 根据买入，自动创建卖出订单
     * @param info
     */
    public void tryGenerateSellLimit(TradeInfo info) {
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        if(info.getTokenInId().equals(tokenMain.getId())) return;
        UserConfig userConfig = userConfigRepository.getById(info.getUid());
        if(userConfig.getAutoSell() == 0) return;
        List<ConfigAutoSell> autoSells = configAutoSellRepository.listExist(info.getUid());
        if(autoSells.isEmpty()) return;
        BigDecimal px = info.getMainPx().multiply(info.getTokenOutAmount()).divide(info.getTokenInAmount(), RoundingMode.HALF_DOWN);
        for(ConfigAutoSell autoSell: autoSells) {
            BigDecimal sellPx = px.multiply(BigDecimal.ONE.add(autoSell.getPxRate()));
            BigDecimal amount = info.getTokenInAmount().multiply(autoSell.getTokenPercent());
            LimitOrder order = LimitOrder.builder()
                    .id(IdUtil.nextId()).walletId(info.getWalletId())
                    .uid(info.getUid()).tokenId(info.getTokenInId()).tokenName(info.getTokenIn())
                    .side(TradeSideEnum.sell).amount(amount).nowPx(px)
                    .px(sellPx).mode(info.getDedicated() == 1 ? SniperMode.protect_mode : SniperMode.fast_mode)
                    .slippage(info.getSlippage()).extraGas(info.getExtraGas())
                    .state(OrderStatEnum.created).createdAt(TimestampUtil.now()).expiredAt(TimestampUtil.plus(3, TimeUnit.DAYS))
                    .build();
            limitOrderRepository.save(order);
            limitOrderQueueService.submitToQueue(order);
        }
    }


    public void tradeLimitPx(TokenBot bot, Long uid, Integer tradeMessageId, TradeSideEnum side, BigDecimal px, BigDecimal amountOrRate) {
        String tokenAddress = userActionService.getValue(uid, tradeMessageId, ActionValEnum.deal_token_address);
        TokenBaseInfo tokenInfo = tokenInfoService.geneTokenBaseInfo(tokenAddress);
        Long walletId = Long.valueOf(userActionService.getValue(uid, tradeMessageId, ActionValEnum.deal_wallet_id));
        UserWallet wallet = userWalletRepository.getById(walletId);
        String modeString = userActionService.getValue(uid, tradeMessageId, ActionValEnum.deal_mode);
        UserConfig userConfig = userConfigRepository.getById(uid);
        BigDecimal slippage = SniperMode.fast_mode.getValue().equals(modeString) ? userConfig.getFastSlippage() : userConfig.getProtectSlippage();
        BigDecimal extraGas = TradeSideEnum.buy.equals(side) ? userConfig.getBuyFee() : userConfig.getSellFee();
        BigDecimal amount = amountOrRate;
        if(side.equals(TradeSideEnum.sell)){
            WalletBalanceStat stat = walletBalanceStatService.getBalanceStatAndSync(uid, wallet, tokenInfo, true);
            amount = stat.getAmount().multiply(amountOrRate);
        }
        LimitOrder order = LimitOrder.builder()
                .id(IdUtil.nextId()).walletId(walletId)
                .uid(uid).tokenId(tokenInfo.getId()).tokenName(tokenInfo.getSymbol())
                .side(side).amount(amount).nowPx(new BigDecimal(tokenInfo.getPrice()))
                .px(px).mode(SniperMode.valueOf(modeString)).slippage(slippage).extraGas(extraGas)
                .state(OrderStatEnum.created).createdAt(TimestampUtil.now()).expiredAt(TimestampUtil.plus(3, TimeUnit.DAYS))
                .build();
        limitOrderRepository.save(order);
        limitOrderQueueService.submitToQueue(order);
        this.tradeCreatedSuccess(bot, uid, order);
    }

    public void tradeLimitRate(TokenBot bot, Long uid, Integer tradeMessageId, TradeSideEnum side, BigDecimal pxRate, BigDecimal amountOrRate) {
        String tokenAddress = userActionService.getValue(uid, tradeMessageId, ActionValEnum.deal_token_address);
        TokenBaseInfo tokenInfo = tokenInfoService.geneTokenBaseInfo(tokenAddress);
        Long walletId = Long.valueOf(userActionService.getValue(uid, tradeMessageId, ActionValEnum.deal_wallet_id));
        UserWallet wallet = userWalletRepository.getById(walletId);
        BigDecimal px = new BigDecimal(tokenInfo.getPrice()).multiply(BigDecimal.ONE.add(pxRate));
        BigDecimal amount = amountOrRate;
        if(side.equals(TradeSideEnum.sell)){
            WalletBalanceStat stat = walletBalanceStatService.getBalanceStatAndSync(uid, wallet, tokenInfo, true);
            amount = stat.getAmount().multiply(amountOrRate);
        }
        String modeString = userActionService.getValue(uid, tradeMessageId, ActionValEnum.deal_mode);
        UserConfig userConfig = userConfigRepository.getById(uid);
        BigDecimal slippage = SniperMode.fast_mode.getValue().equals(modeString) ? userConfig.getFastSlippage() : userConfig.getProtectSlippage();
        BigDecimal extraGas = TradeSideEnum.buy.equals(side) ? userConfig.getBuyFee() : userConfig.getSellFee();
        LimitOrder order = LimitOrder.builder()
                .id(IdUtil.nextId()).walletId(walletId)
                .uid(uid).tokenId(tokenInfo.getId()).tokenName(tokenInfo.getSymbol())
                .side(side).amount(amount).nowPx(new BigDecimal(tokenInfo.getPrice()))
                .px(px).mode(SniperMode.valueOf(modeString)).slippage(slippage).extraGas(extraGas)
                .state(OrderStatEnum.created).createdAt(TimestampUtil.now()).expiredAt(TimestampUtil.plus(3, TimeUnit.DAYS))
                .build();
        limitOrderRepository.save(order);
        limitOrderQueueService.submitToQueue(order);
        this.tradeCreatedSuccess(bot, uid, order);
    }

    public void cancelAllOrder(TokenBot bot, Long uid, Integer messageId) {
        limitOrderRepository.cancelAll(uid);
    }

    public void cancelOrderById(TokenBot bot, Long uid, Integer messageId, Long oid) {
        limitOrderRepository.cancelOne(oid);
    }

    /**
     * 列出所有限价单
     * @param bot
     * @param uid
     * @param messageId
     */
    public void listAllLimitOrders(TokenBot bot, Long uid, Integer messageId) {
        List<LimitOrder> list = limitOrderRepository.list(new LambdaQueryWrapper<LimitOrder>()
                .eq(LimitOrder::getUid, uid).eq(LimitOrder::getState, OrderStatEnum.created)
        );
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        String content = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealOwnedLimitOrderContent);
        for(int i = 0; i < list.size(); i++) {
            LimitOrder o = list.get(i);
            keyboard.add(Collections.singletonList(
                    InlineKeyboardButton.builder().text(String.format("挂单%s-%s", i + 1, o.getTokenName()))
                    .callbackData(BotReplyDealConstants.dealOwnedChooseLimitOrderPrefix + o.getId()
                    ).build()));
        }
        if(list.isEmpty()) {
            content = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealOwnedLimitOrderNoContent);
        } else {
            keyboard.add(Collections.singletonList(
                    InlineKeyboardButton.builder().text(
                            i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealOwnedCloseAllOrderText))
                            .callbackData(BotReplyDealConstants.dealOwnedCloseAllOrderCli
            ).build()));
        }
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                        .callbackData(BotReplyStartConstants.returnToInitStartCli
                        ).build()));
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId).inlineKeyboardMarkup(markup)
                .text(content)
                .build());
    }

    /**
     * 列出某个代币的限价单
     * @param bot
     * @param uid
     * @param messageId
     */
    public void listLimitOrdersByToken(TokenBot bot, Long uid, Integer messageId) {
        String tokenAddress = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        TokenBaseInfo baseInfo = tokenBaseInfoRepository.getByAddress(tokenAddress);
        List<LimitOrder> list = limitOrderRepository.list(new LambdaQueryWrapper<LimitOrder>()
                .eq(LimitOrder::getTokenId, baseInfo.getId())
                .eq(LimitOrder::getUid, uid).eq(LimitOrder::getState, OrderStatEnum.created)
        );

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        StringBuilder content = new StringBuilder(baseInfo.getSymbol() + "进行中的挂单：\n");
        content.append("代币名称      价格      数量      方向      剩余时间\n\n");
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            LimitOrder o = list.get(i);
            content.append(String.format("%s.%s    %s    %s%s    %s    %s \n\n", i + 1, o.getTokenName(), NumberFormatTool.formatNumber(o.getPx(), 4),
                    NumberFormatTool.formatNumber(o.getAmount(), 4), o.getSide().equals(TradeSideEnum.buy) ? "SOL" : o.getTokenName(),
                    o.getSide().getZhValue(), generateCountdown(o.getExpiredAt().getTime() / 1000)
            ));
            buttons.add(InlineKeyboardButton.builder().text(String.valueOf(i + 1))
                            .callbackData(BotReplyDealConstants.dealLimitOrderDetailFromOneCli + o.getId()
                            ).build());
            if(buttons.size() == 3) {
                keyboard.add(buttons);
                keyboard = new ArrayList<>();
            }
        }
        if(list.isEmpty()){
            content = new StringBuilder("暂无已配置挂单");
        }
        if(!buttons.isEmpty()) {
            keyboard.add(buttons);
        }
        keyboard.add(Collections.singletonList(InlineKeyboardButton.builder().text(
                        i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderCancelAllByTokenText)
                )
                .callbackData(BotReplyDealConstants.dealLimitOrderCancelAllByTokenCli
                ).build()));
        keyboard.add(Collections.singletonList(InlineKeyboardButton.builder().text(
                        i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitOrderReturnAllListText)
                )
                .callbackData(BotReplyDealConstants.dealLimitOrderReturnOneCli
                ).build()));
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId).inlineKeyboardMarkup(markup)
                .text(content.toString())
                .build());
    }


    public void cancelLimitOrdersByToken(TokenBot bot, Long uid, Integer messageId) {
        String tokenAddress = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        TokenBaseInfo baseInfo = tokenBaseInfoRepository.getByAddress(tokenAddress);
        limitOrderRepository.cancelAll(uid, baseInfo.getId());
    }

    public void tradeCreatedSuccess(TokenBot bot, Long uid, LimitOrder limitOrder) {
        String content = String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealLimitCreateSuccess),
                limitOrder.getTokenName(),
                limitOrder.getSide().getZhValue(),
                NumberFormatTool.formatNumber(limitOrder.getNowPx(),4),
                NumberFormatTool.formatNumber(limitOrder.getPx(),4),
                limitOrder.getAmount(),
                limitOrder.getSide().equals(TradeSideEnum.buy) ? "SOL" : limitOrder.getTokenName()
        );
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(content)
                .build());
    }

    /**
     * 秒时间戳
     * @param time
     * @return
     */
    private String generateCountdown(Long time) {
        Long now = TimestampUtil.now().getTime() / 1000;
        long day = 0L;
        long hour = 0L;
        long minute = 0L;
        if(time > now){
            long diffInSeconds = time - now;
            day = TimeUnit.SECONDS.toDays(diffInSeconds);
            hour = TimeUnit.SECONDS.toHours(diffInSeconds) % 24;
            minute = TimeUnit.SECONDS.toMinutes(diffInSeconds) % 60;
            return String.format("%sd%sh%sm", day, hour, minute);
        } else {
            return "已过期";
        }
    }

    @Resource
    private UserActionService userActionService;
    @Resource
    private LimitOrderRepository limitOrderRepository;
    @Resource
    private LimitOrderQueueService limitOrderQueueService;
    @Resource
    private BotReplyGenerateService botReplyGenerateService;

}
