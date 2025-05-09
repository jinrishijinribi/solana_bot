package com.rich.sol_bot.trade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.bot.handler.BotSniperHandler;
import com.rich.sol_bot.bot.service.BotNotifyService;
import com.rich.sol_bot.sol.Base58;
import com.rich.sol_bot.sol.BirdeyeService;
import com.rich.sol_bot.sol.entity.MintAccount;
import com.rich.sol_bot.sol.entity.RaydiumMint;
import com.rich.sol_bot.sol.entity.TokenSecurity;
import com.rich.sol_bot.sol.entity.Transaction;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoin;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoinRepository;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.trade.dto.OHLCVTDTO;
import com.rich.sol_bot.trade.dto.ProfitAndLoseDTO;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.mapper.TradeInfoRepository;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.params.OHLCVTParam;
import com.rich.sol_bot.trade.params.UploadBase64Param;
import com.rich.sol_bot.trade.service.*;
import com.rich.sol_bot.user.withdraw.WithdrawSubmitService;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLog;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Tag(name = "交易")
@RestController
@RequestMapping("/api/token")
public class TradeController {

    @Autowired
    private NewCoinRepository newCoinRepository;
    @Autowired
    private BotSniperHandler botSniperHandler;

    @Operation(summary = "token 信息")
    @GetMapping("/info")
    public TokenBaseInfo encode(String address) {
        return tokenInfoService.geneTokenBaseInfo(address);
    }

    @Operation(summary = "获取主币价格")
    @GetMapping("/main/px")
    public BigDecimal mainPx() {
        return tokenPxService.calculateMain();
    }

    @Operation(summary = "获取new-coin")
    @GetMapping("/new/coin")
    public Object getNewCoin(String address) {
        NewCoin newCoin = newCoinRepository.getOne(new LambdaQueryWrapper<NewCoin>()
                .eq(NewCoin::getBaseMint, address).or().eq(NewCoin::getQuoteMint, address).last("limit 1")
        );
        return newCoin;
    }

//    @Operation(summary = "token security")
//    @GetMapping("/security")
//    public TokenSecurity security(String address) {
//        try {
//            return birdeyeService.tokenSecurity(address);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Operation(summary = "mint_account")
    @GetMapping("/mint_account")
    public MintAccount mintAccount(String address) {
        try {
            return solQueryService.mintAccount(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Operation(summary = "raydium_mint")
    @GetMapping("/raydium_int")
    public RaydiumMint raydiumMint(String address) {
        try {
            return solQueryService.raydiumMint(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Operation(summary = "base58_check")
    @GetMapping("/base58_check")
    public Integer base58Check(String address) {
        byte[] re = Base58.decode(address);
        return re.length;
    }



    @Operation(summary = "提交交易")
    @GetMapping("/submit")
    public TradeInfo submitTrade(Long id) {
        try {
            TradeInfo tradeInfo = tradeInfoRepository.getById(id);
            return tradeSubmitService.submitTradeToPump(tradeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Operation(summary = "查询交易")
//    @GetMapping("/queryTx")
//    public Boolean queryTx(Long id) {
//        try {
//            TradeInfo tradeInfo = tradeInfoRepository.getById(id);
//            return tradeSubmitService.confirmTrade(tradeInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Operation(summary = "查询transaction")
    @GetMapping("/transaction")
    public Transaction transaction(String txid) {
        try {
            return solQueryService.getTransaction(txid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Operation(summary = "获取成交数据")
//    @PostMapping("/OHLCVInfo")
//    public OHLCVTDTO transaction(@RequestBody OHLCVTParam param) {
//        try {
//            return birdeyeService.getOHLCVInfo(param);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Operation(summary = "获取收益图数据")
    @GetMapping("/profit/map")
    public ProfitAndLoseDTO profitMap(Long uid, Long tokenId, Long walletId) {
        try {
            return tradeProfitService.generateViewFromGecko(uid, tokenId, walletId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Operation(summary = "上传收益图")
    @PostMapping("/update/base64")
    public Boolean uploadData(@RequestBody UploadBase64Param param) {
        ThreadAsyncUtil.execAsync(() -> {
            tradeProfitService.sendView(param.getUid(), param.getBase64Data());
        });
        return true;
    }

    @Operation(summary = "pump发射")
    @GetMapping("/pump/launch")
    public Boolean pumpLaunch(String address) {
        botSniperHandler.pumpLaunch(address);
        return true;
    }

//    @Operation(summary = "主币转账")
//    @GetMapping("/main/transfer")
//    public String mainTransfer(Long id) {
//        try {
//            UserWithdrawLog withdrawLog = userWithdrawLogRepository.getById(id);
//            return withdrawSubmitService.submitRebateTransfer(withdrawLog);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    @Operation(summary = "主币转账")
//    @GetMapping("/bot/notify")
//    public String botNotify(Long id) {
//        try {
//            TradeInfo tradeInfo = tradeInfoRepository.getById(id);
//            botNotifyService.pushSuccessTrade(tradeInfo, null);
//            return "success";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Operation(summary = "获取代币地址")
    @GetMapping("/token/account")
    public Object tokenAccount(String mint, String address) {
        return solQueryService.tokenBalance(mint, address);
    }

    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private BirdeyeService birdeyeService;
    @Resource
    private TradeSubmitService tradeSubmitService;
    @Resource
    private TradeInfoRepository tradeInfoRepository;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private WithdrawSubmitService withdrawSubmitService;
    @Resource
    private UserWithdrawLogRepository userWithdrawLogRepository;
    @Resource
    private BotNotifyService botNotifyService;
    @Resource
    private TradeProfitService tradeProfitService;
    @Resource
    private TokenPxService tokenPxService;
}
