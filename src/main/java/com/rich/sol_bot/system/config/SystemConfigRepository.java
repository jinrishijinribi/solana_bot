package com.rich.sol_bot.system.config;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.tool.AesTool;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigRepository extends ServiceImpl<SystemConfigMapper, SystemConfig> {
    public String value(SystemConfigConstant name) {
        return this.getById(name.getId()).getValue();
    }

    public void setValue(SystemConfigConstant name, String value) {
        this.update(new LambdaUpdateWrapper<SystemConfig>()
                        .set(SystemConfig::getValue, value)
                .eq(SystemConfig::getId, name));
    }

    public String valueDecode(SystemConfigConstant name) {
        String val = this.getById(name.getId()).getValue();
        return aesTool.decrypt(val);
    }


    @Resource
    private AesTool aesTool;
}
