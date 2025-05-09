package com.rich.sol_bot.bot.handler.constants;

import java.util.Arrays;
import java.util.List;

public class BotReplySniperConstants {
    public static final String sniperPrefix = "sniper:";
    public static final String sniperInputTokenContent = "⚙️输入您想要狙击的合约地址\n";
    public static final String sniperInputAmountContent = """
            ⚙️输入每个钱包允许的最大支出
            允许输入的SOL的格式为0.00，最多输入两位有效小数""";
    public static final String sniperInputExtraGasContent = """
            ⚙️输入想要支付的自动狙击gas费
            含义：额外支付多少SOL 贿赂节点以更快的完成交易，用于自动狙击，最小输入为0.01
            允许输入的SOL的格式为0.00，最多输入两位有效小数
            """;

    public static String existButton(Boolean exist) {
        if(exist) return "\uD83D\uDFE2";
        else return "\uD83D\uDD34";
    }

    public final static String sniperCreateSuccessKeyBoardHead = """
            \uD83D\uDD2B *自动狙击*-%s
            合约地址：`%s`
            """;


    public final static String sniperKeyboardRefreshText = "刷新";
    public final static String sniperKeyboardRefreshCli = sniperPrefix + "main_refresh";
    public final static String sniperSlippageFastModeText = "滑点-极速模式";
    public final static String sniperSlippageFastModeCli = sniperPrefix + "slippage_mode";
    public final static String sniperSlippageProtectModeText = "滑点-防夹模式";
    public final static String sniperSlippageProtectModeCli = sniperPrefix + "protect_mode";
    public final static String sniperExtraGasText = "贿赂gas费";
    public final static String sniperExtraGasCli = sniperPrefix + "extra_gas";
    public final static String sniperDeleteText = "删除此狙击";
    public final static String sniperDeleteCli = sniperPrefix + "to_delete";

    public final static String sniperListNameTextList = "自动狙击列表\n\n";
    public final static String sniperListNameText = "自动狙击";
    public final static String sniperListNameCliPrefix = sniperPrefix + "list_tab:";

    public final static String sniperWalletBindPrefix = sniperPrefix + "sniperWalletBind";
    public static String sniperBindButton(Boolean bind) {
        if(bind) return "\uD83D\uDFE2 ";
        return "\uD83D\uDD34 ";
    }

    public final static String buyPrefix = sniperPrefix + "buy:";

    public static String generateDealBuyText = "买 ";

    public static String generateDealBuyText(String i) {
        return i + " SOL";
    }
    public static String generateDealBuyCli(Long planId, String i) {
        return buyPrefix + planId + ":" + i;
    }
    public static final String unKnowAmount = "x";
    public static final List<String> sniperBuyAmountList = Arrays.asList("0.1", "0.3", "0.5", "1.0", "2.0", unKnowAmount);

    public static final String setFastModeSuccess = "✅设置极速模式成功";
    public static final String setProtectModeSuccess = "✅设置防夹模式成功";
    public static final String deleteSnapPlanSuccess = "✅删除狙击配置成功";
    public static final String chooseAtLeastWallet = "❌请至少选择一个钱包";
    public static final String sniperCreatedSuccess = "✅自动狙击设置成功";

    public static final String sniperNewSettingCli = sniperPrefix + "sniperNewSettingCli";

    public static final String sniperPumpLaunchContent = """
            \uD83D\uDE80PUMP %s - %s
            合约地址: `%s`
            
            \uD83D\uDCDD 池子归属：RAY\s
            \uD83D\uDCB8 代币价格：%s USDT
            \uD83D\uDC8E持仓价值：%s SOL""";


}
