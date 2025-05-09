package com.rich.sol_bot.i18n;

public class ZhCnValue {
    // deal
    public static final String dealTokenToChoose = "⚙️输入您想要买/卖的合约地址";
    public static final String dealPinRefreshText = "刷新";
    public static final String dealFastModeText = "极速模式";
    public static final String dealProtectModeText = "防夹模式";
    public final static String dealToShowPLText = "生成收益图";
    public final static String dealLimitText = "挂单";
    public final static String dealToIcebergText = "冰山策略（强力防夹，大额订单拆分）";
    public final static String dealToIcebergBuyText = "冰山策略买入";
    public final static String dealToIcebergSellText = "冰山策略卖出";
    public final static String dealToIcebergReturnText= "返回";
    public static final String dealTokenPagePreText = "上一页";
    public static final String dealTokenPageNexText = "下一页";
    public final static String dealConfirmSuccessText = "确定下单";
    public final static String dealConfirmCancelText = "取消";
    public static final String dealLimitFastModeText = "极速模式";
    public static final String dealLimitProtectModeText = "防夹模式";
    public final static String dealLimitPxBuyText = "限价委托买入";
    public final static String dealLimitPxSellText = "限价委托卖出";
    public final static String dealLimitRateBuyText = "涨跌幅买入";
    public final static String dealLimitRateSellText = "涨跌幅卖出";
    public final static String dealLimitListOrdersText = "查看此币种挂单";
    public final static String dealLimitReturnText = "返回";
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
    public final static String dealOwnedCloseAllOrderText = "关闭全部挂单";
    public final static String dealLimitOrderRefreshText = "刷新";
    public final static String dealLimitOrderDeleteText = "删除挂单";
    public final static String dealLimitOrderReturnAllListText = "返回";
    public final static String dealLimitOrderCancelAllByTokenText = "撤销此币种全部挂单";
    public final static String dealPumpLaunchFastText = "一键卖出100%(快速模式)";
    public final static String dealPumpLaunchProtectText = "一键卖出100%(防夹模式)";
    public final static String dealBuyTextPrefix = "买 ";
    public final static String dealSellTextPrefix = "卖 ";
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
    public final static String dealTokenInfoTimeAfter = "倒计时: %s日%s小时%s分后开启交易";
    public final static String dealTokenInfoTimeBefore = "创建时间: %s日%s小时%s分前开启交易";

    // ----invite----------------------
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
    public static final String inviteWithdrawText = "提现至钱包";
    public static final String inviteChooseWalletHead = "⚙️选择您要提现的钱包";
    public static final String inviteChooseWalletOtherText = "其他钱包";
    public static final String inviteChooseWalletReturnText = "返回";
    public static final String inviteWaitWalletInput = """
            ⚙️请输入接收此次提现SOL的地址（仅支持SOL网络）
            ⚠️ 请注意核对地址，转账发起后无法撤回，地址输入错误造成的资金损失需自行承担！！！""";
    public static final String inviteTransferSuccessText = """
            ✅转出SOL-交易成功！
            链上交易信息[点击查看](%s)
            转出SOL：%s SOL
            """;
    // ------position-----------
    public static final String positionWalletChoose = "\uD83D\uDCC8查看持仓-请选择钱包\n\n";
    public static final String positionWalletChooseText = "钱包  ";
    public static final String positionReturnToWalletListText = "返回";
    public static final String positionSwitchWalletText = "切换钱包";
    public static final String positionRefreshText = "刷新";
    public static final String positionPrePageText = "上一页";
    public static final String positionNextPageText = "下一页";
    public static final String positionMapIsShowing = "收益图生成中...";
    // ------scraper----------
    public static final String scraperNewTaskText = "\uD83D\uDD2A添加新任务";
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
    public static final String scraperExtraGasText = "贿赂gas费";
    public static final String scraperCountText = "执行次数";
    public static final String scraperCountImportMsg = "请输入刮刀任务执行次数，输入5代表执行成功5次后自动删除任务，输入0代表不限次数，默认为1次";
    public static final String scraperFastModeText = "极速模式";
    public static final String scraperProtectModeText = "防夹模式";
    public static final String scraperAmountContent = "- - 单次买入金额 - -";
    public static String generateDealBuyTextPrefix = "买 ";
    public static final String scraperRmTaskText = "❌删除任务";
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
    // ----------setting
    public static final String settingStart = """
            极速模式滑点：%s
            防夹模式滑点：%s
            买入优先费：%s SOL
            卖出优先费：%s SOL
            狙击优先费: %s SOL
            默认钱包: %s""";
    public static final String settingFastSlippageText = "设置滑点-极速模式";
    public static final String settingProtectSlippageText = "设置滑点-防夹模式";
    public static final String settingBuyGasText = "买入gas优先费";
    public static final String settingSellGasText = "卖出gas优先费";
    public static final String settingSniperGasText = "狙击gas优先费";
    public static final String settingJitoGasText = "防夹模式优先费";
    public static final String settingPreferWalletText = "设置默认钱包";
    public static final String settingAutoSellText = "自动卖出";
    public static final String settingAutoSellContent = """
            开启自动卖出后，系统将根据您设置的参数自动为您买入的全部代币添加限价卖单
            您可以自由配置涨跌幅和卖出比例。如果多次买入相同代币，系统会生成多个配置挂单，您可以在主菜单的“已配置挂单”中手动撤销
            
            """;
    public static final String settingAutoSellSwitchText = "已开启";
    public static final String settingAutoSellConfigText = "添加挂单配置";
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
    public static final String isModeOnText = "✅ 已开启自动卖出";
    public static final String isModeOffText = "❌ 已关闭自动卖出";
    public static String isModeOn(boolean isOn) {
        if(isOn) return isModeOnText;
        else return isModeOffText;
    }
    public static final String settingPreferWalletReturnText = "返回";
    // ----------sniper
    public static final String sniperInputTokenContent = "⚙️输入您想要狙击的合约地址\n";
    public static final String sniperInputAmountContent = """
            ⚙️输入每个钱包允许的最大支出
            允许输入的SOL的格式为0.00，最多输入两位有效小数""";
    public static final String sniperInputExtraGasContent = """
            ⚙️输入想要支付的自动狙击gas费
            含义：额外支付多少SOL 贿赂节点以更快的完成交易，用于自动狙击，最小输入为0.01
            允许输入的SOL的格式为0.00，最多输入两位有效小数
            """;
    public final static String sniperCreateSuccessKeyBoardHead = """
            \uD83D\uDD2B *自动狙击*-%s
            合约地址：`%s`
            """;
    public final static String sniperKeyboardRefreshText = "刷新";
    public final static String sniperSlippageFastModeText = "滑点-极速模式";
    public final static String sniperSlippageProtectModeText = "滑点-防夹模式";
    public final static String sniperExtraGasText = "贿赂gas费";
    public final static String sniperDeleteText = "删除此狙击";
    public final static String sniperListNameTextList = "自动狙击列表\n\n";
    public final static String sniperListNameText = "自动狙击";
    public static String sniperBindButton(Boolean bind) {
        if(bind) return "\uD83D\uDFE2 ";
        return "\uD83D\uDD34 ";
    }
    public static String generateDealBuyText = "买 ";
    public static final String setFastModeSuccess = "✅设置极速模式成功";
    public static final String setProtectModeSuccess = "✅设置防夹模式成功";
    public static final String deleteSnapPlanSuccess = "✅删除狙击配置成功";
    public static final String chooseAtLeastWallet = "❌请至少选择一个钱包";
    public static final String sniperCreatedSuccess = "✅自动狙击设置成功";
    public static final String sniperPumpLaunchContent = """
            \uD83D\uDE80PUMP %s - %s
            合约地址: `%s`
            
            \uD83D\uDCDD 池子归属：RAY\s
            \uD83D\uDCB8 代币价格：%s USDT
            \uD83D\uDC8E持仓价值：%s SOL""";
    // ------ start
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
    public static final String initStartRefreshText = "刷新";
    public static final String initStartAutoSniperText = "\uD83D\uDD2B 自动狙击";
    public static final String initStartDealText = "\uD83E\uDD1D 买/卖";
    public static final String initStartOwnedSniperText = "⚔️ 已设置狙击";
    public static final String initStartOwnedLimitOrderText = "\uD83D\uDDD2 已配置挂单";
    public static final String initStartScraperText = "\uD83D\uDD2A刮刀";
    public static final String initStartPositionText = "\uD83D\uDCC8 查看持仓";
    public static final String initStartWalletText = "\uD83D\uDCB0 钱包";
    public static final String initStartSettingText = "⚙️ 设置";
    public static final String initStartInviteText = "\uD83D\uDC65 邀请返佣";
    public static final String initStartHelpText = "\uD83D\uDCD6 帮助";
    public static final String returnToInitStartText = "返回";
    public static final String returnToInitStartText2 = "\uD83C\uDFE0 主菜单";
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
    // ---- wallet
    public static final String walletCreateText = "创建钱包";
    public static final String walletImportText = "导入钱包";
    public static final String walletRemoveText = "解绑钱包";
    public static final String walletSetNameText = "设置钱包名称";
    public static final String walletExportPriText = "导出钱包私钥";
    public static final String walletTransferSOLText = "转出SOL";
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


    public static final String dealAutoSellNoConfig = "当前暂无配置";
    public static final String dealAutoSellConfigTitle = "已配置：\n";
    public static final String dealAutoSellConfigContent = "配置%s:涨%s卖%s\n";
    public static final String dealAutoSellConfigCancel = "取消配置";
    public static final String initStartLanguageText = "\uD83C\uDF0D切换语言\n";

}
