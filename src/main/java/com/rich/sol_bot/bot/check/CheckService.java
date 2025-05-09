package com.rich.sol_bot.bot.check;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.sol.Base58;
import com.rich.sol_bot.trade.service.TokenInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class CheckService {
    public static final Integer nameLength = 10;

    /**
     * 检查是否满足sol token地址的格式，也可以判断是否是用户地址
     * @param address
     * @return
     */
    public Boolean isTokenAddress(String address) {
        byte[] res = Base58.decode(address);
        return res != null && res.length == 32;
    }

    public Boolean checkTokenAddressPass(TokenBot bot, Long uid, String tokenAddress) {
        if(!this.isTokenAddress(tokenAddress)) {
            this.pushError(uid, bot, CheckError.tokenAddressLengthError);
            return false;
        }
        return true;
    }

    /**
     * 是否满足数字的格式
     * @param bot
     * @param uid
     * @param number
     * @param decimals
     * @return
     */
    public Boolean isNumberValid(TokenBot bot, Long uid, String number, Integer decimals) {
        if(!number.matches("\\d+") && !number.matches("-?\\d+(\\.\\d+)?")){
            this.pushError(uid, bot, CheckError.numberDecimalsError);
            return false;
        }
        if(new BigDecimal(number).scale() > decimals) {
            this.pushError(uid, bot, CheckError.numberDecimalsError);
            return false;
        }
        return true;
    }

    public Boolean isUserAddressValid(TokenBot bot, Long uid, String userAddress) {
        if(!this.isTokenAddress(userAddress)){
            this.pushError(uid, bot, CheckError.userAddressLengthError);
            return false;
        }
        return true;
    }

    /**
     * 名字长度校验
     * @param bot
     * @param uid
     * @param name
     * @return
     */
    public Boolean isNameValid(TokenBot bot, Long uid, String name) {
        if(name.length() > nameLength){
            this.pushError(uid, bot, CheckError.nameTooLong);
            return false;
        };
        return true;
    }

    /**
     * 私钥格式校验
     * @param bot
     * @param uid
     * @param priKey
     * @return
     */
    public Boolean isPriKeyValid(TokenBot bot, Long uid, String priKey) {
        byte[] res = Base58.decode(priKey);
        if(res == null || res.length != 64){
            this.pushError(uid, bot, CheckError.priKeyError);
            return false;
        }
        return true;
    }

    public void pushError(Long uid, TokenBot bot, String text) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(text)
                .build());
    }

    public Boolean isOverCount(TokenBot bot, Long uid, String number, Integer limit) {
        if(Long.parseLong(number) > limit) {
            this.pushError(uid, bot, CheckError.numberOverCountError);
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Resource
    private TokenInfoService tokenInfoService;
}
