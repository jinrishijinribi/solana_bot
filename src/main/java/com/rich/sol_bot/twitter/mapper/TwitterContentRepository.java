package com.rich.sol_bot.twitter.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TwitterContentRepository extends ServiceImpl<TwitterContentMapper, TwitterContent> {

    public boolean existByPostId(String postId) {
        return this.count(new LambdaQueryWrapper<TwitterContent>().eq(TwitterContent::getPostId, postId)) > 0;
    }

    public void confirm(Long id) {
        this.update(new LambdaUpdateWrapper<TwitterContent>().set(TwitterContent::getConfirmed, 1).eq(TwitterContent::getId, id));
    }

}
