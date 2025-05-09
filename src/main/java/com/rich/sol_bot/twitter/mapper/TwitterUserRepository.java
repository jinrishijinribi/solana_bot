package com.rich.sol_bot.twitter.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TwitterUserRepository extends ServiceImpl<TwitterUserMapper, TwitterUser> {
    public TwitterUser findByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<TwitterUser>().eq(TwitterUser::getUsername, username));
    }

    public TwitterUser findByTwitterUid(String twitterUid) {
        return this.getOne(new LambdaQueryWrapper<TwitterUser>().eq(TwitterUser::getTwitterUid, twitterUid));
    }
}
