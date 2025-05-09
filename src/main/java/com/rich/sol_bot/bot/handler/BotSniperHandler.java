package com.rich.sol_bot.bot.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.bot.BotManager;
import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.constants.*;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.sniper.SniperGenerateService;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.sniper.enums.SniperStateEnum;
import com.rich.sol_bot.sniper.mapper.SniperPlan;
import com.rich.sol_bot.sniper.mapper.SniperPlanRepository;
import com.rich.sol_bot.sniper.mapper.SniperPlanWallet;
import com.rich.sol_bot.sniper.mapper.SniperPlanWalletRepository;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.service.TokenBalanceService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.trade.service.TokenPxService;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.UserRepository;
import com.rich.sol_bot.wallet.WalletBalanceStatService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStatRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BotSniperHandler {

    @Autowired
    private WalletBalanceStatRepository walletBalanceStatRepository;
    @Autowired
    private TokenPxService tokenPxService;
    @Resource
    @Lazy
    private BotManager botManager;
    @Autowired
    private TokenBalanceService tokenBalanceService;
    @Autowired
    private WalletBalanceStatService walletBalanceStatService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private I18nTranslator i18nTranslator;

    // 开始, 等待输入sniper地址
    public void sniperAddressStart(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperInputTokenContent))
                .build());
    }

    // 开始, 等待输入金额
    public void sniperInputAmount(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperInputAmountContent))
                .build());
    }

    // 狙击计划创建成功
    public SniperPlan sniperCreateSuccess(TokenBot bot, Long uid, Integer messageId) {
        String tokenAddress = userActionService.getValue(uid, ActionValEnum.sniper_token_address);
        UserConfig userConfig = userConfigRepository.getById(uid);
        SniperPlan plan = sniperGenerateService.generateSniperPlan(uid, tokenAddress, BigDecimal.ZERO, userConfig.getSniperFee());
        userActionService.setValue(uid, ActionValEnum.sniper_plan_id, plan.getId().toString());
        return plan;
    }

    // 设置额外gas
    public void setSniperExtraGas(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperInputExtraGasContent))
                .build());
    }

    // 狙击列表
    public void sniperKeyBoard(TokenBot bot, Long uid, Integer messageId) {
        String planId = userActionService.getValue(uid, ActionValEnum.sniper_plan_id);
        this.sniperKeyBoard(bot, uid, messageId, Long.valueOf(planId));
    }

    // 狙击列表
    public void sniperKeyBoard(TokenBot bot, Long uid, Integer messageId, Long planId) {
        SniperPlan plan = sniperPlanRepository.getById(planId);
        TokenBaseInfo baseInfoOne = tokenBaseInfoRepository.getById(plan.getTokenId());
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(baseInfoOne.getAddress());
        List<Long> walletIds = sniperPlanWalletRepository.listAllBind(uid, plan.getId()).stream().map(SniperPlanWallet::getWalletId).toList();
        List<UserWallet> wallets = userWalletRepository.listByUid(uid);
        UserWallet userWallet = null;
        if(!walletIds.isEmpty()) {
            userWallet = userWalletRepository.getPreferWallet(uid, walletIds.get(0));
        }

        String content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperCreateSuccessKeyBoardHead),
                baseInfo.getSymbol(),
                baseInfo.getAddress()
                );
        content += botReplyGenerateService.generateTokenStaticHead(baseInfo, bot.getLanguage(uid));
        content += botReplyGenerateService.generateTokenStaticSocial(baseInfo, bot.getLanguage(uid));
        content += botReplyGenerateService.generateWalletStaticSniper(plan, userWallet);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperKeyboardRefreshText))
                                .callbackData(BotReplySniperConstants.sniperKeyboardRefreshCli)
                                .build()
                )
        );
        if(plan.getState().equals(SniperStateEnum.pre_created)){
            keyboard = this.preCratedKeyBoard(plan, wallets, walletIds, keyboard);
        }
        // 两种状态下允许删除
        if(plan.getState().equals(SniperStateEnum.created) || plan.getState().equals(SniperStateEnum.success)){
            keyboard = this.createdKeyBoard(plan, keyboard);
        }

        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                                .callbackData(BotReplyStartConstants.returnToInitStartCli)
                                .build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(content).inlineKeyboardMarkup(markup)
                .build());
    }

    /**
     * 预创建的钱包面板
     * @param plan
     * @param wallets
     * @param walletIds
     * @param keyboard
     * @return
     */
    public List<List<InlineKeyboardButton>> preCratedKeyBoard(SniperPlan plan, List<UserWallet> wallets, List<Long> walletIds, List<List<InlineKeyboardButton>> keyboard) {
        I18nLanguageEnum language = userRepository.getLanguage(plan.getUid());
        List<InlineKeyboardButton> walletButtons = new ArrayList<>();
        for(UserWallet wallet: wallets) {
            walletButtons.add(
                    InlineKeyboardButton.builder()
                            .text(BotReplySniperConstants.sniperBindButton(walletIds.contains(wallet.getId())) + wallet.getName())
                            .callbackData(BotReplySniperConstants.sniperWalletBindPrefix + plan.getId() + ":" + wallet.getId())
                            .build()
            );
            if(walletButtons.size() == 3) {
                keyboard.add(walletButtons);
                walletButtons = new ArrayList<>();
            }
        }
        if(!walletButtons.isEmpty()) {
            keyboard.add(walletButtons);
        }
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(BotReplySniperConstants.existButton(SniperMode.fast_mode.equals(plan.getMode()))
                                        + i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.sniperSlippageFastModeText))
                                .callbackData(BotReplySniperConstants.sniperSlippageFastModeCli)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(BotReplySniperConstants.existButton(SniperMode.protect_mode.equals(plan.getMode())) +
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.sniperSlippageProtectModeText))
                                .callbackData(BotReplySniperConstants.sniperSlippageProtectModeCli)
                                .build()
                )
        );
        String buyPrefix = i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.generateDealBuyText);
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(buyPrefix + BotReplySniperConstants.generateDealBuyText(BotReplySniperConstants.sniperBuyAmountList.get(0)))
                                .callbackData(BotReplySniperConstants.generateDealBuyCli(plan.getId(), BotReplySniperConstants.sniperBuyAmountList.get(0)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(buyPrefix + BotReplySniperConstants.generateDealBuyText(BotReplySniperConstants.sniperBuyAmountList.get(1)))
                                .callbackData(BotReplySniperConstants.generateDealBuyCli(plan.getId(), BotReplySniperConstants.sniperBuyAmountList.get(1)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(buyPrefix + BotReplySniperConstants.generateDealBuyText(BotReplySniperConstants.sniperBuyAmountList.get(2)))
                                .callbackData(BotReplySniperConstants.generateDealBuyCli(plan.getId(), BotReplySniperConstants.sniperBuyAmountList.get(2)))
                                .build()
                )
        );
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(buyPrefix + BotReplySniperConstants.generateDealBuyText(BotReplySniperConstants.sniperBuyAmountList.get(3)))
                                .callbackData(BotReplySniperConstants.generateDealBuyCli(plan.getId(), BotReplySniperConstants.sniperBuyAmountList.get(3)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(buyPrefix + BotReplySniperConstants.generateDealBuyText(BotReplySniperConstants.sniperBuyAmountList.get(4)))
                                .callbackData(BotReplySniperConstants.generateDealBuyCli(plan.getId(), BotReplySniperConstants.sniperBuyAmountList.get(4)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(buyPrefix + BotReplySniperConstants.generateDealBuyText(BotReplySniperConstants.sniperBuyAmountList.get(5)))
                                .callbackData(BotReplySniperConstants.generateDealBuyCli(plan.getId(), BotReplySniperConstants.sniperBuyAmountList.get(5)))
                                .build()
                )
        );
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.sniperExtraGasText))
                        .callbackData(BotReplySniperConstants.sniperExtraGasCli)
                        .build()
        ));
        return keyboard;
    }

    public List<List<InlineKeyboardButton>> createdKeyBoard(SniperPlan plan, List<List<InlineKeyboardButton>> keyboard) {
        I18nLanguageEnum language = userRepository.getLanguage(plan.getUid());
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.sniperDeleteText))
                                .callbackData(BotReplySniperConstants.sniperDeleteCli + plan.getId())
                                .build()
                )
        );
        return keyboard;
    }



    // 列出狙击列表
    public void listSniper(TokenBot bot, Long uid, Integer messageId) {
        List<SniperPlan> plans = sniperPlanRepository.listCreatedByUid(uid);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<Long> tokenIds = plans.stream().map(SniperPlan::getTokenId).toList();

        List<TokenBaseInfo> baseInfos = new ArrayList<>();
        Map<Long, String> map = new HashMap<>();
        if(!tokenIds.isEmpty()) {
            baseInfos = tokenBaseInfoRepository.list(new LambdaQueryWrapper<TokenBaseInfo>().in(TokenBaseInfo::getId, tokenIds));
            baseInfos.forEach(o -> {
                map.put(o.getId(), o.getSymbol());
            });
        }
        for (SniperPlan plan: plans) {
            String symbol = map.get(plan.getTokenId());
            if(symbol == null) {
                symbol = "";
            }
            keyboard.add(Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperListNameText) + symbol)
                                    .callbackData(BotReplySniperConstants.sniperListNameCliPrefix + plan.getId())
                                    .build()
                    )
            );
        }
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                                .callbackData(BotReplyStartConstants.returnToInitStartCli)
                                .build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperListNameTextList)).inlineKeyboardMarkup(markup)
                .build());
    }

    // pump发射上池子提醒
    public void pumpLaunch(String address) {
        TokenBaseInfo info = tokenBaseInfoRepository.getByAddress(address);
        if(info != null) {
            List<WalletBalanceStat> list = walletBalanceStatRepository.listExistByTokenId(info.getId());
            if(list.isEmpty()) return;
            BigDecimal px = tokenPxService.getPx(info);
            TokenBaseInfo tokenMain = tokenInfoService.getMain();
            for(WalletBalanceStat i:list) {
                ThreadAsyncUtil.execAsync(() -> {
                    this.notifyUser(i.getUid(),px, new BigDecimal(tokenMain.getPrice()), info, i);
                });
            }
        }
    }


    public void notifyUser(Long uid, BigDecimal px, BigDecimal mainPx, TokenBaseInfo info, WalletBalanceStat stat) {
        I18nLanguageEnum language = userRepository.getLanguage(uid);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealPumpLaunchFastText)
                                )
                                .callbackData(BotReplyDealConstants.dealPumpLaunchFastCli + stat.getId())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealPumpLaunchProtectText)
                                )
                                .callbackData(BotReplyDealConstants.dealPumpLaunchProtectCli + stat.getId())
                                .build()
                )
        );
        UserWallet wallet = userWalletRepository.getById(stat.getWalletId());
        stat = walletBalanceStatService.getBalanceStatAndSync(uid, wallet, info, true);
        if(stat.getAmount().compareTo(BigDecimal.ZERO) == 0) return;
        markup.setKeyboard(keyboard);
        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.sniperPumpLaunchContent),
                "100%",
                info.getSymbol(),
                info.getAddress(),
                NumberFormatTool.formatNumber(px, 4),
                NumberFormatTool.formatNumber(px.multiply(stat.getAmount()).divide(mainPx, RoundingMode.HALF_DOWN), 4)
                );
        botManager.pushContent(uid, null, content, markup, null);
    }


    @Resource
    private UserActionService userActionService;
    @Resource
    private SniperGenerateService sniperGenerateService;
    @Resource
    private SniperPlanWalletRepository sniperPlanWalletRepository;
    @Resource
    private SniperPlanRepository sniperPlanRepository;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private BotReplyGenerateService botReplyGenerateService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private UserConfigRepository userConfigRepository;
}
