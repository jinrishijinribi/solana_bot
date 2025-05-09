package com.rich.sol_bot.bot.handler.constants;


public class BotReplyStartConstants {
    public static final String initStart =
            """
                    欢迎使用冲锋鸭推出的Duck Sniper Solana Bot

                    \uD83D\uDCAC 使用须知\s
                    1. 请确保妥善保管您的钱包私钥, Bot不会也无法保存您的私钥信息
                    2. 在进行任何投资操作前, 请彻底了解项目详情.  ❗务必注意: 投资具有潜在的资产归零风险\s
                    3. 我们鼓励使用邀请链接邀请您的朋友加入Duck Sniper, 您将能永久享有40%的返佣 \uD83C\uDF7B
                    4. 如遇任何使用问题，请通过加入冲锋鸭社群并联系管理员进行咨询: https://t.me/rushduckarmy

                    感谢您的使用和支持!""";

    public static final String initStartNoWallet = """
            检测到您是首次使用或已解绑所有钱包，请前往“钱包”部分执行创建或导入钱包的操作。
            如遇任何使用问题，请通过加入冲锋鸭社群并联系管理员进行咨询: https://t.me/rushduckarmy""";

    public static final String initStartDefaultWallet = """
            钱包地址: `%s`\s
            余额: %s SOL ($%s)
            
            点击“刷新”按钮以更新您的当前余额。如遇任何使用问题，请通过加入冲锋鸭社群并联系管理员进行咨询: https://t.me/rushduckarmy""";

    // init start
    public static final String initStartPrefix = "initStart:";
    public static final String startCode = "/start";
    public static final String initStartRefreshText = "刷新";
    public static final String initStartRefreshCli = initStartPrefix + "initStartRefreshCli";
    public static final String initStartAutoSniperText = "\uD83D\uDD2B 自动狙击";
    public static final String initStartAutoSniperCli = initStartPrefix + "initStartAutoSniperCli";
    public static final String initStartDealText = "\uD83E\uDD1D 买/卖";
    public static final String initStartDealCli = initStartPrefix + "initStartBuyAndSellCli";
    public static final String initStartOwnedSniperText = "⚔️ 已设置狙击";
    public static final String initStartOwnedSniperCli = initStartPrefix + "initStartOwnedSniperCli";
    public static final String initStartOwnedLimitOrderText = "\uD83D\uDDD2 已配置挂单";
    public static final String initStartOwnedLimitOrderCli = initStartPrefix + "initStartOwnerLimitOrderCli";

    public static final String initStartScraperText = "\uD83D\uDD2A刮刀";
    public static final String initStartScraperCli = initStartPrefix + "initStartScraperCli";

    public static final String initStartPositionText = "\uD83D\uDCC8 查看持仓";
    public static final String initStartPositionCli = initStartPrefix + "initStartPositionCli";
    public static final String initStartWalletText = "\uD83D\uDCB0 钱包";
    public static final String initStartWalletCli = initStartPrefix + "initStarWalletCli";
    public static final String initStartSettingText = "⚙️ 设置";
    public static final String initStartSettingCli =  initStartPrefix + "initStartSettingCli";
    public static final String initStartInviteText = "\uD83D\uDC65 邀请返佣";
    public static final String initStartInviteCli = initStartPrefix + "initStartInviteCli";
    public static final String initStartLanguageCli = initStartPrefix + "initStartLanguageCli";
    public static final String initStartHelpText = "\uD83D\uDCD6 帮助";
    public static final String initStartHelpCli = initStartPrefix + "initStartHelpCli";
    public static final String returnToInitStartText = "返回";
    public static final String returnToInitStartText2 = "\uD83C\uDFE0 主菜单";

    public static final String returnToInitStartCli = initStartPrefix + "returnToInitStartCli";

    public static final String initStartHelpContent = """
            📖 钱包
        你可以通过两种方式连接钱包，进而开始交易。
        1、通过导入私钥绑定已有钱包
        2、通过Duck Sniper Bot生成新钱包
        有两点需要注意：
        ⛔️ 请勿导入主钱包或有大额资产的钱包
        ⛔️ 请勿对外泄露私钥
        
        📖 管理钱包
        Duck Sniper Bot最多支持绑定10个钱包。
        可以切换默认钱包、设置钱包昵称等。
        
        📖 自动狙击
        Duck Sniper Bot 支持 Raydium 池子的开盘狙击，只需发送合约地址即可设置狙击金额和防夹模式。项目开盘时，机器人会立即发起买入。在设置中，可以调整狙击优先费以提高速度，建议设置为 0.01 SOL 以上，这样失败率低且速度较快。
        
        📖 Pump代币交易
        pump.fun 是Solana上的快速发币平台，新项目在完成bonding curve之前，短期不会添加Raydium池子流动性。
        如果你想交易pump项目，发送合约或者pump链接给Duck Sniper Bot就能选择买入或卖出。
        
        📖 买/卖
        需要买/卖特定项目时，直接输入合约地址，系统将自动识别项目信息。包括项目数据，上车群友，安全信息等，便于快速做出交易决策。
        交易可以选择防夹模式或极速模式，防夹更安全，可以避免财产损失。
        
        📖 快速买/卖
        点击买/卖金额，系统将自动完成买/卖，若系统默认金额不符合您的需求，点击最后一个选项 买 x SOL/ 卖x% ，输入自定义买入金额即可。
        
        📖 限价挂单
        Duck Sniper Bot支持挂单，方便快速抄底、止盈止损，无须盯盘
        可以按照指定价格，或者指定涨跌幅挂单，当代币价格到达挂单价格，会立即触发交易。
        发送合约地址，点击【挂单】按钮开始设置，输入目标价格（涨跌比例）和自动买/卖金额，以逗号分隔。例如，输入0.1,1 代表价格达到 $0.1，自动买入1 SOL。到达价格自动触发，即可实现抄底。
        
        📖 交易执行
        系统一般会在买/卖后自动发起授权，减少等待成本。
        当交易成功后，会出现交易成功提示，即代表实际交易已上链。可以看到实际的交易金额，买入的代币数量，交易时的价格，以及钱包余额。
        
        📖 冰山策略
        冰山策略是一种大额订单拆分、分批下单的策略。 用户在进行大额交易时，为避免被夹子机器人恶意攻击，可以通过此策略将大单交易自动拆为多笔小额下单（gas费也将随着下单数增多而支付多笔）
        
        📖 查看持仓
        支持查看当前持仓代币名称，持仓价值，持仓涨跌，持仓时长。
        
        📖 邀请返佣
        邀请返佣功能，复制邀请链接，分享给新用户使用，可以永久赚取他们交易手续费的40%（满0.01 SOL可提现）
        
        📖 设置
        默认设置能够满足新手交易需求，无需调整。推荐的交易参数具有较高的成功率、较快的上链速度，并且被夹风险较低，用户可参考使用。如果交易的代币波动较大，或者希望提高交易速度，可以调整滑点和gas设置。
                                                                                                             
        🏘️ [官方社群](https://t.me/rushduckarmy)
        🐦 [推特链接](https://x.com/DuckSniper_Bot)\
            """;

}
