package com.rich.sol_bot.bot.handler.constants;

public class BotReplyInviteConstants {
    public static final String invitePrefix = "invite:";
    public static final String inviteKeyBoardContent = """
            \uD83D\uDD17 邀请链接: %s\s
            \uD83D\uDCB5 可提现：%s SOL（%s SOL 处理中）
            \uD83D\uDCB0 累计提现：%s SOL   \
            \uD83D\uDC65 累计邀请：%s 人\s
            \uD83D\uDCD6 规则：
            1. 邀请他人使用，可以永久赚取他们交易手续费的%s
            2. 满0.01 SOL可提现
            """;

    public static final String inviteRefreshText = "刷新";
    public static final String inviteRefreshCli = invitePrefix + "inviteRefreshCli";

    public static final String inviteWithdrawText = "提现至钱包";
    public static final String inviteWithdrawCli = invitePrefix + "inviteWithdrawCli";
    public static final String inviteChooseWalletHead = "⚙️选择您要提现的钱包";
    public static final String inviteChooseWalletPrefix = invitePrefix + "choose_wallet";

    public static final String inviteChooseWalletOtherText = "其他钱包";
    public static final String inviteChooseWalletOtherCli = invitePrefix + "inviteChooseWalletOtherCli";
    public static final String inviteChooseWalletReturnText = "返回";
    public static final String inviteChooseWalletReturnCli = invitePrefix + "inviteChooseWalletReturnCli";
    public static final String inviteWaitWalletInput = """
            ⚙️请输入接收此次提现SOL的地址（仅支持SOL网络）
            ⚠️ 请注意核对地址，转账发起后无法撤回，地址输入错误造成的资金损失需自行承担！！！""";

    public static final String inviteTransferSuccessText = """
            ✅转出SOL-交易成功！
            链上交易信息[点击查看](%s)
            转出SOL：%s SOL
            """;

}
