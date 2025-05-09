package com.rich.sol_bot.bot.handler.constants;

import java.util.Arrays;
import java.util.List;

public class BotReplyPositionConstants {

    public static final String positionWalletChoose = "\uD83D\uDCC8查看持仓-请选择钱包\n\n";
    public static final String positionPrefix = "position:";
    public static final String positionWalletPrefix = "wallet:";
    public static final String positionWalletChooseText = "钱包  ";
    public static final String positionWalletChooseCli = positionPrefix + positionWalletPrefix;

    public static final String positionReturnToWalletListText = "返回";
    public static final String positionReturnToWalletListCli = positionPrefix + "positionReturnToWalletListCli";
    public static final String positionSwitchWalletText = "切换钱包";
    public static final String positionSwitchWalletCli = positionPrefix + "positionSwitchWalletCli";
    public static final String positionRefreshText = "刷新";
    public static final String positionPrePageText = "上一页";
    public static final String positionPrePageCli = positionPrefix + "pre_page";
    public static final String positionNextPageText = "下一页";
    public static final String positionNextPageCli = positionPrefix + "next_page";

    public static final String positionRefreshCli = positionPrefix + "positionRefreshCli";
    public static final String positionMapIsShowing = "收益图生成中...";
}
