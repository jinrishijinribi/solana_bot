package com.rich.sol_bot.user.action;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.user.action.mapper.UserAction;
import com.rich.sol_bot.user.action.mapper.UserActionRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserActionService {

    public void setValue(Long uid, ActionValEnum actionVal, String value) {
        userActionRepository.save(UserAction.builder()
                        .id(IdUtil.nextId()).uid(uid)
                .type(actionVal.getAction()).name(actionVal.getValue())
                        .value(value).createdAt(TimestampUtil.now())
                .build());
    }

    public void setValue(Long uid, Integer messageId, ActionValEnum actionVal, String value) {
        userActionRepository.save(UserAction.builder()
                .id(IdUtil.nextId()).uid(uid).messageId(messageId)
                .type(actionVal.getAction()).name(actionVal.getValue())
                .value(value).createdAt(TimestampUtil.now())
                .build());
    }

    public String getValue(Long uid, ActionValEnum actionVal) {
        UserAction action = userActionRepository.getOne(
                new LambdaQueryWrapper<UserAction>()
                        .eq(UserAction::getUid, uid).eq(UserAction::getType, actionVal.getAction())
                        .eq(UserAction::getName, actionVal.getValue()).orderByDesc(UserAction::getCreatedAt)
                        .last("limit 1")
        );
        if(action == null) return null;
        else return action.getValue();
    }

    public String getValue(Long uid, Integer messageId, ActionValEnum actionVal) {
        UserAction action = userActionRepository.getOne(
                new LambdaQueryWrapper<UserAction>()
                        .eq(UserAction::getUid, uid).eq(UserAction::getType, actionVal.getAction())
                        .eq(UserAction::getMessageId, messageId)
                        .eq(UserAction::getName, actionVal.getValue()).orderByDesc(UserAction::getCreatedAt)
                        .last("limit 1")
        );
        if(action == null) return null;
        else return action.getValue();
    }

    public void actionFinish(Long uid, ActionEnum action) {
        userActionRepository.remove(new LambdaQueryWrapper<UserAction>()
                .eq(UserAction::getUid, uid)
                .eq(UserAction::getType, action));
    }


    @Resource
    private UserActionRepository userActionRepository;
}
