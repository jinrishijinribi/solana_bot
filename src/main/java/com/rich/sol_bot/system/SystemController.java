package com.rich.sol_bot.system;

import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.system.tool.AesTool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@Tag(name = "系统")
@RestController
@RequestMapping("/api/system")
public class SystemController {
    @Operation(summary = "系统名")
    @GetMapping("/name")
    public String name() {
        return systemConfigRepository.value(SystemConfigConstant.SYSTEM_NAME);
    }

    @Operation(summary = "ping")
    @GetMapping("/ping")
    public String ping() {
        return "success";
    }

//
//
    @Operation(summary = "decode")
    @GetMapping("/decode")
    public String decode(String val) {
        return aesTool.decrypt(val);
    }

    @Operation(summary = "encode")
    @GetMapping("/encode")
    public String encode(String val) {
        return aesTool.encrypt(val);
    }

    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private AesTool aesTool;
}
