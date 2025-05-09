package com.rich.sol_bot.user;

import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.mapper.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserService {
//    @Transactional
//    public void generateUser(User user) {
//        User exist = userRepository.getById(user.getId());
//        if(exist == null) {
//            userRepository.save(user);
//            String rebateRate = systemConfigRepository.value(SystemConfigConstant.TRADE_REBATE_RATE);
//            userConfigRepository.generateDefaultConfig(user.getId(), new BigDecimal(rebateRate));
//        }
//    }

    @Transactional
    public void generateUser(User user, String refCode) {
        User exist = userRepository.getById(user.getId());
        if(exist == null) {
            userRepository.save(user);
            userRelationService.tryToBindRelation(user, refCode);
            String rebateRate = systemConfigRepository.value(SystemConfigConstant.TRADE_REBATE_RATE);
            String feeRate = systemConfigRepository.value(SystemConfigConstant.TRADE_FEE_RATE);
            userConfigRepository.generateDefaultConfig(user.getId(), new BigDecimal(rebateRate), new BigDecimal(feeRate));
        }
    }

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserConfigRepository userConfigRepository;
    @Resource
    private UserRelationService userRelationService;
    @Resource
    private SystemConfigRepository systemConfigRepository;
}
