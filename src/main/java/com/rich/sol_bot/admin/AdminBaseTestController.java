package com.rich.sol_bot.admin;

import com.rich.sol_bot.admin.mapper.Admin;
import com.rich.sol_bot.admin.mapper.AdminRepository;
import com.rich.sol_bot.admin.service.MFATotpService;
import com.rich.sol_bot.admin.stat.StatService;
import com.rich.sol_bot.admin.stat.mapper.StatInvite;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.AesTool;
import com.rich.sol_bot.trade.dto.ProfitAndLoseDTO;
import com.rich.sol_bot.user.mapper.User;
import com.rich.sol_bot.user.mapper.UserRelationRepository;
import com.rich.sol_bot.user.mapper.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@Tag(name = "管理面板测试")
@RestController
@RequestMapping("/api/test")
public class AdminBaseTestController {


    private final UserRelationRepository userRelationRepository;
    private final UserRepository userRepository;
    private final StatService statService;
    @Autowired
    private AdminRepository adminRepository;

    public AdminBaseTestController(UserRelationRepository userRelationRepository, UserRepository userRepository, StatService statService) {
        this.userRelationRepository = userRelationRepository;
        this.userRepository = userRepository;
        this.statService = statService;
    }

    @Operation(summary = "返回某个时区某个时间0点的时间戳")
    @GetMapping("/ts/zero")
    public Timestamp tsZero(Timestamp ts, Long zoneOffset) {
        return TimestampUtil.getTimestampOfZone(ts, zoneOffset);
    }

    @Operation(summary = "统计某个用户的邀请信息")
    @GetMapping("/stat/invite")
    public Object statInvite(Long uid) {
        return statService.updateInit();
    }

    @Operation(summary = "创建一个admin")
    @GetMapping("/admin/create")
    public String createAdmin(String username) {
        String secret = mfaTotpService.newSecret();
        adminRepository.save(Admin.builder().id(IdUtil.nextId())
                        .username(username).auth(aesTool.encrypt(secret)).lastLoginAt(TimestampUtil.now())
                .build());
        return secret;
    }

    @Resource
    private MFATotpService mfaTotpService;
    @Resource
    private AesTool aesTool;
}
