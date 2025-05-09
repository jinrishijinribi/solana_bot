package com.rich.sol_bot.bot.handler.constants;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class BotReplyScraperConstants {

    public static final String scraperPrefix = "scraper:";

    public static final String scraperNewTaskText = "\uD83D\uDD2A添加新任务";
    public static final String scraperNewTaskCli = scraperPrefix + "scraperNewTaskCli";
    public static final String scraperStartImportNameMsg = """
            ⚙️输入您想要刮刀的推特名（只需输入@后面部分）
            """;
    public static final String scraperStartTwitterErrMsg = """
            错误：未搜索到此推特名，请重新输入
            ⚙\uFE0F输入您想要刮刀的推特名（只需输入@后面部分）""";
    public static final String scraperStartInputAmountMsg = "请输入金额，输入0.1代表买入0.1 SOL，输入后将创建挂刀任务";

    public static final String scraperSendSuccess = """
            ✅刮刀任务%s-交易已发起
            \uD83C\uDD94推特ID：%s
            代币名称：%s
            合约地址：%s
            当前任务触发次数：%s
            """;


    public static final String scraperRefreshText = "刷新";
    public static final String scraperRefreshCliPrefix = scraperPrefix + "scraperRefreshCliPrefix";
    public static final String scraperExtraGasText = "贿赂gas费";
    public static final String scraperExtraGasCli = scraperPrefix + "scraperExtraGasCli";
    public static final String scraperCountText = "执行次数";
    public static final String scraperCountCli = scraperPrefix + "scraperCountCli";

    public static final String scraperCountImportMsg = "请输入刮刀任务执行次数，输入5代表执行成功5次后自动删除任务，输入0代表不限次数，默认为1次";


    public static final String scraperSetWalletCli = scraperPrefix + "scraperSetWalletCli";
    public static final String scraperSelectTaskCli = scraperPrefix + "scraperSelectTaskCli";


    public static final String scraperFastModeText = "极速模式";
    public static final String scraperFastModeCli = scraperPrefix + "scraperFastModeCli";
    public static final String scraperProtectModeText = "防夹模式";
    public static final String scraperProtectModeCli = scraperPrefix + "scraperProtectModeCli";



    public static final String scraperAmountContent = "- - 单次买入金额 - -";
    public static final List<String> amountList = Arrays.asList("0.1", "0.3", "0.5", "1.0", "2.0", "x");
    public static final String scraperBuyPrefix = scraperPrefix + "buy";
    public static String generateDealBuyCli(Long taskId, String i) {
        return scraperBuyPrefix + taskId + ":" + i;
    }
    public static String generateDealBuyTextPrefix = "买 ";
    public static String generateDealBuyText(String i) {
        return i + " SOL";
    }

    public static final String scraperRmTaskText = "❌删除任务";
    public static final String scraperRmTaskCli = scraperPrefix + "scraperRmTaskCli";

    public static String isModeOn(Boolean isOn) {
        if(isOn) {
            return "✅";
        } else {
            return "❌";
        }
    }

    public static String isSelected(Boolean isOn) {
        if(isOn) {
            return "✅";
        } else {
            return "";
        }
    }

    public static final String scraperNoTask= """
            
            🔪暂无刮刀任务
            
            """;
    public static final String scraperTaskInfoTemplate = """
            \uD83D\uDD2A 刮刀任务 %s
            
            \uD83C\uDD94 推特ID：%s
            ✅ 刮刀成功次数：%s
            
            \uD83D\uDC5B 钱包余额：%s SOL
            \uD83D\uDD22 刮刀执行次数：%s
            \uD83D\uDCB0 单次买入金额：%s SOL
            """;

}
