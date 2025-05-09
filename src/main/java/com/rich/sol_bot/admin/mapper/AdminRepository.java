package com.rich.sol_bot.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AdminRepository extends ServiceImpl<AdminMapper, Admin> {
    public Admin getByUserName(String username) {
        return this.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username).last("limit 1"));
    }
}
