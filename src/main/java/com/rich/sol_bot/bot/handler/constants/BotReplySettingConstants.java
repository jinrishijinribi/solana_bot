package com.rich.sol_bot.bot.handler.constants;

public class BotReplySettingConstants {
    public static final String settingStart = """
            极速模式滑点：%s
            防夹模式滑点：%s
            买入优先费：%s SOL
            卖出优先费：%s SOL
            狙击优先费: %s SOL
            默认钱包: %s""";

    public static final String settingPrefix = "setting:";
    public static final String settingFastSlippageText = "设置滑点-极速模式";
    public static final String settingFastSlippageCli = settingPrefix + "settingSlippageCli";
    public static final String settingProtectSlippageText = "设置滑点-防夹模式";
    public static final String settingProtectSlippageCli = settingPrefix + "settingProtectSlippageCli";
    public static final String settingBuyGasText = "买入gas优先费";
    public static final String settingBuyGasCli = settingPrefix + "settingBuyGasCli";
    public static final String settingSellGasText = "卖出gas优先费";
    public static final String settingSellGasCli = settingPrefix + "settingSellGasCli";
    public static final String settingSniperGasText = "狙击gas优先费";
    public static final String settingSniperGasCli = settingPrefix + "settingSniperGasCli";
    public static final String settingJitoGasText = "防夹模式优先费";
    public static final String settingJitoGasCli = settingPrefix + "settingJitoGasCli";
    public static final String settingPreferWalletText = "设置默认钱包";
    public static final String settingPreferWalletCli = settingPrefix + "settingPreferWalletCli";
    public static final String settingAutoSellText = "自动卖出";
    public static final String settingAutoSellCli = settingPrefix + "settingAutoSellCli";
    public static final String settingAutoSellContent = """
            开启自动卖出后，系统将根据您设置的参数自动为您买入的全部代币添加限价卖单
            您可以自由配置涨跌幅和卖出比例。如果多次买入相同代币，系统会生成多个配置挂单，您可以在主菜单的“已配置挂单”中手动撤销
            
            """;
    public static final String settingAutoSellCancelCliPrefix = settingPrefix + "settingAutoSellCancelCliPrefix";
    public static final String settingAutoSellSwitchText = "已开启";
    public static final String settingAutoSellSwitchCli = settingPrefix + "settingAutoSellSwitchCli";
    public static final String settingAutoSellConfigText = "添加挂单配置";
    public static final String settingAutoSellConfigCli = settingPrefix + "settingAutoSellConfigCli";

    public static final String settingAutoSellConfig = """
            请输入涨跌幅和自动卖出比例，以逗号分隔。
            例如：
            输入50,100 代表价格涨50%卖100%
            输入-50,100代表价格跌50%卖100%""";


    public static final String settingForFast = """
            请输入滑点
            极速模式，建议10-20，不低于5
            防夹模式，建议20-50，保证成功率""";
    public static final String settingForProtect = """
            请输入滑点
            极速模式，建议10-20，不低于5
            防夹模式，建议20-50，保证成功率""";
    public static final String settingForBuyGas = """
            请输入买入优先费（决定交易速度），范围0-1 SOL
            当前优先费%s SOL
            普通交易建议0.003 SOL，快速交易建议0.005 SOL，极速建议0.01 SOL甚至更高""";
    public static final String settingForSellGas = """
            请输入卖出优先费（决定交易速度），范围0-1 SOL
            当前优先费%s SOL
            普通交易建议0.003 SOL，快速交易建议0.005 SOL，极速建议0.01 SOL甚至更高""";
    public static final String settingForSniper = """
            请输入狙击优先费（决定交易速度），范围0-1 SOL
            当前优先费%s SOL""";
    public static final String settingForJito = """
            请输入防夹优先费（决定交易速度），范围0-1 SOL
            当前优先费%s SOL""";
    public static final String settingPreferWalletHead = "⚙️设置默认钱包\n";
    public static final String settingPreferWalletPrefix = settingPrefix + "settingPreferWalletPrefix";
    public static String showButton(Boolean show) {
        if(show) return "✅";
        else return "";
    }
    public static final String isModeOnText = "✅ 已开启自动卖出";
    public static final String isModeOffText = "❌ 已关闭自动卖出";

    public static String isModeOn(boolean isOn) {
        if(isOn) return isModeOnText;
        else return isModeOffText;
    }

    public static final String settingPreferWalletReturnText = "返回";
    public static final String settingPreferWalletReturnCli = settingPrefix + "settingPreferWalletReturnCli";
    public static final String settingLanguage = settingPrefix + "settingLanguage";


}
