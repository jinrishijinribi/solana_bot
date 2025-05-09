package com.rich.sol_bot.admin.stat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.admin.stat.dto.TradeStatDTO;
import com.rich.sol_bot.admin.stat.enums.ParentUpdateScene;
import com.rich.sol_bot.admin.stat.mapper.*;
import com.rich.sol_bot.system.common.StringUtils;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TradeInfoMapper;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.mapper.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class StatService {
    @Autowired
    private StatInviteMapper statInviteMapper;
    @Autowired
    private TokenInfoService tokenInfoService;
    @Autowired
    private StatInitRepository statInitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TradeInfoMapper tradeInfoMapper;
    @Autowired
    private UserConfigRepository userConfigRepository;

    /**
     * 邀请 + 交易返现的时候更新
     * @param user 上级用户
     * @return
     */
    public void updateStatInvite(User user, ParentUpdateScene scene) {
        if(user == null ) return;
        Long pid = user.getId();
        StatInvite invite = statInviteRepository.getById(user.getId());
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        UserConfig userConfig = userConfigRepository.getById(user.getId());
        if(invite == null) {
            invite = StatInvite.builder()
                    .id(user.getId())
                    .nickname(StringUtils.nullToEmpty(user.getFirstName()) + StringUtils.nullToEmpty(user.getLastName()))
                    .username(user.getUsername())
                    .lastUpdateAt(TimestampUtil.now())
                    .rebate(userConfig.getRebateRate()).inviteCountToday(0L)
                    .validCount(0L).tradeAmount(BigDecimal.ZERO).tradeAmountToday(BigDecimal.ZERO).inviteCount(0L)
                    .rebate(BigDecimal.ZERO).rebateToday(BigDecimal.ZERO).rebateRate(userConfig.getRebateRate())
                    .build();
        } else {
            invite.setNickname(StringUtils.nullToEmpty(user.getFirstName()) + StringUtils.nullToEmpty(user.getLastName()));
            invite.setUsername(user.getUsername());
            invite.setRebateRate(userConfig.getRebateRate());
        }
        Timestamp startTs = TimestampUtil.getTimestampOfZone(TimestampUtil.now(), 8L);
        switch (scene) {
            case trade -> {
                Long validCount = statInviteMapper.validInvite(pid);
                TradeStatDTO tradeStatToday = statInviteMapper.tradeAmount(pid,  mainToken.getId(), startTs);
                TradeStatDTO tradeStatAll = statInviteMapper.tradeAmount(pid,  mainToken.getId(), new Timestamp(0));
                invite.setValidCount(validCount);
                invite.setTradeAmountToday(tradeStatToday.getTradeAmount());
                invite.setTradeAmount(tradeStatAll.getTradeAmount());
                invite.setRebateToday(tradeStatToday.getTradeRebate());
                invite.setRebate(tradeStatAll.getTradeRebate());
            }
            case invite -> {
                Long inviteCount = statInviteMapper.countInvite(pid, new Timestamp(0));
                Long inviteCountToday = statInviteMapper.countInvite(pid, startTs);
                invite.setInviteCount(inviteCount);
                invite.setInviteCountToday(inviteCountToday);
            }
            case all -> {
                Long validCount = statInviteMapper.validInvite(pid);
                TradeStatDTO tradeStatToday = statInviteMapper.tradeAmount(pid,  mainToken.getId(), startTs);
                TradeStatDTO tradeStatAll = statInviteMapper.tradeAmount(pid,  mainToken.getId(), new Timestamp(0));
                invite.setValidCount(validCount);
                invite.setTradeAmountToday(tradeStatToday.getTradeAmount());
                invite.setTradeAmount(tradeStatAll.getTradeAmount());
                invite.setRebateToday(tradeStatToday.getTradeRebate());
                invite.setRebate(tradeStatAll.getTradeRebate());

                Long inviteCount = statInviteMapper.countInvite(pid, new Timestamp(0));
                Long inviteCountToday = statInviteMapper.countInvite(pid, startTs);
                invite.setInviteCount(inviteCount);
                invite.setInviteCountToday(inviteCountToday);
            }
        }
        invite.setLastUpdateAt(TimestampUtil.now());
        statInviteRepository.saveOrUpdate(invite);
    }

    /**
     * 交易成功时更新，并且定时任务，每1h更新
     * @param ts
     * @return
     */
    public void updateDaily(Timestamp ts) {
        ts = TimestampUtil.getTimestampOfZone(ts, 8L);
        StatTradeDaily one = statTradeDailyRepository.getById(ts);
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        TradeStatDTO tradeStatDTO = statInviteMapper.tradeAmountOfTimeRange(mainToken.getId(), ts, new Timestamp(ts.getTime() + 24 * 60 * 60 * 1000));
        if(one != null) {
            one.setTradeAmount(tradeStatDTO.getTradeAmount());
        } else {
            one = StatTradeDaily.builder().id(ts.getTime()).tradeAmount(tradeStatDTO.getTradeAmount()).build();
        }
        statTradeDailyRepository.saveOrUpdate(one);
//        return one;
    }

    /**
     * 首页数据，有交易成功是更新
     * @return
     */
    public StatInit updateInit() {
        Timestamp ts = TimestampUtil.getTimestampOfZone(TimestampUtil.now(), 8L);
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        StatInit statInit = statInitRepository.getById(ts.getTime());
        if(statInit == null) {
            statInit = StatInit.builder().id(ts.getTime()).build();
        }
        Long regUser = userRepository.count();
        Long regUserToday = userRepository.count(new LambdaQueryWrapper<User>().gt(User::getCreatedAt, ts));
        TradeStatDTO allStat = statInviteMapper.tradeAmountOfTimeRange(mainToken.getId(), new Timestamp(0), TimestampUtil.now());
        TradeStatDTO todayStat = statInviteMapper.tradeAmountOfTimeRange(mainToken.getId(), ts, TimestampUtil.now());
        Long tradeUser = tradeInfoMapper.countUid(new Timestamp(0), TimestampUtil.now());
        Long tradeUserToday = tradeInfoMapper.countUid(ts, TimestampUtil.now());
        Long tradeCountAll = tradeInfoMapper.countSuccess(new Timestamp(0), TimestampUtil.now());
        Long tradeCountToday = tradeInfoMapper.countSuccess(ts, TimestampUtil.now());
        Long activeUserToday = userRepository.count(new LambdaQueryWrapper<User>().gt(User::getLastActive, ts));
        statInit.setRegUser(regUser);
        statInit.setRegUserToday(regUserToday);
        statInit.setTradeAmount(allStat.getTradeAmount());
        statInit.setTradeAmountToday(todayStat.getTradeAmount());
        statInit.setTradeUser(tradeUser);
        statInit.setTradeUserToday(tradeUserToday);
        statInit.setTradeCount(tradeCountAll);
        statInit.setTradeCountToday(tradeCountToday);
        statInit.setActiveUserToday(activeUserToday);
        statInit.setLastUpdateAt(TimestampUtil.now());
        statInitRepository.saveOrUpdate(statInit);
        return statInit;
    }



    @Resource
    private StatInviteRepository statInviteRepository;
    @Resource
    private StatTradeDailyRepository statTradeDailyRepository;
}
