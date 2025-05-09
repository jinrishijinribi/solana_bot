package com.rich.sol_bot.bot.route;

import ch.qos.logback.core.pattern.color.BoldGreenCompositeConverter;
import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.*;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyDealConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyPositionConstants;
import com.rich.sol_bot.bot.handler.enums.KeyBoardType;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.trade.enums.TradeSideEnum;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

@Service
@Slf4j
public class SolBotDealRoute {
    @Autowired
    private BotDealLimitHandler botDealLimitHandler;
    @Autowired
    private I18nTranslator i18nTranslator;

    public void handlerCallbackQuery(TokenBot bot, Update update) {
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        // 切换钱包
        if(content.startsWith(BotReplyDealConstants.dealWalletPrefix)){
            String walletId = content.replace(BotReplyDealConstants.dealWalletPrefix, "");
            userActionService.setValue(uid, messageId, ActionValEnum.deal_wallet_id, walletId);
            botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.common);
            return;
        }
        // 切换交易对
        if(content.startsWith(BotReplyDealConstants.dealTokenPrefix)) {
            String tokenId = content.replace(BotReplyDealConstants.dealTokenPrefix, "");
            TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getById(tokenId);
            userActionService.setValue(uid, messageId, ActionValEnum.deal_token_address, tokenBaseInfo.getAddress());
            botDealHandler.showKeyBoard(uid, messageId, tokenBaseInfo.getId(), null, null, null, null, KeyBoardType.common);
            return;
        }
        // 买卖
        if(content.startsWith(BotReplyDealConstants.dealPrefix)){
            String sideAndAmount = content.replace(BotReplyDealConstants.dealPrefix, "");
            if(sideAndAmount.startsWith(BotReplyDealConstants.buyPrefix)){
                String amount = sideAndAmount.replace(BotReplyDealConstants.buyPrefix, "");
                if(BotReplyDealConstants.unknownAmount.equals(amount)){
                    userActionService.setValue(uid, ActionValEnum.deal_side, TradeSideEnum.buy.getValue());
                    userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(
                                    i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealImportAmountBuy)
                            )
                            .build());
                    botStateService.lockState(uid, LockStateEnum.deal_to_choose_amount);
                } else {
                    botDealHandler.dealImportAmount(bot, uid, messageId, new BigDecimal(amount), TradeSideEnum.buy, false);
                }
            }
            if(sideAndAmount.startsWith(BotReplyDealConstants.sellPrefix)){
                String amount = sideAndAmount.replace(BotReplyDealConstants.sellPrefix, "");
                if(BotReplyDealConstants.unknownAmount.equals(amount)){
                    userActionService.setValue(uid, ActionValEnum.deal_side, TradeSideEnum.sell.getValue());
                    userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(
                                    i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealImportAmountSell)
                            )
                            .build());
                    botStateService.lockState(uid, LockStateEnum.deal_to_choose_amount);
                } else {
                    botDealHandler.dealImportAmount(bot, uid, messageId, new BigDecimal(amount), TradeSideEnum.sell, false);
                }
            }
        }

        if(content.startsWith(BotReplyDealConstants.dealConfirmSuccessCli)) {
            content = content.replace(BotReplyDealConstants.dealConfirmSuccessCli, "");
            String[] values = content.split(":");
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.delete_message)
                    .chatId(uid).messageId(messageId)
                    .build());
            if(values.length == 3) {
                Long lastMessageId = Long.valueOf(values[0]);
                String side = values[2];
                BigDecimal amount = new BigDecimal(values[1]);
                botDealHandler.dealImportAmount(bot, uid, lastMessageId.intValue(), amount, TradeSideEnum.valueOf(side), true);
            }
        }
        if(content.startsWith(BotReplyDealConstants.dealConfirmCancelCli)){
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.delete_message)
                    .chatId(uid).messageId(messageId)
                    .build());
        }
        // 限价单-所有订单-查看限价单详情刷新
        if(content.startsWith(BotReplyDealConstants.dealLimitOrderRefreshCli)){
            botDealLimitHandler.limitOrderKeyBoard(bot, uid, messageId, Long.valueOf(content.replace(BotReplyDealConstants.dealLimitOrderRefreshCli, "")));
            return;
        }
        // 限价单-所有订单-查看限价单详情删除
        if(content.startsWith(BotReplyDealConstants.dealLimitOrderDeleteCli)){
            botDealLimitHandler.cancelOrderById(bot, uid, messageId, Long.valueOf(content.replace(BotReplyDealConstants.dealLimitOrderDeleteCli, "")));
            botDealLimitHandler.listAllLimitOrders(bot, uid, messageId);
            return;
        }
        // 限价单-所有订单-订单详情
        if(content.startsWith(BotReplyDealConstants.dealOwnedChooseLimitOrderPrefix)){
            botDealLimitHandler.limitOrderKeyBoard(bot, uid, messageId, Long.valueOf(content.replace(BotReplyDealConstants.dealOwnedChooseLimitOrderPrefix, "")));
        }
        // 限价单-此币种订单-订单详情
        if(content.startsWith(BotReplyDealConstants.dealLimitOrderDetailFromOneCli)) {
            botDealLimitHandler.limitOrderKeyBoardFromOne(bot, uid, messageId, Long.valueOf(content.replace(BotReplyDealConstants.dealLimitOrderDetailFromOneCli, "")));
        }
        // 限价单-此币种订单-订单详情-刷新
        if(content.startsWith(BotReplyDealConstants.dealLimitOrderRefreshFromOneCli)) {
            botDealLimitHandler.limitOrderKeyBoardFromOne(bot, uid, messageId, Long.valueOf(content.replace(BotReplyDealConstants.dealLimitOrderRefreshFromOneCli, "")));
        }
        // 限价单-此币种订单-订单详情-删除
        if(content.startsWith(BotReplyDealConstants.dealLimitOrderDeleteOneCli)){
            botDealLimitHandler.cancelOrderById(bot, uid, messageId, Long.valueOf(content.replace(BotReplyDealConstants.dealLimitOrderDeleteOneCli, "")));
            botDealLimitHandler.limitOrderKeyBoardFromOne(bot, uid, messageId, Long.valueOf(content.replace(BotReplyDealConstants.dealLimitOrderDeleteOneCli, "")));
        }
        // pump发射一键卖出(快速模式)
        if(content.startsWith(BotReplyDealConstants.dealPumpLaunchFastCli)){
            String id = content.replace(BotReplyDealConstants.dealPumpLaunchFastCli, "");
            botDealHandler.dealPumpLaunch(bot, uid, Long.valueOf(id), SniperMode.fast_mode);
        }
        // pump发射一键卖出(防夹模式)
        if(content.startsWith(BotReplyDealConstants.dealPumpLaunchProtectCli)){
            String id = content.replace(BotReplyDealConstants.dealPumpLaunchProtectCli, "");
            botDealHandler.dealPumpLaunch(bot, uid, Long.valueOf(id), SniperMode.protect_mode);
        }

        switch (content) {
            // 买卖页刷新
            case BotReplyDealConstants.dealRefreshCli, BotReplyDealConstants.dealToIcebergReturnCli -> {
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.common);
            }
            // pin的买卖页面刷新
            case BotReplyDealConstants.dealPinRefreshCli -> {
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.common);
            }
            // 买卖页面切换模式-快速模式
            case BotReplyDealConstants.dealFastModeCli -> {
                userActionService.setValue(uid, messageId, ActionValEnum.deal_mode, SniperMode.fast_mode.getValue());
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.common);
            }
            // 买卖页面切换模式-保护模式
            case BotReplyDealConstants.dealProtectModeCli -> {
                userActionService.setValue(uid, messageId, ActionValEnum.deal_mode, SniperMode.protect_mode.getValue());
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.common);
            }
            // 进入限价单页面
            case BotReplyDealConstants.dealLimitCli -> {
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.limit);
            }
            // 生成收益图
            case BotReplyDealConstants.dealToShowPLCli -> {
                String walletId = userActionService.getValue(uid, ActionValEnum.deal_wallet_id);
                String tokenAddress = null;
                if(messageId != null) {
                    tokenAddress = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
                }
                if(tokenAddress == null) {
                    tokenAddress = userActionService.getValue(uid, ActionValEnum.deal_token_address);
                }
                TokenBaseInfo tokenBaseInfo = tokenInfoService.geneTokenBaseInfo(tokenAddress);
                solQueryService.generatePageView(uid, tokenBaseInfo.getId(), Long.valueOf(walletId));
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(uid).messageId(null)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionMapIsShowing))
                        .build());
            }
            // 冰山策略
            case BotReplyDealConstants.dealToIcebergCli -> {
                botDealHandler.showKeyBoard(uid, messageId, null, null, null,null, null, KeyBoardType.ice);
            }
            // 冰山策略买
            case BotReplyDealConstants.dealToIcebergBuyCli -> {
                userActionService.setValue(uid, ActionValEnum.deal_side, TradeSideEnum.buy.getValue());
                userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(uid).messageId(null)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealToIcebergBuyLongText))
                        .build());
                botStateService.lockState(uid, LockStateEnum.deal_to_iceberg_amount);
            }
            // 冰山策略卖
            case BotReplyDealConstants.dealToIcebergSellCli -> {
                userActionService.setValue(uid, ActionValEnum.deal_side, TradeSideEnum.sell.getValue());
                userActionService.setValue(uid, ActionValEnum.deal_message_id, messageId.toString());
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(uid).messageId(null)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealToIcebergSellLongText))
                        .build());
                botStateService.lockState(uid, LockStateEnum.deal_to_iceberg_amount);
            }
            // 上下token 翻页
            case BotReplyDealConstants.dealTokenPageNexCli -> {
                String pageStr = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_page);
                long page = Long.parseLong(pageStr);
                page += 1;
                userActionService.setValue(uid, messageId, ActionValEnum.deal_token_page, Long.toString(page));
                botDealHandler.showKeyBoard(uid, messageId, null, null, page,null, null, KeyBoardType.common);
            }
            case BotReplyDealConstants.dealTokenPagePreCli -> {
                String pageStr = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_page);
                long page = Long.parseLong(pageStr);
                page -= 1;
                if(page <= 0) page = 1L;
                userActionService.setValue(uid, messageId, ActionValEnum.deal_token_page, Long.toString(page));
                botDealHandler.showKeyBoard(uid, messageId, null, null, page, null, null, KeyBoardType.common);
            }
            // 限价单-极速模式
            case BotReplyDealConstants.dealLimitFastModeCli -> {
                userActionService.setValue(uid, messageId, ActionValEnum.deal_mode, SniperMode.fast_mode.getValue());
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.limit);
            }
            // 限价单-防夹模式
            case BotReplyDealConstants.dealLimitProtectModeCli -> {
                userActionService.setValue(uid, messageId, ActionValEnum.deal_mode, SniperMode.protect_mode.getValue());
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.limit);
            }
            // 限价单-限价买入
            case BotReplyDealConstants.dealLimitPxBuyCli -> {
                botDealLimitHandler.startPxBuy(bot, uid, messageId);
            }
            // 限价单-限价卖出
            case BotReplyDealConstants.dealLimitPxSellCli -> {
                botDealLimitHandler.startPxSell(bot, uid, messageId);
            }
            // 限价单-涨跌幅买入
            case BotReplyDealConstants.dealLimitRateBuyCli -> {
                botDealLimitHandler.startRateBuy(bot, uid, messageId);
            }
            // 限价单-涨跌幅卖出
            case BotReplyDealConstants.dealLimitRateSellCli -> {
                botDealLimitHandler.startRateSell(bot, uid, messageId);
            }
            // 限价单-从详情返回
            case BotReplyDealConstants.dealLimitOrderReturnAllListCli -> {
                botDealLimitHandler.listAllLimitOrders(bot, uid, messageId);
            }
            // 限价单-关闭所有挂单
            case BotReplyDealConstants.dealOwnedCloseAllOrderCli -> {
                botDealLimitHandler.cancelAllOrder(bot, uid, messageId);
                botDealLimitHandler.listAllLimitOrders(bot, uid, messageId);
            }
            // 限价-返回
            case BotReplyDealConstants.dealLimitReturnCli -> {
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.common);
            }
            // 限价-查看此币种订单
            case BotReplyDealConstants.dealLimitListOrdersCli -> {
                botDealLimitHandler.listLimitOrdersByToken(bot, uid, messageId);
            }
            // 限价-查看此币种订单-返回
            case BotReplyDealConstants.dealLimitOrderReturnOneCli -> {
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.limit);
            }
            // 限价单-查看此币种订单-撤销此币种订单
            case BotReplyDealConstants.dealLimitOrderCancelAllByTokenCli -> {
                botDealLimitHandler.cancelLimitOrdersByToken(bot, uid, messageId);
                botDealHandler.showKeyBoard(uid, messageId, null, null, null, null, null, KeyBoardType.limit);
            }
        }
    }
    @Resource
    private BotStateService botStateService;
    @Resource
    private BotDealHandler botDealHandler;
    @Resource
    private UserActionService userActionService;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
}
