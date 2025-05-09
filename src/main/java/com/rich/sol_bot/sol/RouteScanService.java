package com.rich.sol_bot.sol;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.gson.Gson;
import com.rich.sol_bot.sniper.enums.SniperStateEnum;
import com.rich.sol_bot.sniper.mapper.*;
import com.rich.sol_bot.sol.entity.RayComputeAmountOut;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoin;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoinMapper;
import com.rich.sol_bot.sol.queue.Message;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.system.tool.AesTool;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoMapper;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wangqiyun
 * @since 2024/3/20 18:38
 */

@Service
@Slf4j
public class RouteScanService {


    @Autowired
    private UserConfigRepository userConfigRepository;

    public void newCoinScan(String mint) {
        TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getByAddress(mint);
        if (tokenBaseInfo == null) return;
        List<SniperPlan> sniperPlans = sniperPlanMapper.selectList(new LambdaQueryWrapper<SniperPlan>().eq(SniperPlan::getTokenId, tokenBaseInfo.getId())
                .eq(SniperPlan::getState, SniperStateEnum.created).eq(SniperPlan::getDeleted, 0));
        for (SniperPlan sniperPlan : sniperPlans) {
            List<SniperPlanWallet> sniperPlanWallets = sniperPlanWalletMapper.selectList(new LambdaQueryWrapper<SniperPlanWallet>().eq(SniperPlanWallet::getPlanId, sniperPlan.getId()));
            for (SniperPlanWallet sniperPlanWallet : sniperPlanWallets) {
                try {
                    handleTx(sniperPlan.getId(), sniperPlanWallet.getWalletId());
                } catch (Exception e) {
                    log.warn("newCoinScan error: {}", e.toString());
                }
            }
        }
    }

    //    @Scheduled(cron = "* * * * * ?")
//    public void task() {
//        executorService.submit(() -> {
//            try (RedisLock redisLock = new RedisLock(stringRedisTemplate, "RouteScanTask_task")) {
//                if (!redisLock._success()) return;
//                TokenBaseInfo main = tokenInfoService.getMain();
//                List<Long> tokenIdDistinct = sniperPlanMapper.getToken_idDistinct();
//                for (Long token_id : tokenIdDistinct) {
//                    TokenBaseInfo tokenBaseInfo = tokenBaseInfoMapper.selectById(token_id);
//                    NewCoin newCoin = newCoinMapper.selectOne(new LambdaQueryWrapper<NewCoin>()
//                            .eq(NewCoin::getBaseMint, tokenBaseInfo.getAddress())
//                            .eq(NewCoin::getQuoteMint, "So11111111111111111111111111111111111111112").last(" LIMIT 1"));
//                    if (newCoin != null && newCoin.getPoolOpenTime() <= (System.currentTimeMillis() / 1000L)) {
//                        // 检测到路由
//                        List<SniperPlan> sniperPlans = sniperPlanMapper.selectList(new LambdaQueryWrapper<SniperPlan>().eq(SniperPlan::getTokenId, token_id)
//                                .eq(SniperPlan::getState, SniperStateEnum.created).eq(SniperPlan::getDeleted, 0));
//                        for (SniperPlan sniperPlan : sniperPlans)
//                            executorService.submit(() -> sniper(sniperPlan, main, newCoin.getAmmKey()));
//                        sniperPlanMapper.update(null, new LambdaUpdateWrapper<SniperPlan>().set(SniperPlan::getState, "success").eq(SniperPlan::getTokenId, token_id)
//                                .eq(SniperPlan::getState, "created"));
//                    }
//                }
//            }
//        });
//    }

//    private void sniper(SniperPlan sniperPlan, TokenBaseInfo main, String ammKey) {
//        List<SniperPlanWallet> sniperPlanWallets = sniperPlanWalletMapper.selectList(new LambdaQueryWrapper<SniperPlanWallet>().eq(SniperPlanWallet::getPlanId, sniperPlan.getId()));
//        for (SniperPlanWallet sniperPlanWallet : sniperPlanWallets) {
//            UserWallet userWallet = userWalletMapper.selectById(sniperPlanWallet.getWalletId());
//            TokenBaseInfo tokenBaseInfo = tokenBaseInfoMapper.selectById(sniperPlan.getTokenId());


//            Message message = new Message().setAmmKey(ammKey)
//                    .setSecret(userWalletService.getPriKey(userWallet)).setUserPublicKey(userWallet.getAddress())
//                    .setPriorityFee(sniperPlan.getExtraGas().multiply(BigDecimal.TEN.pow(9)).longValue())
//                    .setFeeAccount(systemConfigRepository.value(SystemConfigConstant.TRADE_FEE_ADDRESS))
//                    .setFee(sniperPlan.getMainAmount().multiply(BigDecimal.TEN.pow(9))
//                            .multiply(new BigDecimal(systemConfigRepository.value(SystemConfigConstant.TRADE_FEE_RATE))).longValue())
//                    .setAmount(sniperPlan.getMainAmount().multiply(BigDecimal.TEN.pow(9)).longValue())
//                    .setInputMint(main.getAddress()).setOutputMint(tokenBaseInfo.getAddress())
//                    .setSlippageBps(sniperPlan.getSlippage().multiply(BigDecimal.TEN.pow(2)).intValue())
//                    .setSniper_plan_wallet_id(sniperPlanWallet.getId());

//            sniperPlanTxMapper.delete(new LambdaQueryWrapper<SniperPlanTx>().eq(SniperPlanTx::getPlanId, sniperPlan.getId())
//                    .eq(SniperPlanTx::getWalletId, userWallet.getId()));
//            sniperPlanTxMapper.insert(SniperPlanTx.builder().id(IdUtil.nextId())
//                    .uid(sniperPlan.getUid()).planId(sniperPlan.getId()).walletId(userWallet.getId()).build());
//            solQueueService.send(message);
//        }
//    }

    public void recreateTx(Long planId) {
        List<SniperPlanWallet> sniperPlanWallets = sniperPlanWalletMapper.selectList(new LambdaQueryWrapper<SniperPlanWallet>().eq(SniperPlanWallet::getPlanId, planId));
        // 先删除钱包的
        for (SniperPlanWallet sniperPlanWallet : sniperPlanWallets) {
            try {
                handleTx(planId, sniperPlanWallet.getWalletId());
            } catch (Exception e) {
                log.warn("newCoinScan error: {}", e.toString());
            }
        }
    }

//    public void removeTx(Long planId, Long walletId) {
//        sniperPlanTxMapper.delete(new LambdaQueryWrapper<SniperPlanTx>().eq(SniperPlanTx::getPlanId, planId)
//                .eq(SniperPlanTx::getWalletId, walletId).eq(SniperPlanTx::getSuccess, 0));
//    }

//    public void addTx(Long planId, Long walletId) {
//        try {
//            this.handleTx(planId, walletId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void removeCreatedTx(Long planId) {
        List<SniperPlanWallet> sniperPlanWallets = sniperPlanWalletMapper.selectList(new LambdaQueryWrapper<SniperPlanWallet>().eq(SniperPlanWallet::getPlanId, planId));
        for(SniperPlanWallet wallet: sniperPlanWallets) {
            try {
                this.removeCreatedTx(planId, wallet.getWalletId());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeCreatedTx(Long plan_id, Long wallet_id) throws Exception {
        String key = redisKeyGenerateTool.generateKey("RouteScanTask_handleTx_" + plan_id + "_" + wallet_id);
        try (RedisLock redisLock = new RedisLock(stringRedisTemplate, key)) {
            if (!redisLock._success()) return;
            BoundZSetOperations<String, String> ops = stringRedisTemplate.boundZSetOps(TX_QUEUE());
            SniperPlanTx sniperPlanTx = sniperPlanTxMapper.selectOne(new LambdaQueryWrapper<SniperPlanTx>().eq(SniperPlanTx::getPlanId, plan_id)
                    .eq(SniperPlanTx::getWalletId, wallet_id));
            if (sniperPlanTx != null && sniperPlanTx.getSuccess() == 1) return;
            if (sniperPlanTx != null) {
                sniperPlanTxMapper.deleteById(sniperPlanTx.getId());
                ops.remove(sniperPlanTx.getId().toString());
            }
        }
    }

    private void handleTx(Long plan_id, Long wallet_id) throws Exception {
        String key = redisKeyGenerateTool.generateKey("RouteScanTask_handleTx_" + plan_id + "_" + wallet_id);
        try (RedisLock redisLock = new RedisLock(stringRedisTemplate, key)) {
            if (!redisLock._success()) return;
            BoundZSetOperations<String, String> ops = stringRedisTemplate.boundZSetOps(TX_QUEUE());

            SniperPlanTx sniperPlanTx = sniperPlanTxMapper.selectOne(new LambdaQueryWrapper<SniperPlanTx>().eq(SniperPlanTx::getPlanId, plan_id)
                    .eq(SniperPlanTx::getWalletId, wallet_id));
            if (sniperPlanTx != null && sniperPlanTx.getSuccess() == 1) return;
            if (sniperPlanTx != null) {
                sniperPlanTxMapper.deleteById(sniperPlanTx.getId());
                ops.remove(sniperPlanTx.getId().toString());
            }
            SniperPlan sniperPlan = sniperPlanMapper.selectById(plan_id);
            if (sniperPlan == null) return;
            UserWallet userWallet = userWalletMapper.selectById(wallet_id);
            if (userWallet == null) return;
            TokenBaseInfo tokenBaseInfo = tokenBaseInfoMapper.selectById(sniperPlan.getTokenId());
            if (tokenBaseInfo == null) return;
            NewCoin newCoin = newCoinMapper.get(tokenBaseInfo.getAddress());
            if (newCoin == null) return;
            RayComputeAmountOut rayComputeAmountOut = raydiumOperator.computeAmountOut("So11111111111111111111111111111111111111112",
                    tokenBaseInfo.getAddress(), newCoin.getAmmKey(), sniperPlan.getMainAmount().multiply(BigDecimal.TEN.pow(9)).longValue(),
                    sniperPlan.getSlippage().multiply(BigDecimal.TEN.pow(2)).intValue());
            log.info("rayComputeAmountOut: {}", new Gson().toJson(rayComputeAmountOut));
            if (rayComputeAmountOut != null && StringUtils.hasLength(rayComputeAmountOut.getMinAmountOut())) {
                UserConfig config = userConfigRepository.getById(sniperPlan.getUid());
                String transaction = raydiumOperator.rayCalc(userWallet.getAddress(), sniperPlan.getExtraGas().multiply(BigDecimal.TEN.pow(9)).longValue()
                        , systemConfigRepository.value(SystemConfigConstant.TRADE_FEE_ADDRESS),
                        sniperPlan.getMainAmount().multiply(BigDecimal.TEN.pow(9))
                                .multiply(config.getFeeRate()).longValue(),
                        "So11111111111111111111111111111111111111112", tokenBaseInfo.getAddress(), newCoin.getAmmKey(),
                        sniperPlan.getMainAmount().multiply(BigDecimal.TEN.pow(9)).longValue(), Long.valueOf(rayComputeAmountOut.getMinAmountOut()));
                log.info("transaction: {}", transaction);
                Long nextId = IdUtil.nextId();
                sniperPlanTxMapper.insert(SniperPlanTx.builder().id(nextId)
                        .uid(sniperPlan.getUid()).planId(sniperPlan.getId()).walletId(userWallet.getId())
                        .tx(transaction).poolStartTime(newCoin.getPoolOpenTime()).success(0).build());
                sniperPlanMapper.update(null, new LambdaUpdateWrapper<SniperPlan>().set(SniperPlan::getState, "success").eq(SniperPlan::getId, plan_id)
                                .eq(SniperPlan::getState, "created"));
                long now = System.currentTimeMillis() / 1000L;
                long offsetSecond = Math.max(newCoin.getPoolOpenTime(), now) + 3600L - now;
                stringRedisTemplate.opsForValue().set(TX_INFO() + nextId, new Gson().toJson(new Message()
                                .setSniper_plan_tx_id(nextId).setTx(transaction).setPri_key(aesTool.decrypt(userWallet.getPriKey()))),
                        offsetSecond, TimeUnit.SECONDS);
                ops.add(nextId.toString(), newCoin.getPoolOpenTime());
            }
        }
    }


    @Resource
    private AesTool aesTool;

//    public static final String TX_INFO = "TX_INFO_";
    public String TX_INFO() {
        return redisKeyGenerateTool.generateCommonKey("TX_INFO_");
    }

    public String TX_QUEUE() {
        return redisKeyGenerateTool.generateCommonKey("TX_QUEUE");
    }

//    public static final String TX_QUEUE = "TX_QUEUE";
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private RaydiumOperator raydiumOperator;
    @Resource
    private SniperPlanTxMapper sniperPlanTxMapper;
    @Resource
    private NewCoinMapper newCoinMapper;
    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private UserWalletMapper userWalletMapper;
    @Resource
    private SniperPlanWalletMapper sniperPlanWalletMapper;
    @Resource
    private TokenBaseInfoMapper tokenBaseInfoMapper;
    @Resource
    private SniperPlanMapper sniperPlanMapper;


    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
}
