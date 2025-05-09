package com.rich.sol_bot.trade.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.bot.BotManager;
import com.rich.sol_bot.gecko.GeckoOperator;
import com.rich.sol_bot.gecko.gecko_entity.GeckoOHLCVData;
import com.rich.sol_bot.gecko.gecko_entity.GeckoOHLCVParam;
import com.rich.sol_bot.sol.BirdeyeService;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.dto.OHLCVTDTO;
import com.rich.sol_bot.trade.dto.ProfitAndLoseDTO;
import com.rich.sol_bot.trade.enums.OHLCVTypeEnum;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.trade.mapper.*;
import com.rich.sol_bot.trade.params.OHLCVTParam;
import com.rich.sol_bot.user.balance.UserBalanceService;
import com.rich.sol_bot.wallet.WalletBalanceStatService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 收益图相关
 */
@Service
public class TradeProfitService {

    @Autowired
    private GeckoOperator geckoOperator;
    @Autowired
    private TokenPoolInfoRepository tokenPoolInfoRepository;

    public ProfitAndLoseDTO generateViewFromGecko(Long uid, Long tokenId, Long walletId) {
        UserWallet userWallet = userWalletRepository.getById(walletId);
        TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getById(tokenId);
        tokenBaseInfo = tokenInfoService.geneTokenBaseInfo(tokenBaseInfo.getAddress());
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        String image = tokenBaseInfo.getImage();

        WalletBalanceStat stat = walletBalanceStatService.getBalanceStatAndSync(uid, userWallet, tokenBaseInfo, true);
//        WalletBalanceStat stat = walletBalanceStatService.getBalanceStatFromCache(uid, userWallet, tokenBaseInfo, true);

        if(stat == null) return null;
        // 买卖的点位
        List<TradeInfo> result = tradeInfoRepository.list(new LambdaQueryWrapper<TradeInfo>()
                .eq(TradeInfo::getUid, uid).eq(TradeInfo::getWalletId, walletId)
                .eq(TradeInfo::getState, TradeStateEnum.success)
                .ge(TradeInfo::getCreatedAt, TimestampUtil.minus(7, TimeUnit.DAYS))
                .and(o -> o.eq(TradeInfo::getTokenInId, tokenId).or().eq(TradeInfo::getTokenOutId, tokenId))
                .orderByAsc(TradeInfo::getCreatedAt)
        );
        Integer size = result.size();
        Long start = TimestampUtil.minus(1, TimeUnit.DAYS).getTime() / 1000;
        Long end = TimestampUtil.now().getTime() / 1000;
        if(size == 1) {
            start = result.get(0).getCreatedAt().getTime() / 1000;
            end = TimestampUtil.now().getTime() / 1000;
        }
        if(size > 1) {
            start = result.get(0).getCreatedAt().getTime() / 1000;
            end = result.get(size - 1).getCreatedAt().getTime() / 1000;
        }
        OHLCVTypeEnum type = OHLCVTypeEnum.calculateOne(start, end, 50L);
        start = (start / type.getSecond() - 2) * type.getSecond();
        end = (end / type.getSecond() + 2) * type.getSecond();

        TokenBaseInfo finalTokenBaseInfo = tokenBaseInfo;
        List<Long> buyPoint = result.stream().filter(o -> o.getTokenInId().equals(finalTokenBaseInfo.getId())).map(o -> (o.getCreatedAt().getTime() / 1000 / type.getSecond()) * type.getSecond()).toList();
        List<Long> sellPoint = result.stream().filter(o -> o.getTokenOutId().equals(finalTokenBaseInfo.getId())).map(o -> (o.getCreatedAt().getTime() / 1000 / type.getSecond()) * type.getSecond()).toList();

        TokenPoolInfo tokenPoolInfo = tokenPoolInfoRepository.getBiggestPoolByBaseMint(tokenBaseInfo.getAddress(), tokenMain.getAddress());
//        GeckoOHLCVData data = geckoOperator.getPHLCVData(GeckoOHLCVParam.builder()
//                .limit(50).beforeTimestamp(end).poolAddress(tokenPoolInfo.getAmmKey())
//                .timeframe(type.getTimeframe()).aggregate(type.getAggregate())
//                .build());
//
//        List<Long> x = data.getAttributes().getOhlcvList().stream().map(o -> o.get(0).setScale(0, RoundingMode.HALF_DOWN).longValue()).toList();
//        List<String> y = data.getAttributes().getOhlcvList().stream().map(o -> o.get(4).stripTrailingZeros().toPlainString()).toList();


        BigDecimal buyAmount = result.stream().filter(o -> o.getTokenInId().equals(finalTokenBaseInfo.getId()))
                .map(TradeInfo::getTokenOutAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sellAmount = result.stream().filter(o -> o.getTokenOutId().equals(finalTokenBaseInfo.getId()))
                .map(TradeInfo::getTokenInAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal nowSol = stat.getAmount().multiply(new BigDecimal(tokenBaseInfo.getPrice()))
                .divide(new BigDecimal(tokenMain.getPrice()), RoundingMode.HALF_DOWN);
//        BigDecimal profit = nowSol.subtract(stat.getVal());
        BigDecimal profit = sellAmount.add(nowSol).subtract(buyAmount);

        BigDecimal rate = BigDecimal.ZERO;
//        if(stat.getVal().compareTo(BigDecimal.ZERO) > 0) {
//            rate = profit.divide(stat.getVal(), RoundingMode.HALF_DOWN);
//        }
        if(buyAmount.compareTo(BigDecimal.ZERO) > 0) {
            rate = profit.divide(buyAmount, RoundingMode.HALF_DOWN);
        }

        return ProfitAndLoseDTO.builder()
                .x(new ArrayList<>()).y(new ArrayList<>())
                .sellPoint(sellPoint).buyPoint(buyPoint)
                .buyAmount(buyAmount).sellAmount(sellAmount)
                .holdAmount(nowSol).token(tokenBaseInfo.getSymbol())
                .profit(profit).profitLostRate(rate)
                .image(image)
                .imageBase64(this.getFromUrl(image))
                .build();
    }




//    public ProfitAndLoseDTO generateView(Long uid, Long tokenId, Long walletId) {
//        UserWallet userWallet = userWalletRepository.getById(walletId);
//        TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getById(tokenId);
//        tokenBaseInfo = tokenInfoService.geneTokenBaseInfo(tokenBaseInfo.getAddress());
//        TokenBaseInfo tokenMain = tokenInfoService.getMain();
//        String image = tokenInfoService.getImage(tokenBaseInfo);
//
//        WalletBalanceStat stat = walletBalanceStatService.getBalanceStatAndSync(uid, userWallet, tokenBaseInfo, true);
////        WalletBalanceStat stat = walletBalanceStatService.getBalanceStatFromCache(uid, userWallet, tokenBaseInfo, true);
//
//        if(stat == null) return null;
//        // 买卖的点位
//        List<TradeInfo> result = tradeInfoRepository.list(new LambdaQueryWrapper<TradeInfo>()
//                .eq(TradeInfo::getUid, uid).eq(TradeInfo::getWalletId, walletId)
//                .eq(TradeInfo::getState, TradeStateEnum.success)
//                .ge(TradeInfo::getCreatedAt, TimestampUtil.minus(7, TimeUnit.DAYS))
//                .and(o -> o.eq(TradeInfo::getTokenInId, tokenId).or().eq(TradeInfo::getTokenOutId, tokenId))
//                .orderByAsc(TradeInfo::getCreatedAt)
//        );
//        Integer size = result.size();
//        Long start = TimestampUtil.minus(1, TimeUnit.DAYS).getTime() / 1000;
//        Long end = TimestampUtil.now().getTime() / 1000;
//        if(size == 1) {
//            start = result.get(0).getCreatedAt().getTime() / 1000;
//            end = TimestampUtil.now().getTime() / 1000;
//        }
//        if(size > 1) {
//            start = result.get(0).getCreatedAt().getTime() / 1000;
//            end = result.get(size - 1).getCreatedAt().getTime() / 1000;
//        }
//        OHLCVTypeEnum type = OHLCVTypeEnum.calculateOne(start, end, 50L);
//        start = (start / type.getSecond() - 2) * type.getSecond();
//        end = (end / type.getSecond() + 2) * type.getSecond();
//
//        TokenBaseInfo finalTokenBaseInfo = tokenBaseInfo;
//        List<Long> buyPoint = result.stream().filter(o -> o.getTokenInId().equals(finalTokenBaseInfo.getId())).map(o -> (o.getCreatedAt().getTime() / 1000 / type.getSecond()) * type.getSecond()).toList();
//        List<Long> sellPoint = result.stream().filter(o -> o.getTokenOutId().equals(finalTokenBaseInfo.getId())).map(o -> (o.getCreatedAt().getTime() / 1000 / type.getSecond()) * type.getSecond()).toList();
//
//        OHLCVTDTO data = birdeyeService.getOHLCVInfo(OHLCVTParam.builder()
//                .timeFrom(start).timeTo(end).type(type).address(tokenBaseInfo.getAddress())
//                .build());
//        List<Long> x = data.getData().getItems().stream().map(OHLCVTDTO.Item::getUnixTime).toList();
//        List<String> y = data.getData().getItems().stream().map(OHLCVTDTO.Item::getC).toList();
//        BigDecimal buyAmount = result.stream().filter(o -> o.getTokenInId().equals(finalTokenBaseInfo.getId()))
//                .map(TradeInfo::getTokenOutAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal sellAmount = result.stream().filter(o -> o.getTokenOutId().equals(finalTokenBaseInfo.getId()))
//                .map(TradeInfo::getTokenInAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        BigDecimal nowSol = stat.getAmount().multiply(new BigDecimal(tokenBaseInfo.getPrice()))
//                .divide(new BigDecimal(tokenMain.getPrice()), RoundingMode.HALF_DOWN);
////        BigDecimal profit = nowSol.subtract(stat.getVal());
//        BigDecimal profit = sellAmount.add(nowSol).subtract(buyAmount);
//
//        BigDecimal rate = BigDecimal.ZERO;
////        if(stat.getVal().compareTo(BigDecimal.ZERO) > 0) {
////            rate = profit.divide(stat.getVal(), RoundingMode.HALF_DOWN);
////        }
//        if(buyAmount.compareTo(BigDecimal.ZERO) > 0) {
//            rate = profit.divide(buyAmount, RoundingMode.HALF_DOWN);
//        }
//
//        return ProfitAndLoseDTO.builder()
//                .x(x).y(y)
//                .sellPoint(sellPoint).buyPoint(buyPoint)
//                .buyAmount(buyAmount).sellAmount(sellAmount)
//                .holdAmount(nowSol).token(tokenBaseInfo.getSymbol())
//                .profit(profit).profitLostRate(rate)
//                .image(image)
//                .imageBase64(this.getFromUrl(image))
//                .build();
//    }

    public void sendView(Long uid, String base64Data) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        InputFile inputFile = new InputFile(new ByteArrayInputStream(imageBytes), "photo.jpg");
        botManager.pushPhoto(uid, inputFile);
    }

    public String getFromUrl(String imageUrl) {
        String base64Image = "";
        if(imageUrl == null) return base64Image;
        try {
            // 从URL获取图片
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            // 将图片转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            // 将字节数组编码为Base64
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }


    @Resource
    private BirdeyeService birdeyeService;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private TradeInfoRepository tradeInfoRepository;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private WalletBalanceStatService walletBalanceStatService;
    @Resource
    private BotManager botManager;
}
