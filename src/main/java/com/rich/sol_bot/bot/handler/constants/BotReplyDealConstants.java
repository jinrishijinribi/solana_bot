package com.rich.sol_bot.bot.handler.constants;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class BotReplyDealConstants {


    // deal
    public static final String dealTokenToChoose = "⚙️输入您想要买/卖的合约地址";
    public static final String dealPrefix = "deal:";
    public static final String dealRefreshCli = dealPrefix + "dealRefreshCli";
    public static final String dealPinRefreshText = "刷新";
    public static final String dealPinRefreshCli = dealPrefix + "dealPinRefreshCli";
    public static final String dealFastModeText = "极速模式";
    public static final String dealFastModeCli = dealPrefix + "dealFastModeCli";
    public static final String dealProtectModeText = "防夹模式";
    public static final String dealProtectModeCli = dealPrefix + "dealSlippageModeCli";
    public final static String dealToShowPLText = "生成收益图";
    public final static String dealToShowPLCli = dealPrefix + "show_pl";
    public final static String dealLimitText = "挂单";
    public final static String dealLimitCli = dealPrefix + "limit";
    public final static String dealToIcebergText = "冰山策略（强力防夹，大额订单拆分）";
    public final static String dealToIcebergCli = dealPrefix + "iceberg";
    public final static String dealToIcebergBuyText = "冰山策略买入";
    public final static String dealToIcebergBuyCli = dealPrefix  + "iceberg" + "buy";
    public final static String dealToIcebergSellText = "冰山策略卖出";
    public final static String dealToIcebergSellCli = dealPrefix + "iceberg" + "sell";
    public final static String dealToIcebergReturnText= "返回";
    public final static String dealToIcebergReturnCli = dealPrefix + "iceberg" + "return";
    public static final String unknownAmount = "x";
    public static final List<String> dealBuyAmountList = Arrays.asList("0.1", "0.3", "0.5", "1.0", "2.0", unknownAmount);
    public static final List<String> dealSellAmountList = Arrays.asList("50", "100", unknownAmount);
    public static final List<String> dealSellAmountValueList = Arrays.asList("0.5", "1", "x");

    public static final String sellPrefix = "sell";
    public static final String buyPrefix = "buy";
    public static final String dealWalletPrefix = dealPrefix + "wallet:";
    public static final String dealTokenPrefix = dealPrefix + "token_select:";
    public static final String dealTokenPagePreText = "上一页";
    public static final String dealTokenPagePreCli = dealPrefix + "page_pre";
    public static final String dealTokenPageNexText = "下一页";
    public static final String dealTokenPageNexCli = dealPrefix + "page_next";

    public static final String dealConfirmPrefix = dealPrefix + "confirm";
    public final static String dealConfirmSuccessText = "确定下单";
    public final static String dealConfirmSuccessCli = dealConfirmPrefix + "dealConfirmSuccessCli";
    public final static String dealConfirmCancelText = "取消";
    public final static String dealConfirmCancelCli = dealConfirmPrefix + "dealConfirmCancelCli";

    // 限价单
    public static final String dealLimitFastModeText = "极速模式";
    public static final String dealLimitFastModeCli = dealPrefix + "dealLimitFastModeCli";
    public static final String dealLimitProtectModeText = "防夹模式";
    public static final String dealLimitProtectModeCli = dealPrefix + "dealLimitProtectModeCli";
    public final static String dealLimitPxBuyText = "限价委托买入";
    public final static String dealLimitPxBuyCli = dealPrefix + "dealLimitPxBuyCli";
    public final static String dealLimitPxSellText = "限价委托卖出";
    public final static String dealLimitPxSellCli = dealPrefix + "dealLimitPxSellCli";
    public final static String dealLimitRateBuyText = "涨跌幅买入";
    public final static String dealLimitRateBuyCli = dealPrefix + "dealLimitRateBuyRate";
    public final static String dealLimitRateSellText = "涨跌幅卖出";
    public final static String dealLimitRateSellCli = dealPrefix + "dealLimitRateSellRate";
    public final static String dealLimitListOrdersText = "查看此币种挂单";
    public final static String dealLimitListOrdersCli = dealPrefix + "dealLimitListOrdersCli";
    public final static String dealLimitReturnText = "返回";
    public final static String dealLimitReturnCli = dealPrefix + "dealLimitReturnCli";
    public final static String dealLimitPxBuyContent = """
            请输入限定价格和买入金额，以逗号分隔。
            例如：输入0.001,0.1 代表价格达到 0.001 USDT，自动买入0.1 SOL
            
            当前价格：`%s`（点击复制）""";
    public final static String dealLimitPxSellContent = """
            请输入限定价格和卖出数量，以逗号分隔。
            例如：输入0.001,50 代表价格达到 0.001 USDT，自动卖出50%%
            
            当前价格：`%s`（点击复制）""";
    public final static String dealLimitRateBuyContent = """
            请输入价格涨跌幅和买入金额，以逗号分隔。正数代表涨（止盈），负数代表跌（止损）。
            例如：
            输入50,0.01 代表涨50%%，买入0.01 SOL
            输入-50,0.01 代表跌50%%，买入0.01 SOL

            当前价格：`%s`（点击复制）""";
    public final static String dealLimitRateSellContent = """
            请输入价格涨跌幅和卖出数量，以逗号分隔。正数代表涨（止盈），负数代表跌（止损）。
            例如：
            输入50,50 代表涨50%%，卖出50%%
            输入-50,50 代表跌50%%，卖出50%%
            
            当前价格：`%s`（点击复制）""";

    public final static String dealLimitCreateSuccess = """
            ✅ %s-挂单设置成功

            挂单方向：%s
            当前价格：%s  USDT
            挂单价格：%s USDT
            挂单金额/数量：%s %s
            
            ⚠️ 挂单从当前价格达到挂单价格才会被执行，因价格波动或代币数量变化，实际买入价格可能存在误差。
            """;

    public final static String dealLimitOrderKeyBoardFoot = """
            挂单：
            方向: %s
            代币价格：%s USDT
            金额/数量：%s %s
            优先费：%s SOL
            剩余时间：%s""";

    public final static String dealOwnedLimitOrderContent = " ⚔\uFE0F 已配置挂单列表";
    public final static String dealOwnedLimitOrderNoContent = " 暂无已配置挂单";
    public final static String dealOwnedChooseLimitOrderPrefix = dealPrefix + "chooseLimitOrder";
    public final static String dealOwnedCloseAllOrderText = "关闭全部挂单";
    public final static String dealOwnedCloseAllOrderCli = dealPrefix + "dealOwnedCloseAllOrderCli";
    public final static String dealLimitOrderRefreshText = "刷新";
    public final static String dealLimitOrderRefreshCli = dealPrefix + "dealLimitOrderRefreshCli";
    public final static String dealLimitOrderDeleteText = "删除挂单";
    public final static String dealLimitOrderDeleteCli = dealPrefix + "dealLimitOrderDeleteCli";
    public final static String dealLimitOrderReturnAllListText = "返回";
    public final static String dealLimitOrderReturnAllListCli = dealPrefix + "dealLimitOrderReturnAllListCli";
    public final static String dealLimitOrderDetailFromOneCli = dealPrefix + "dealLimitOneOrderDetail";
    public final static String dealLimitOrderRefreshFromOneCli = dealPrefix + "dealLimitOrderRefreshFromOneCli";
    public final static String dealLimitOrderReturnOneCli = dealPrefix + "dealLimitOrderReturnOneCli";
    public final static String dealLimitOrderDeleteOneCli = dealPrefix + "dealLimitOrderDeleteOneCli";
    public final static String dealLimitOrderCancelAllByTokenText = "撤销此币种全部挂单";
    public final static String dealLimitOrderCancelAllByTokenCli = dealPrefix + "dealLimitOrderCancelAllByTokenCli";


    // pump 发射一键卖出
    public final static String dealPumpLaunchFastText = "一键卖出100%(快速模式)";
    public final static String dealPumpLaunchFastCli = dealPrefix + "dealPumpLaunchFastCli";
    public final static String dealPumpLaunchProtectText = "一键卖出100%(防夹模式)";
    public final static String dealPumpLaunchProtectCli = dealPrefix + "dealPumpLaunchProtectCli";

    public final static String dealBuyTextPrefix = "买 ";
    public final static String dealSellTextPrefix = "卖 ";


    public static String generateDealBuyText(String i) {
        return  i + " SOL";
    }
    public static String generateDealBuyCli(String i) {
        return dealPrefix + buyPrefix + i;
    }
    public static String generateDealSellText(String i) {
        return i + " %";
    }
    public static String generateDealSellCli(String i) {
        return dealPrefix + sellPrefix + i;
    }

    public static final String dealImportAmountBuy = "请输入购买金额，输入0.1代表买入0.1 SOL，输入后将立即提交买入交易\n";
    public static final String dealImportAmountSell = "请输入卖出比例，输入0.5代表卖出50%，输入后将立即提交买入交易\n";
    public static final String dealKeyboardHead = """
            \uD83D\uDD04 *买/卖模式*-%s
            合约地址：`%s`

            """;

    public static final String dealLimitKeyboardHead = """
            \uD83D\uDD04 *挂单模式*-%s
            合约地址：`%s`

            """;

    public static String isModeOn(Boolean isOn) {
        if(isOn) {
            return "✅";
        } else {
            return "❌";
        }
    }

    public static final String dealSubmitSuccess = "交易已提交，请等待交易成功！\n";
    public static final String dealSuccessContent = """
            ✅交易成功！%s
            链上交易信息[点击查看](%s)
            %s %s -> %s %s\s
            成交均价：%s

            钱包余额：
            %s %s
            %s %s
            """
            ;

    public static final String dealSuccessNormalContent = """
            ✅交易成功!
            链上交易信息[点击查看](%s)
            %s %s -> %s %s\s
            成交均价：%s

            钱包余额：
            %s %s
            %s %s
            """
            ;
    public static final String dealSuccessLimitContent = """
            挂单-%s
            ✅交易成功!
            链上交易信息[点击查看](%s)
            %s %s -> %s %s\s
            成交均价：%s

            钱包余额：
            %s %s
            %s %s
            """;


    public static final String dealSuccessIcePart = """
            ✅交易成功！%s
            链上交易信息[点击查看](%s)
            %s %s -> %s %s\s
            成交均价：%s
            """;

    public static final String iceDealSuccessContentLast = """
            ✅冰山策略执行成功
            钱包余额：
            %s %s
            %s %s
            """;

    public static final String dealFailLimitPrefix = "挂单-%s \n";

    public static final String dealFailContentPrefix = "❌交易失败! %s \n";

    public static final String dealFailContent = """
            请预留足够的余额以支付gas费、滑点等费用！""";

    public final static String dealToIcebergBuyLongText = """
            请输入单笔买入金额和下单交易次数，以逗号分隔。例如：输入0.1，5 代表买入总额为0.5 SOL，分5次买入，每次买入0.1 SOL，支付5次gas费(下单交易次数最大可输入10)""";
    public final static String dealToIcebergSellLongText = """
            请输入单笔卖出百分比和下单交易次数，以逗号分隔。例如：输入0.2，5 代表卖出余额的100%，分5次卖出，每次卖出20%，支付5次gas费(下单交易次数最大可输入10)""";
    public final static String dealToConfirmTwice = """
            大额订单-二次确认
            
            交易金额：%s SOL
            交易模式：%s
            
            ⚠️大额订单建议使用防夹模式或冰山策略拆分订单""";
}
