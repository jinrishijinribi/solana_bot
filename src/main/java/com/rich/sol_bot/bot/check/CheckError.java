package com.rich.sol_bot.bot.check;


public class CheckError {
    public static final String tokenAddressLengthError = "合约地址不正确, 请重新输入";
    public static final String numberDecimalsError = "输入的格式错误，请重新输入";
    public static final String userAddressLengthError = "用户地址不正确, 请重新输入";
    public static final String nameTooLong = "名字过长, 请重新输入";
    public static final String priKeyError = "私钥格式不正确, 请重新输入";
    public static final String withdrawRebateFail = "❌ 提现失败，可提现余额需大于0.01 SOL";
    public static final String tradeAmountNotEnough = "余额不足，请先充值到此钱包或切换钱包";
    public static final String alLeastOneWallet = "❌请至少选择一个钱包";
    public static final String transferSolNotEnough = "❌SOL余额不足，转出SOL失败";
    public static final String numberOverCountError = "❌交易次数最大可输入10，请重新输入";
}
