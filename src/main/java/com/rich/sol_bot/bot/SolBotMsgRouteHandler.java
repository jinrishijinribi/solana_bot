package com.rich.sol_bot.bot;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.CheckService;
import com.rich.sol_bot.bot.handler.*;
import com.rich.sol_bot.bot.handler.constants.*;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.route.*;
import com.rich.sol_bot.user.UserService;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.User;
import com.rich.sol_bot.user.mapper.UserRepository;
import com.rich.sol_bot.wallet.UserWalletService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class SolBotMsgRouteHandler implements BotMsgHandler {

    @Autowired
    private BotSettingHandler botSettingHandler;
    @Autowired
    private BotInviteHandler botInviteHandler;
    @Autowired
    private BotSniperHandler botSniperHandler;
    @Autowired
    private BotDealLimitHandler botDealLimitHandler;
    @Autowired
    private BotPositionHandler botPositionHandler;
    @Autowired
    private UserService userService;
    @Autowired
    private SolBotScraperRoute solBotScraperRoute;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void handler(TokenBot bot, Update update) {
//        log.info("{}", update);
        // 如果是群聊中的ca地址
        if(update.hasMessage() && ("group".equals(update.getMessage().getChat().getType()) || "supergroup".equals(update.getMessage().getChat().getType()))){
            String ca = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            // 处理完就返回
            if(ca == null) return;
            ca = ca.replace("https://www.pump.fun/", "").replace("https://pump.fun/", "");
            if(checkService.isTokenAddress(ca)){
                botGroupHandler.showGroupBoard(bot, chatId, ca);
            }
            return;
        }
        if(update.hasMessage() && ! "private".equals(update.getMessage().getChat().getType())) {
            return;
        }
        // 放在这个位置，不打印群组的消息
        log.info("{}", update);
        Long uid = null;
        if(update.hasMessage() && update.getMessage().getFrom() != null) {
            uid = update.getMessage().getFrom().getId();
        }
        if(update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            uid = update.getCallbackQuery().getFrom().getId();
        }
        if(uid != null) {
            botStartHandler.activeUser(uid);
            User user = userRepository.getById(uid);
            if(user != null) {
                bot.setLanguage(uid, user.getLanguage());
            }
        }

        // 处理命令行
        if(update.hasMessage() && update.getMessage().hasText()){
            String cliText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            switch (cliText){
                // 主菜单
                case "/menu" -> {
                    botStartHandler.handlerStart(bot, chatId, null);
                    return;
                }
                // 已设置狙击
                case "/sniper" -> {
                    botSniperHandler.listSniper(bot, chatId, null);
                    return;
                }
                // 已配置挂单
                case "/limit" -> {
                    botDealLimitHandler.listAllLimitOrders(bot, chatId, null);
                    return;
                }
                // 查看持仓
                case "/assets" -> {
                    botPositionHandler.walletKeyBoard(bot, chatId, null);
                    return;
                }
                // 钱包
                case "/wallets" -> {
                    if(!userWalletService.walletExist(chatId)){
                        botWalletHandler.noWalletStart(bot, chatId, null);
                    } else {
                        botWalletHandler.walletKeyBoard(bot, chatId, null);
                    }
                    return;
                }
                // 设置
                case "/settings" -> {
                    botSettingHandler.settingKeyboard(bot, chatId, null);
                    return;
                }
                // 帮助
                case "/help" -> {
                    botStartHandler.startHelp(bot, chatId);
                    return;
                }
                // 邀请返佣
                case "/invite" -> {
                    botInviteHandler.inviteKeyboard(bot, chatId, null);
                    return;
                }
            }
        }

        // 处理 键盘面板上的callback
        if(update.hasCallbackQuery()) {
            String content = update.getCallbackQuery().getData();
            uid = update.getCallbackQuery().getFrom().getId();
            log.info("{}: callback content: {}", uid, content);
            // 初始键盘
            if(content.startsWith(BotReplyStartConstants.initStartPrefix)){
                solBotStartRoute.handlerCallbackQuery(bot, update);
            }
            // 交易下级键盘
            if(content.startsWith(BotReplyDealConstants.dealPrefix)){
                solBotDealRoute.handlerCallbackQuery(bot,  update);
            }
            // 钱包下级键盘
            if(content.startsWith(BotReplyWalletConstants.walletPrefix)){
                solBotWalletRoute.handlerCallbackQuery(bot, update);
            }
            // 设置下级键盘
            if(content.startsWith(BotReplySettingConstants.settingPrefix)){
                solBotSettingRoute.handlerCallbackQuery(bot, update);
            }
            // 狙击下级键盘
            if(content.startsWith(BotReplySniperConstants.sniperPrefix)){
                solBotSniperRoute.handlerCallbackQuery(bot, update);
            }
            // 邀请下级键盘
            if(content.startsWith(BotReplyInviteConstants.invitePrefix)){
                solBotInviteRoute.handlerCallbackQuery(bot, update);
            }
            // 仓位
            if(content.startsWith(BotReplyPositionConstants.positionPrefix)){
                solBotPositionRoute.handlerCallbackQuery(bot, update);
            }
            // 刮刀
            if(content.startsWith(BotReplyScraperConstants.scraperPrefix)) {
                solBotScraperRoute.handlerCallbackQuery(bot, update);
            }
        }

        if(update.hasMessage() && update.getMessage().hasText()){
            // 处理 /start
            uid = update.getMessage().getFrom().getId();
            String content = update.getMessage().getText();
            String refCode = content.replace(BotReplyStartConstants.startCode, "").trim();
            botStartHandler.checkUser(update, refCode);
            log.info("{}: text content: {}", uid, content);
            //  /start 的入口
            if(content.startsWith(BotReplyStartConstants.startCode)) {
                if(checkService.isTokenAddress(refCode)){
                    botStartHandler.handlerDirectAddress(bot, update, refCode);
                } else {
                    botStartHandler.handlerStartReplyKeyBoard(bot, uid, null);
                    botStartHandler.handlerStart(bot, uid, null);
                }
                return;
            }
            // 处理reply keyboard 的响应 text 输入，如果被处理了，则结束
            if(solBotStartRoute.handlerReplyContent(bot, update)){
                return;
            };
            // 处理 有state的状态输入
            LockStateEnum state = botStateService.getState(uid);
            if(state != null) {
                solBotStateRoute.handlerWithState(bot, update, state, content);
                return;
            } else {
                // 最后处理什么都没命中的合约地址
                // 尝试去掉pump的前缀
                content = content.replace("https://www.pump.fun/", "").replace("https://pump.fun/", "");
                if(checkService.isTokenAddress(content)){
                    // 没有钱包，就引导去创建钱包
                    if(!userWalletService.walletExist(uid)){
                        botWalletHandler.noWalletStart(bot, uid, null);
                    } else {
                        botStartHandler.handlerDirectAddress(bot, update, content);
                    }
                }
            }
        }
    }


    @Resource
    private BotStartHandler botStartHandler;
    @Resource
    private BotWalletHandler botWalletHandler;
    @Resource
    private BotStateService botStateService;
    @Resource
    private SolBotDealRoute solBotDealRoute;
    @Resource
    private SolBotStateRoute solBotStateRoute;
    @Resource
    private SolBotStartRoute solBotStartRoute;
    @Resource
    private SolBotWalletRoute solBotWalletRoute;
    @Resource
    private SolBotSettingRoute solBotSettingRoute;
    @Resource
    private SolBotSniperRoute solBotSniperRoute;
    @Resource
    private SolBotInviteRoute solBotInviteRoute;
    @Resource
    private SolBotPositionRoute solBotPositionRoute;
    @Resource
    private CheckService checkService;
    @Resource
    private BotGroupHandler botGroupHandler;
    @Resource
    private UserWalletService userWalletService;

}
