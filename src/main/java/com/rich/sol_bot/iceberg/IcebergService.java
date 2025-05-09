package com.rich.sol_bot.iceberg;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.iceberg.mapper.IcebergTask;
import com.rich.sol_bot.iceberg.mapper.IcebergTaskMapper;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.iceberg.mapper.IcebergRepository;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IcebergService {

    public boolean icebergCheck(TradeInfo i) {
        if(i.getIceId() == null) return false;
        IcebergTask task = icebergRepository.getById(i.getIceId());
        if(task == null) return false;
        if(task.getTokenAccount() != null) return true;
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        TokenBaseInfo tokenBaseInfo = null;
        if(mainToken.getId().equals(i.getTokenOutId())){
            tokenBaseInfo = tokenBaseInfoRepository.getById(i.getTokenInId());
        } else {
            tokenBaseInfo = tokenBaseInfoRepository.getById(i.getTokenOutId());
        }
        UserWallet userWallet = userWalletRepository.ownedByUid(i.getWalletId(), i.getUid());
        // 从这个判断，成功了一笔之后，可以继续
        String tokenAccount = solQueryService.tokenAccount(tokenBaseInfo.getAddress(), userWallet.getAddress());
        if(tokenAccount != null) {
            icebergRepository.update(new LambdaUpdateWrapper<IcebergTask>().set(IcebergTask::getTokenAccount, tokenAccount).eq(IcebergTask::getId, task.getId()));
            return true;
        }
        // 失败相当于重新提交交易
        if(task.getFailCount().equals(task.getSubmitCount())) {
            return true;
        }
        return false;
    }

    public IcebergTask generateTask(Long uid, Long count) {
        IcebergTask icebergTask = IcebergTask.builder()
                .id(IdUtil.nextId()).uid(uid)
                .allCount(count).successCount(0L).failCount(0L).submitCount(0L).createdAt(TimestampUtil.now())
                .build();
        icebergRepository.save(icebergTask);
        return icebergTask;
    }

    public void successOne(Long id) {
        icebergTaskMapper.successOne(id);
    }

    public void failOne(Long id) {
        icebergTaskMapper.failOne(id);
    }

    public IcebergTask getById(Long id) {
        return icebergRepository.getById(id);
    }

    @Resource
    private IcebergRepository icebergRepository;
    @Resource
    private IcebergTaskMapper icebergTaskMapper;
    @Autowired
    private TokenInfoService tokenInfoService;
    @Autowired
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private UserWalletRepository userWalletRepository;
    @Autowired
    private SolQueryService solQueryService;
}
