package com.rich.sol_bot.admin.login;


import com.rich.sol_bot.admin.login.dto.LoginResultDTO;
import com.rich.sol_bot.admin.login.param.LoginParam;
import com.rich.sol_bot.admin.mapper.Admin;
import com.rich.sol_bot.admin.mapper.AdminRepository;
import com.rich.sol_bot.admin.service.AdminLoginService;
import com.rich.sol_bot.admin.service.MFATotpService;
import com.rich.sol_bot.admin.stat.dto.StatInitResult;
import com.rich.sol_bot.system.exception.DefaultAppExceptionCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理面板")
@RestController
@RequestMapping("/api/admin")
public class AdminLoginController {


    @Autowired
    private AdminLoginService adminLoginService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public LoginResultDTO login(@RequestBody LoginParam param) {
        Admin admin = adminRepository.getByUserName(param.getUsername());
        if(admin == null) {
            throw DefaultAppExceptionCode.ADMIN_NOT_EXIST.toException();
        }
        String token = adminLoginService.verifyTotp(admin, param.getCode());
        if(token == null) {
            throw DefaultAppExceptionCode.LOGIN_FAIL.toException();
        }
        return LoginResultDTO.builder().token(token).build();
    }


    @Resource
    private AdminRepository adminRepository;
    @Resource
    private MFATotpService mfaTotpService;

}
