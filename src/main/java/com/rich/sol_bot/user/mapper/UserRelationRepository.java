package com.rich.sol_bot.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRelationRepository extends ServiceImpl<UserRelationMapper, UserRelation> {
    public Long countSon(Long pid){
        return this.count(new LambdaQueryWrapper<UserRelation>().eq(UserRelation::getPid, pid));
    }

    public Long getParent(Long uid) {
        UserRelation userRelation = this.getById(uid);
        if(userRelation == null) return null;
        else return userRelation.getPid();
    }
}
