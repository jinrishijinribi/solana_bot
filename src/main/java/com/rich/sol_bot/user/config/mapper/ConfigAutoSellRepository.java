package com.rich.sol_bot.user.config.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigAutoSellRepository extends ServiceImpl<ConfigAutoSellMapper, ConfigAutoSell> {
    public List<ConfigAutoSell> listExist(Long uid) {
        return this.list(new LambdaQueryWrapper<ConfigAutoSell>().eq(ConfigAutoSell::getDeleted, 0).eq(ConfigAutoSell::getUid, uid));
    }

    public void deleteAutoConfig(Long id) {
        this.update(new LambdaUpdateWrapper<ConfigAutoSell>().set(ConfigAutoSell::getDeleted, 1).eq(ConfigAutoSell::getId, id));
    }
}
