package com.rich.sol_bot.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rich.sol_bot.admin.mapper.Admin;
import com.rich.sol_bot.admin.stat.StatService;
import com.rich.sol_bot.admin.stat.dto.*;
import com.rich.sol_bot.admin.stat.enums.ParentUpdateScene;
import com.rich.sol_bot.admin.stat.mapper.*;
import com.rich.sol_bot.admin.stat.params.SetRebateRateParam;
import com.rich.sol_bot.admin.stat.params.StatInviteParam;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.mvc.RequestContextManager;
import com.rich.sol_bot.system.query.PageResult;
import com.rich.sol_bot.user.mapper.User;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.mapper.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Tag(name = "管理面板")
@RestController
@RequestMapping("/api/admin")
public class AdminBaseController {

    private final StatInitRepository statInitRepository;
    private final StatService statService;
    private final StatTradeDailyRepository statTradeDailyRepository;
    private final StatInviteRepository statInviteRepository;
    private final UserConfigRepository userConfigRepository;
    private final RequestContextManager requestContextManager;
    private final UserRepository userRepository;

    public AdminBaseController(StatInitRepository statInitRepository, StatService statService, StatTradeDailyRepository statTradeDailyRepository, StatInviteRepository statInviteRepository, UserConfigRepository userConfigRepository, RequestContextManager requestContextManager, UserRepository userRepository) {
        this.statInitRepository = statInitRepository;
        this.statService = statService;
        this.statTradeDailyRepository = statTradeDailyRepository;
        this.statInviteRepository = statInviteRepository;
        this.userConfigRepository = userConfigRepository;
        this.requestContextManager = requestContextManager;
        this.userRepository = userRepository;
    }

    @Operation(summary = "首页统计")
    @GetMapping("/stat/start")
    public StatInitResult tsZero() {
        Admin admin = requestContextManager.admin();
        Timestamp ts = TimestampUtil.getTimestampOfZone(TimestampUtil.now(), 8L);
        StatInit init = statInitRepository.getById(ts.getTime());
        if(init == null) {
            init = statService.updateInit();
        }
        return StatInitResult.transfer(init);
    }

    @Operation(summary = "交易量图表")
    @GetMapping("/stat/tv/view")
    public List<StatTVView> statTvView(Timestamp start, Timestamp end) {
        Admin admin = requestContextManager.admin();
        List<StatTradeDaily> list = statTradeDailyRepository.list(
                new LambdaQueryWrapper<StatTradeDaily>()
                        .ge(StatTradeDaily::getId, start.getTime()).lt(StatTradeDaily::getId, end.getTime())
        );
        return list.stream().map(StatTVView::transfer).toList();
    }

    @Operation(summary = "邀请返佣管理")
    @PostMapping("/stat/invite/list")
    public PageResult<StatInviteDTO> inviteList(@RequestBody @Valid StatInviteParam param) {
        Admin admin = requestContextManager.admin();
        QueryWrapper<StatInvite> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(param.getUsername())) {
            queryWrapper.like("username", param.getUsername()).or().like("nickname", param.getUsername());
        }
        if(param.getOrderValue() != null) {
            queryWrapper.orderBy(true, param.getAsc(), param.getOrderValue().getValue());
        }
        Integer page = param.getPage();
        Integer size = param.getSize();
        List<StatInvite> list = statInviteRepository.page(new Page<>(page, size), queryWrapper).getRecords();
        long count = statInviteRepository.count(queryWrapper);
        return PageResult.<StatInviteDTO>builder()
                .rows(list.stream().map(StatInviteDTO::transfer).toList()).count(count)
                .build();
    }


    @Operation(summary = "更新返佣比例")
    @PostMapping("/set/rebate/rate")
    public Boolean setRebateRate(@RequestBody @Valid SetRebateRateParam param) {
//        Admin admin = requestContextManager.admin();
        if(param.getRate().compareTo(BigDecimal.ZERO) < 0 || param.getRate().compareTo(BigDecimal.ONE) > 0) return false;
        userConfigRepository.update(new LambdaUpdateWrapper<UserConfig>().set(UserConfig::getRebateRate, param.getRate()).eq(UserConfig::getId, param.getId()));
        StatInvite statInvite = statInviteRepository.getById(param.getId());
        if(statInvite == null) {
            User user = userRepository.getById(param.getId());
            UserConfig userConfig = userConfigRepository.getById(param.getId());
            statInvite = StatInvite.builder()
                    .id(param.getId())
                    .nickname(com.rich.sol_bot.system.common.StringUtils.nullToEmpty(user.getFirstName()) + com.rich.sol_bot.system.common.StringUtils.nullToEmpty(user.getLastName()))
                    .username(user.getUsername())
                    .lastUpdateAt(TimestampUtil.now())
                    .rebate(userConfig.getRebateRate()).inviteCountToday(0L)
                    .validCount(0L).tradeAmount(BigDecimal.ZERO).tradeAmountToday(BigDecimal.ZERO).inviteCount(0L)
                    .rebate(BigDecimal.ZERO).rebateToday(BigDecimal.ZERO).rebateRate(userConfig.getRebateRate())
                    .build();
        } else {
            statInvite.setRebateRate(param.getRate());
        }
        statInviteRepository.saveOrUpdate(statInvite);
        return true;
    }


    @Operation(summary = "根据username模糊搜索用户")
    @GetMapping("/set/rebate/rate")
    public List<UserDTO> searchUser(String username) {
//        Admin admin = requestContextManager.admin();
        if(StringUtils.isEmpty(username)) return new ArrayList<>();
        List<User> list = userRepository.list(new LambdaQueryWrapper<User>().like(User::getUsername, username));
        return list.stream().map(UserDTO::transfer).toList();
    }

    @Operation(summary = "一次性更新所有的代理商记录")
    @PostMapping("/admin/init")
    public Boolean inviteInit(@RequestBody List<Long> ids) {
        for(Long id: ids) {
            User user = userRepository.getById(id);
            if(user == null) continue;
            statService.updateStatInvite(user, ParentUpdateScene.all);
        }
        return true;
    }
}
