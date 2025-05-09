package com.rich.sol_bot.scraper;

import com.rich.sol_bot.bot.BotManager;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyDealConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyScraperConstants;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.scraper.mapper.UserScraperTask;
import com.rich.sol_bot.scraper.mapper.UserScraperTaskRepository;
import com.rich.sol_bot.scraper.mapper.UserScraperTaskSub;
import com.rich.sol_bot.scraper.mapper.UserScraperTaskSubRepository;
import com.rich.sol_bot.sniper.SniperGenerateService;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.TokenService;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.mapper.TradeInfoRepository;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ScraperService {

    @Autowired
    private TokenInfoService tokenInfoService;
    @Autowired
    private UserConfigRepository userConfigRepository;
    @Autowired
    private TradeInfoRepository tradeInfoRepository;
    @Autowired
    private UserScraperTaskRepository userScraperTaskRepository;
    @Autowired
    private UserScraperTaskSubRepository userScraperTaskSubRepository;
    @Autowired
    private I18nTranslator i18nTranslator;
    @Autowired
    private UserRepository userRepository;

    public void sendTaskAndAddress(String address, List<UserScraperTask> tasks) {
        if(!tokenService.isToken(address)) return;
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(address);
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        if(tokenService.canDeal(address)) {
            // 创建tradeDeal
            for(UserScraperTask task: tasks) {
                Long id = IdUtil.nextId();
                UserScraperTaskSub subTask = UserScraperTaskSub
                        .builder().id(id).uid(task.getUid()).taskId(task.getId()).tokenAddress(address)
                        .amount(task.getAmount()).mode(task.getMode())
                        .walletId(task.getWalletId()).createdAt(TimestampUtil.now()).build();
                userScraperTaskSubRepository.save(subTask);
                userScraperTaskRepository.updateSuccessCount(task.getId(), task.getUid(), task.getCount(), task.getSuccessCount() + 1);
                UserConfig userConfig = userConfigRepository.getById(task.getUid());
                BigDecimal slippage = task.getMode().equals(SniperMode.fast_mode) ? userConfig.getFastSlippage() : userConfig.getProtectSlippage();
                Integer dedicated = task.getMode().equals(SniperMode.fast_mode) ? 0 : 1;
                TradeInfo info = TradeInfo.builder()
                        .id(id).uid(task.getUid())
                        .walletId(task.getWalletId())
                        .tokenInId(baseInfo.getId()).tokenIn(baseInfo.getSymbol()).tokenInAmount(null)
                        .tokenOutId(mainToken.getId()).tokenOut(mainToken.getSymbol()).tokenOutAmount(task.getAmount())
                        .tx(null).gas(null).state(TradeStateEnum.created).rebate(null).slippage(slippage)
                        .extraGas(userConfig.getBuyFee())
                        .createdAt(TimestampUtil.now())
                        .dedicated(dedicated)
                        .iceId(null)
                        .build();
                tradeInfoRepository.save(info);
                I18nLanguageEnum language = userRepository.getLanguage(userConfig.getId());
                botManager.bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(task.getUid()).messageId(null)
                        .text(String.format(
                                i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.scraperSendSuccess),
                                (task.getId() % 1000),
                                task.getTwitterUsername(),
                                baseInfo.getSymbol(),
                                address,
                                task.getSuccessCount()
                                )
                        )
                        .build());
            }
        }
//        else {
//            // 创建狙击
//            for (UserScraperTask task : tasks) {
//                UserConfig userConfig = userConfigRepository.getById(task.getUid());
//                sniperGenerateService.generateSniperPlan(task.getUid(), address, task.getAmount(), userConfig.getSniperFee());
//            }
//        }
    }

    @Resource
    private TokenService tokenService;
    @Resource
    private BotManager botManager;
}
