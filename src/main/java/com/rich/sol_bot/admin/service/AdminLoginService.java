package com.rich.sol_bot.admin.service;

import com.rich.sol_bot.admin.mapper.Admin;
import com.rich.sol_bot.admin.mapper.AdminRepository;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.exception.DefaultAppExceptionCode;
import com.rich.sol_bot.system.tool.AesTool;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.cache.CacheBiz;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AdminLoginService {

    @Autowired
    private AdminRepository adminRepository;

    public String verifyTotp(Admin admin, String code) {
        String secret = this.getSecret(admin);
        if(mfaTotpService.validateCode0(secret, code)){
            String token = IdUtil.simpleUuid();
            this.cacheItem(admin.getId().toString(), CacheBiz.login, "token", token);
            this.cacheItem(token, CacheBiz.login, "uid", admin.getId().toString());
            return token;
        }
        return null;
    }

    public Admin getCacheAdmin(String token) {
        if(StringUtils.isEmpty(token)) {
            throw DefaultAppExceptionCode.UN_LOGIN.toException();
        }
        String adminId = this.getCacheItem(token, CacheBiz.login, "uid");
        Admin admin = adminRepository.getById(adminId);
        if(admin == null) {
            throw DefaultAppExceptionCode.UN_LOGIN.toException();
        }
        return admin;
    }


    public String getSecret(Admin admin) {
        return aesTool.decrypt(admin.getAuth());
    }

    public void cacheItem(String uid, CacheBiz biz, String key, String item) {
        String redisKey = redisKeyGenerateTool.generateKey(uid, biz.getValue(), key);
        RBucket<String> rBucket = redisson.getBucket(redisKey);
        rBucket.set(item, 24 * 30, TimeUnit.HOURS);
    }

    public String getCacheItem(String uid, CacheBiz biz,  String key) {
        String redisKey = redisKeyGenerateTool.generateKey(uid, biz.getValue(), key);
        RBucket<String> rBucket = redisson.getBucket(redisKey);
        return rBucket.get();
    }


    @Resource
    private AesTool aesTool;
    @Resource
    private MFATotpService mfaTotpService;
    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private RedissonClient redisson;
}
