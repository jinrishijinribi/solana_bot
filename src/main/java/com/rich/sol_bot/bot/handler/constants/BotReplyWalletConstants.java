package com.rich.sol_bot.bot.handler.constants;

import com.rich.sol_bot.bot.handler.enums.WalletActionEnum;

public class BotReplyWalletConstants {
    public static final String walletPrefix = "wallet:";
    public static final String walletCreateText = "创建钱包";
    public static final String walletCreateCli = walletPrefix + "walletCreateCli";
    public static final String walletImportText = "导入钱包";
    public static final String walletImportCli = walletPrefix + "walletImportCli";
    public static final String walletRemoveText = "解绑钱包";
    public static final String walletRemoveCli = walletPrefix + "walletRemoveCli";
    public static final String walletSetNameText = "设置钱包名称";
    public static final String walletSetNameCli = walletPrefix + "walletSetNameCli";
    public static final String walletExportPriText = "导出钱包私钥";
    public static final String walletExportPriCli = walletPrefix + "walletExportPriCli";
    public static final String walletTransferSOLText = "转出SOL";
    public static final String walletTransferSOLCli = walletPrefix + "walletTransferSOLCli";
    public static final String walletListTitle = "您的钱包列表:\n\n";
    public static final String walletShowContent = """
            您的钱包：%s\s
            钱包地址：`%s`\s
            SOL余额：%s SOL

            """;
    public static final String createWalletStart = """
            欢迎使用Duck Sniper SOL机器人。
            当前系统未检测到任何钱包信息，表明您可能是首次使用或已删除所有钱包。
            如需继续使用，请先进行钱包的创建或导入。
            """;
    public static final String createWalletName = "✍️ 请为您的钱包命名，限制在10个字符以内，可使用汉字、字母、数字组合。";
    public static final String createWalletSuccess = """
            ✅ 钱包已成功创建。
            ⚠️ 请确保妥善保管您的私钥。私钥将不会被再次显示。
            \uD83D\uDCA1 私钥：[%s]
            您的SOL钱包已成功添加，钱包地址为：`%s`
            """;

    public static final String importWalletKey = "⚙️输入您的钱包私钥\n\n";
    public static final String importWalletSuccess = """
            ✅ 钱包导入成功
            成功添加钱包：%s\s
            余额：%s SOL""";
    public static final String walletToChooseContent = "⚙️请选择钱包";
    public static final String walletToChooseActionPrefix = walletPrefix + "choose:";
    public static String walletChooseByType(WalletActionEnum type, Long id) {
        return walletToChooseActionPrefix + type.getValue() + ":" + id;
    }
    public static final String walletToSetNameContent = "⚙️命名您的钱包，10个以内的字符，支持汉字，字母，数字组合\n";
    public static final String walletToTransferSolContent = "⚙️输入转出SOL数量\n\n允许输入的SOL的格式为0.0000，最多输入四位有效小数";

    public static final String walletTransferInputAddress = """
            ⚙️请输入接收此次转出SOL的地址（仅支持SOL网络）

            ⚠️ 请注意核对地址，转账发起后无法撤回，地址输入错误造成的资金损失需自行承担！！！""";

    public static final String walletTransferSubmitSuccess = "交易已提交，请等待交易成功！\n\n";

    public static final String walletTransferFailText = "❌SOL余额不足，转出SOL失败";

    public static final String walletTransferSuccessText = """
            ✅转出SOL-交易成功！
            链上交易信息[点击查看](%s)
            转出SOL：%s SOL
            钱包余额：%s SOL""";


    public static final String tokenStaticContentPart1Sniper =
            """
                    *代币*：
                    \uD83D\uDCB0 当前市值：%s USDT\s
                    \uD83D\uDCB8 代币价格：%s USDT\s
                    \uD83D\uDD1D 前十占比：%s\s

                    *池子*：
                    \uD83C\uDFE6 池子金额：%s SOL\s
                    📝 池子归属：%s %s\s
                    ⏳ %s\s
                    无增发 (%s)  丢权限 (%s) 烧池子 (%s)\s

                    """;

    public static final String tokenStaticContentPart2 = """
            *群友*：
            \uD83D\uDC65 上车人数：%s\s
            ⚖️ 盈亏比例：%s : %s\s

            """;

    public static final String tokenStaticContentPart3 = """
            *链接*：
            [X](%s) | [Telegram](%s)
            [DexScreener](%s) | [Birdeye](%s) | [Dextools](%s) | [Pump](%s) | [GMGN](%s) \s

            """;

    public static final String tokenWalletContentPart4Sniper = """
            *狙击*：
            \uD83D\uDC5B 钱包余额：%s SOL
            \uD83D\uDCB0 狙击金额：%s SOL
            \uD83D\uDCA8 狙击优先费：%s SOL
            """;

    public static final String tokenWalletContentPart4Deal = """
            *钱包*：
            \uD83D\uDCB5 当前余额：%s SOL \s
            \uD83D\uDCB1 买入均价：%s USDT\s
            \uD83D\uDC8E 持仓价值：%s SOL\s
            \uD83D\uDCB9 持仓涨跌：%s\s
            """;

}
