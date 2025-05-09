package com.rich.sol_bot.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import org.springframework.stereotype.Service;

@Service
public class UserRepository extends ServiceImpl<UserMapper, User> {
    public User getByRefCode(String refCode) {
        return this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getRefCode, refCode)
                        .last("limit 1")
        );
    }

    public I18nLanguageEnum getLanguage(Long uid) {
        User user = this.getById(uid);
        if(user == null) return I18nLanguageEnum.zh_CN;
        else return user.getLanguage();
    }

    public void setLanguage(Long uid, I18nLanguageEnum language) {
        this.update(new LambdaUpdateWrapper<User>().set(User::getLanguage, language).eq(User::getId, uid));
    }
}
