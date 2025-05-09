package com.rich.sol_bot.i18n;

public class EnUsValue {
    public static final String dealTokenToChoose = "⚙️ Enter the contract address you want to buy/sell";
    public static final String dealPinRefreshText = "Refresh";
    public static final String dealFastModeText = "Fast Mode";
    public static final String dealProtectModeText = "Anti-Front-Run Mode";
    public final static String dealToShowPLText = "Generate Profit Chart";
    public final static String dealLimitText = "Limit Order";
    public final static String dealToIcebergText = "Iceberg Strategy (Strong Anti-Front-Run, Large Orders Split)";
    public final static String dealToIcebergBuyText = "Iceberg Strategy Buy";
    public final static String dealToIcebergSellText = "Iceberg Strategy Sell";
    public final static String dealToIcebergReturnText = "Return";
    public static final String dealTokenPagePreText = "Previous Page";
    public static final String dealTokenPageNexText = "Next Page";
    public final static String dealConfirmSuccessText = "Confirm Order";
    public final static String dealConfirmCancelText = "Cancel";
    public static final String dealLimitFastModeText = "Fast Mode";
    public static final String dealLimitProtectModeText = "Anti-Front-Run Mode";
    public final static String dealLimitPxBuyText = "Limit Order Buy";
    public final static String dealLimitPxSellText = "Limit Order Sell";
    public final static String dealLimitRateBuyText = "Price Change Buy";
    public final static String dealLimitRateSellText = "Price Change Sell";
    public final static String dealLimitListOrdersText = "View Limit Orders for This Token";
    public final static String dealLimitReturnText = "Return";
    public final static String dealLimitPxBuyContent = """
        Enter the target price and purchase amount, separated by a comma.
        Example: Enter 0.001,0.1 to automatically buy 0.1 SOL when the price reaches 0.001 USDT.

        Current price: `%s` (Click to copy)""";
    public final static String dealLimitPxSellContent = """
        Enter the target price and sell quantity, separated by a comma.
        Example: Enter 0.001,50 to automatically sell 50%% when the price reaches 0.001 USDT.

        Current price: `%s` (Click to copy)""";
    public final static String dealLimitRateBuyContent = """
        Enter the price change percentage and purchase amount, separated by a comma. Positive values indicate an increase (profit-taking), and negative values indicate a decrease (stop-loss).
        Example:
        Enter 50,0.01 to buy 0.01 SOL at a 50%% increase.
        Enter -50,0.01 to buy 0.01 SOL at a 50%% decrease.

        Current price: `%s` (Click to copy)""";
    public final static String dealLimitRateSellContent = """
        Enter the price change percentage and sell percentage, separated by a comma. Positive values indicate an increase (profit-taking), and negative values indicate a decrease (stop-loss).
        Example:
        Enter 50,50 to sell 50%% at a 50%% increase.
        Enter -50,50 to sell 50%% at a 50%% decrease.

        Current price: `%s` (Click to copy)""";
    public final static String dealLimitCreateSuccess = """
        ✅ %s-Limit Order Created Successfully

        Order Direction: %s
        Current Price: %s USDT
        Order Price: %s USDT
        Order Amount/Quantity: %s %s

        ⚠️ Orders will only execute when the current price reaches the order price. Due to price fluctuations or token availability, the actual execution price may vary.
        """;
    public final static String dealLimitOrderKeyBoardFoot = """
        Limit Order:
        Direction: %s
        Token Price: %s USDT
        Amount/Quantity: %s %s
        Priority Fee: %s SOL
        Remaining Time: %s""";
    public final static String dealOwnedLimitOrderContent = "⚔️ Configured Limit Orders";
    public final static String dealOwnedLimitOrderNoContent = "No Configured Limit Orders";
    public final static String dealOwnedCloseAllOrderText = "Close All Limit Orders";
    public final static String dealLimitOrderRefreshText = "Refresh";
    public final static String dealLimitOrderDeleteText = "Delete Limit Order";
    public final static String dealLimitOrderReturnAllListText = "Return";
    public final static String dealLimitOrderCancelAllByTokenText = "Cancel All Limit Orders for This Token";
    public final static String dealPumpLaunchFastText = "Sell 100%% Instantly (Fast Mode)";
    public final static String dealPumpLaunchProtectText = "Sell 100%% Instantly (Anti-Front-Run Mode)";
    public final static String dealBuyTextPrefix = "Buy ";
    public final static String dealSellTextPrefix = "Sell ";
    public static final String dealImportAmountBuy = "Enter the purchase amount. Enter 0.1 to buy 0.1 SOL. The transaction will be submitted immediately.\n";
    public static final String dealImportAmountSell = "Enter the selling ratio. Enter 0.5 to sell 50%%. The transaction will be submitted immediately.\n";
    public static final String dealKeyboardHead = """
        🔄 *Buy/Sell Mode*-%s
        Contract Address: `%s`

        """;
    public static final String dealLimitKeyboardHead = """
        🔄 *Limit Order Mode*-%s
        Contract Address: `%s`

        """;
    public static final String dealSubmitSuccess = "Transaction submitted. Please wait for successful execution!\n";
    public static final String dealSuccessContent = """
        ✅ Transaction Successful! %s
        On-Chain Transaction Info [Click to View](%s)
        %s %s -> %s %s
        Average Execution Price: %s

        Wallet Balance:
        %s %s
        %s %s
        """;
    public static final String dealSuccessNormalContent = """
        ✅ Transaction Successful!
        On-Chain Transaction Info [Click to View](%s)
        %s %s -> %s %s
        Average Execution Price: %s

        Wallet Balance:
        %s %s
        %s %s
        """;
    public static final String dealSuccessLimitContent = """
        Limit Order-%s
        ✅ Transaction Successful!
        On-Chain Transaction Info [Click to View](%s)
        %s %s -> %s %s
        Average Execution Price: %s

        Wallet Balance:
        %s %s
        %s %s
        """;
    public static final String dealSuccessIcePart = """
        ✅ Transaction Successful! %s
        On-Chain Transaction Info [Click to View](%s)
        %s %s -> %s %s
        Average Execution Price: %s
        """;
    public static final String iceDealSuccessContentLast = """
        ✅ Iceberg Strategy Executed Successfully
        Wallet Balance:
        %s %s
        %s %s
        """;
    public static final String dealFailLimitPrefix = "Limit Order-%s \n";
    public static final String dealFailContentPrefix = "❌ Transaction Failed! %s \n";
    public static final String dealFailContent = """
        Please ensure sufficient balance to cover gas fees, slippage, etc.!""";
    public final static String dealToIcebergBuyLongText = """
        Enter the single purchase amount and the number of transactions, separated by a comma. For example, enter 0.1,5 to buy a total of 0.5 SOL in 5 transactions, with each transaction buying 0.1 SOL (up to 10 transactions).""";
    public final static String dealToIcebergSellLongText = """
        Enter the single selling percentage and the number of transactions, separated by a comma. For example, enter 0.2,5 to sell 100%% of the balance in 5 transactions, with each transaction selling 20%% (up to 10 transactions).""";
    public final static String dealToConfirmTwice = """
        Large Order - Secondary Confirmation

        Transaction Amount: %s SOL
        Transaction Mode: %s

        ⚠️ It is recommended to use Anti-Front-Run Mode or Iceberg Strategy for large orders.""";
    public final static String dealTokenInfoTimeAfter = "Countdown: %s days %s hours %s minutes until trading starts";
    public final static String dealTokenInfoTimeBefore = "Creation time: %s days %s hours %s minutes before trading starts";

    // ----
    public static final String inviteKeyBoardContent = """
        \uD83D\uDD17 Invitation Link: %s\s
        \uD83D\uDCB5 Withdrawable: %s SOL (%s SOL in process)
        \uD83D\uDCB0 Total Withdrawn: %s SOL   \
        \uD83D\uDC65 Total Invites: %s People\s
        \uD83D\uDCD6 Rules:
        1. Invite others to use the service and earn %s of their transaction fees permanently.
        2. Withdrawals are available for balances above 0.01 SOL.
        """;
    public static final String inviteRefreshText = "Refresh";
    public static final String inviteWithdrawText = "Withdraw to Wallet";
    public static final String inviteChooseWalletHead = "⚙️ Choose the Wallet for Withdrawal";
    public static final String inviteChooseWalletOtherText = "Other Wallet";
    public static final String inviteChooseWalletReturnText = "Return";
    public static final String inviteWaitWalletInput = """
        ⚙️ Please enter the address to receive the withdrawn SOL (only supports the SOL network).
        ⚠️ Please double-check the address. Once the transaction is initiated, it cannot be canceled. Losses due to incorrect address input must be borne by the user!""";
    public static final String inviteTransferSuccessText = """
        ✅ SOL Withdrawal - Transaction Successful!
        On-Chain Transaction Info [Click to View](%s)
        Withdrawn SOL: %s SOL
        """;
    // --------------
    public static final String positionWalletChoose = "\uD83D\uDCC8 View Positions - Please choose a wallet\n\n";
    public static final String positionWalletChooseText = "Wallet  ";
    public static final String positionReturnToWalletListText = "Return";
    public static final String positionSwitchWalletText = "Switch Wallet";
    public static final String positionRefreshText = "Refresh";
    public static final String positionPrePageText = "Previous Page";
    public static final String positionNextPageText = "Next Page";
    public static final String positionMapIsShowing = "Generating Profit Chart...";
    // --------------
    public static final String scraperNewTaskText = "\uD83D\uDD2A Add New Task";
    public static final String scraperStartImportNameMsg = """
            ⚙️ Enter the Twitter handle you want to scrape (just the part after @)
            """;
    public static final String scraperStartTwitterErrMsg = """
            Error: Could not find this Twitter handle, please try again
            ⚙\uFE0F Enter the Twitter handle you want to scrape (just the part after @)
            """;
    public static final String scraperStartInputAmountMsg = "Please enter the amount, for example, enter 0.1 to buy 0.1 SOL. After entering, a scraping task will be created.";
    public static final String scraperSendSuccess = """
            ✅ Scraping Task %s - Transaction has been initiated
            \uD83C\uDD94 Twitter ID: %s
            Token Name: %s
            Contract Address: %s
            Current task trigger count: %s
            """;
    public static final String scraperRefreshText = "Refresh";
    public static final String scraperExtraGasText = "Bribe Gas Fee";
    public static final String scraperCountText = "Execution Count";
    public static final String scraperCountImportMsg = "Enter the number of times you want the scraping task to execute. Enter 5 for the task to delete after 5 successful executions. Enter 0 for unlimited times. The default is 1 time.";
    public static final String scraperFastModeText = "Fast Mode";
    public static final String scraperProtectModeText = "Anti-sniping Mode";
    public static final String scraperAmountContent = "- - Single Buy Amount - -";
    public static String generateDealBuyTextPrefix = "Buy ";
    public static final String scraperRmTaskText = "❌ Delete Task";
    public static final String scraperNoTask = """
            
            🔪 No Scraping Tasks
            
            """;
    public static final String scraperTaskInfoTemplate = """
            \uD83D\uDD2A Scraping Task %s
            
            \uD83C\uDD94 Twitter ID: %s
            ✅ Scraping Success Count: %s
            
            \uD83D\uDC5B Wallet Balance: %s SOL
            \uD83D\uDD22 Scraping Execution Count: %s
            \uD83D\uDCB0 Single Buy Amount: %s SOL
            """;
    // ----------------------
    public static final String settingStart = """
            Fast Mode Slippage: %s
            Anti-sniping Mode Slippage: %s
            Buy Priority Fee: %s SOL
            Sell Priority Fee: %s SOL
            Sniping Priority Fee: %s SOL
            Default Wallet: %s""";
    public static final String settingFastSlippageText = "Set Slippage - Fast Mode";
    public static final String settingProtectSlippageText = "Set Slippage - Anti-sniping Mode";
    public static final String settingBuyGasText = "Buy Gas Priority Fee";
    public static final String settingSellGasText = "Sell Gas Priority Fee";
    public static final String settingSniperGasText = "Sniping Gas Priority Fee";
    public static final String settingJitoGasText = "Anti-sniping Mode Priority Fee";
    public static final String settingPreferWalletText = "Set Default Wallet";
    public static final String settingAutoSellText = "Auto Sell";
    public static final String settingAutoSellContent = """
            After enabling auto-sell, the system will automatically add limit sell orders for all tokens you buy based on the parameters you set.
            You can freely configure price change percentage and sell proportion. If you buy the same token multiple times, the system will generate multiple configured orders, which you can manually cancel in the "Configured Orders" section of the main menu.
            """;
    public static final String settingAutoSellSwitchText = "Enabled";
    public static final String settingAutoSellConfigText = "Add Order Configuration";
    public static final String settingAutoSellConfig = """
            Please enter price change percentage and automatic sell proportion, separated by commas.
            For example:
            Enter 50,100 means the price increases by 50% and sell 100%.
            Enter -50,100 means the price decreases by 50% and sell 100%""";
    public static final String settingForFast = """
            Please enter slippage.
            For fast mode, it is recommended to set between 10-20, not lower than 5.
            For anti-sniping mode, it is recommended to set between 20-50 to ensure success rate""";
    public static final String settingForProtect = """
            Please enter slippage.
            For fast mode, it is recommended to set between 10-20, not lower than 5.
            For anti-sniping mode, it is recommended to set between 20-50 to ensure success rate""";
    public static final String settingForBuyGas = """
            Please enter the buy priority fee (determines transaction speed), range 0-1 SOL.
            Current priority fee: %s SOL
            For normal transactions, it is recommended to set 0.003 SOL; for fast transactions, 0.005 SOL; for ultra-fast, 0.01 SOL or higher""";
    public static final String settingForSellGas = """
            Please enter the sell priority fee (determines transaction speed), range 0-1 SOL.
            Current priority fee: %s SOL
            For normal transactions, it is recommended to set 0.003 SOL; for fast transactions, 0.005 SOL; for ultra-fast, 0.01 SOL or higher""";
    public static final String settingForSniper = """
            Please enter the sniping priority fee (determines transaction speed), range 0-1 SOL.
            Current priority fee: %s SOL""";
    public static final String settingForJito = """
            Please enter the anti-sniping priority fee (determines transaction speed), range 0-1 SOL.
            Current priority fee: %s SOL""";
    public static final String settingPreferWalletHead = "⚙️ Set Default Wallet\n";
    public static final String isModeOnText = "✅ Auto Sell Enabled";
    public static final String isModeOffText = "❌ Auto Sell Disabled";
    public static String isModeOn(boolean isOn) {
        if(isOn) return isModeOnText;
        else return isModeOffText;
    }
    public static final String settingPreferWalletReturnText = "Return";
    // ------------
    public static final String sniperInputTokenContent = "⚙️ Enter the contract address you want to sniper\n";
    public static final String sniperInputAmountContent = """
            ⚙️ Enter the maximum spend allowed per wallet
            The allowed format for SOL is 0.00, with a maximum of two decimal places""";
    public static final String sniperInputExtraGasContent = """
            ⚙️ Enter the extra gas fee you want to pay for automatic sniping
            Meaning: How much extra SOL to bribe the node to complete the transaction faster, used for automatic sniping, the minimum input is 0.01.
            The allowed format for SOL is 0.00, with a maximum of two decimal places
            """;
    public final static String sniperCreateSuccessKeyBoardHead = """
            \uD83D\uDD2B *Auto Sniping*-%s
            Contract Address: `%s`
            """;
    public final static String sniperKeyboardRefreshText = "Refresh";
    public final static String sniperSlippageFastModeText = "Slippage - Fast Mode";
    public final static String sniperSlippageProtectModeText = "Slippage - Anti-sniping Mode";
    public final static String sniperExtraGasText = "Bribe Gas Fee";
    public final static String sniperDeleteText = "Delete this Sniping";
    public final static String sniperListNameTextList = "Auto Sniping List\n\n";
    public final static String sniperListNameText = "Auto Sniping";
    public static String sniperBindButton(Boolean bind) {
        if(bind) return "\uD83D\uDFE2 "; // Green Circle
        return "\uD83D\uDD34 "; // Red Circle
    }
    public static String generateDealBuyText = "Buy ";
    public static final String setFastModeSuccess = "✅ Fast Mode set successfully";
    public static final String setProtectModeSuccess = "✅ Anti-sniping Mode set successfully";
    public static final String deleteSnapPlanSuccess = "✅ Sniping configuration deleted successfully";
    public static final String chooseAtLeastWallet = "❌ Please choose at least one wallet";
    public static final String sniperCreatedSuccess = "✅ Auto sniping set up successfully";
    public static final String sniperPumpLaunchContent = """
            \uD83D\uDE80PUMP %s - %s
            Contract Address: `%s`
            
            \uD83D\uDCDD Pool Ownership: RAY\s
            \uD83D\uDCB8 Token Price: %s USDT
            \uD83D\uDC8E Holding Value: %s SOL""";
    // ------ start
    public static final String initStart =
            """
            Welcome to Duck Sniper Solana Bot by RushDuck
            
            \uD83D\uDCAC Usage Instructions:
            1. Please ensure that you securely store your wallet private key. The Bot does not and cannot save your private key information.
            2. Before making any investment, please thoroughly understand the project details. ❗ Be sure to note: Investments carry the potential risk of asset loss.
            3. We encourage you to invite your friends to join Duck Sniper using your referral link, where you can earn 40% commission forever. \uD83C\uDF7B
            4. If you encounter any issues, please join the RushDuck community and contact the administrator for assistance: https://t.me/rushduckarmy

            Thank you for your support and usage!
            """;
    public static final String initStartNoWallet = """
            It seems this is your first time using the bot, or you have unbound all your wallets. Please go to the "Wallet" section to create or import a wallet.
            If you encounter any issues, please join the RushDuck community and contact the administrator for assistance: https://t.me/rushduckarmy""";
    public static final String initStartDefaultWallet = """
            Wallet Address: `%s`
            Balance: %s SOL ($%s)

            Click the "Refresh" button to update your current balance. If you encounter any issues, please join the RushDuck community and contact the administrator for assistance: https://t.me/rushduckarmy""";
    public static final String initStartRefreshText = "Refresh";
    public static final String initStartAutoSniperText = "\uD83D\uDD2B Auto Sniping";
    public static final String initStartDealText = "\uD83E\uDD1D Buy/Sell";
    public static final String initStartOwnedSniperText = "⚔️ Sniping Set";
    public static final String initStartOwnedLimitOrderText = "\uD83D\uDDD2 Limit Orders Configured";
    public static final String initStartScraperText = "\uD83D\uDD2A Scraper";
    public static final String initStartPositionText = "\uD83D\uDCC8 View Holdings";
    public static final String initStartWalletText = "\uD83D\uDCB0 Wallet";
    public static final String initStartSettingText = "⚙️ Settings";
    public static final String initStartInviteText = "\uD83D\uDC65 Invite Rewards";
    public static final String initStartHelpText = "\uD83D\uDCD6 Help";
    public static final String returnToInitStartText = "Return";
    public static final String returnToInitStartText2 = "\uD83C\uDFE0 Main Menu";
    public static final String initStartHelpContent = """
            📖 Wallet
        You can connect your wallet in two ways to start trading.
        1. Bind an existing wallet by importing the private key.
        2. Create a new wallet using Duck Sniper Bot.
        Two important notes:
        ⛔️ Do not import your main wallet or wallets with significant assets.
        ⛔️ Do not expose your private key.
        
        📖 Managing Wallets
        Duck Sniper Bot supports binding up to 10 wallets.
        You can switch the default wallet, set wallet nicknames, etc.
        
        📖 Auto Sniping
        Duck Sniper Bot supports opening sniping for Raydium pools. Simply send the contract address to set the sniping amount and anti-sniping mode. The bot will immediately initiate a purchase when the project launches. In settings, you can adjust the sniping fee to increase speed; it's recommended to set it above 0.01 SOL for lower failure rates and faster speeds.
        
        📖 Pump Token Trading
        pump.fun is a fast token issuance platform on Solana. New projects, before completing the bonding curve, will not add liquidity to Raydium pools in the short term.
        If you want to trade pump tokens, simply send the contract or pump link to Duck Sniper Bot to choose whether to buy or sell.
        
        📖 Buy/Sell
        When you need to buy or sell a specific project, directly enter the contract address, and the system will automatically recognize project information, including project data, community groups, security details, etc., to help you quickly make a trading decision.
        You can choose either anti-sniping mode or fast mode for transactions. Anti-sniping mode is safer and avoids potential losses.
        
        📖 Quick Buy/Sell
        Click on the buy/sell amount, and the system will automatically complete the buy/sell. If the default amount does not meet your needs, click the last option "Buy x SOL/Sell x%" to enter a custom amount.
        
        📖 Limit Orders
        Duck Sniper Bot supports placing limit orders, making it easy to buy at the bottom or take profits/losses without monitoring the market.
        You can place an order at a specified price or by a percentage change. When the token price reaches the order price, the trade is triggered immediately.
        Send the contract address, click the "Limit Order" button to start setting, and input the target price (percentage change) and automatic buy/sell amount, separated by commas. For example, enter 0.1,1 to buy 1 SOL when the price reaches $0.1. The system will automatically trigger when the price is reached.
        
        📖 Transaction Execution
        After buying/selling, the system will usually initiate authorization automatically to reduce waiting time.
        When the transaction is successful, a success prompt will appear, indicating that the transaction has been recorded on the blockchain. You can view the actual transaction amount, token quantity purchased, transaction price, and wallet balance.
        
        📖 Iceberg Strategy
        The iceberg strategy is a method of splitting large orders into multiple smaller orders. When making large transactions, users can use this strategy to automatically split big trades into smaller ones to avoid malicious attacks by sniping bots (gas fees will increase with the number of smaller orders).
        
        📖 View Holdings
        Supports viewing current holdings, token names, holding value, price changes, and holding duration.
        
        📖 Invite Rewards
        The invite reward feature allows you to copy the invite link and share it with new users. You can earn 40% of their trading fees permanently (withdrawable after reaching 0.01 SOL).
        
        📖 Settings
        The default settings meet the needs of new traders without the need for adjustment. The recommended trading parameters have a high success rate, fast blockchain speeds, and a low risk of being sniped. Users can refer to these settings. If the token being traded fluctuates significantly or if you want to increase the speed of transactions, you can adjust the slippage and gas settings.
                                                                                                             
        🏘️ [Official Community](https://t.me/rushduckarmy)
        🐦 [Twitter Link](https://x.com/DuckSniper_Bot)
            """;
    public static final String walletCreateText = "Create Wallet";
    public static final String walletImportText = "Import Wallet";
    public static final String walletRemoveText = "Unbind Wallet";
    public static final String walletSetNameText = "Set Wallet Name";
    public static final String walletExportPriText = "Export Wallet Private Key";
    public static final String walletTransferSOLText = "Transfer SOL";
    public static final String walletListTitle = "Your Wallet List:\n\n";
    public static final String walletShowContent = """
            Your Wallet: %s
            Wallet Address: `%s`
            SOL Balance: %s SOL
            """;
    public static final String createWalletStart = """
            Welcome to Duck Sniper SOL Bot.
            The system currently detects no wallet information, indicating that you may be using the bot for the first time or have deleted all wallets.
            To continue, please create or import a wallet first.
            """;
    public static final String createWalletName = "✍️ Please name your wallet, limited to 10 characters. You can use Chinese characters, letters, and numbers.";
    public static final String createWalletSuccess = """
            ✅ Wallet created successfully.
            ⚠️ Please securely store your private key. It will not be shown again.
            \uD83D\uDCA1 Private Key: [%s]
            Your SOL wallet has been successfully added with the address: `%s`
            """;

    public static final String importWalletKey = "⚙️ Enter your wallet private key\n\n";
    public static final String importWalletSuccess = """
            ✅ Wallet imported successfully.
            Successfully added wallet: %s
            Balance: %s SOL""";
    public static final String walletToChooseContent = "⚙️ Please choose a wallet";
    public static final String walletToSetNameContent = "⚙️ Name your wallet, up to 10 characters, supports Chinese, letters, and numbers\n";
    public static final String walletToTransferSolContent = "⚙️ Enter the amount of SOL to transfer\n\nThe allowed format for SOL is 0.0000, with up to four decimal places";
    public static final String walletTransferInputAddress = """
            ⚙️ Please enter the address to receive the SOL transfer (SOL network only)

            ⚠️ Please double-check the address, as transfers cannot be undone once initiated. You are responsible for any funds lost due to incorrect addresses!""";
    public static final String walletTransferSubmitSuccess = "Transaction submitted, please wait for it to complete!\n\n";
    public static final String walletTransferFailText = "❌ Insufficient SOL balance, transfer failed";
    public static final String walletTransferSuccessText = """
            ✅ SOL transfer successful!
            Blockchain transaction details [Click here](%s)
            Transferred SOL: %s SOL
            Wallet balance: %s SOL""";
    public static final String tokenStaticContentPart1Sniper =
            """
            *Token*:
            \uD83D\uDCB0 Current Market Cap: %s USDT
            \uD83D\uDCB8 Token Price: %s USDT
            \uD83D\uDD1D Top 10 Proportion: %s

            *Pool*:
            \uD83C\uDFE6 Pool Amount: %s SOL
            📝 Pool Owner: %s %s
            ⏳ %s
            No Mint (%s) No Permissions (%s) Burn Pool (%s)
            
            """;
    public static final String tokenStaticContentPart2 = """
            *Community*:
            \uD83D\uDC65 Number of Users: %s
            ⚖️ Profit/Loss Ratio: %s : %s
            """;
    public static final String tokenStaticContentPart3 = """
            *Links*:
            [X](%s) | [Telegram](%s)
            [DexScreener](%s) | [Birdeye](%s) | [Dextools](%s) | [Pump](%s) | [GMGN](%s)
            
            """;
    public static final String tokenWalletContentPart4Sniper = """
            *Sniping*:
            \uD83D\uDC5B Wallet Balance: %s SOL
            \uD83D\uDCB0 Sniping Amount: %s SOL
            \uD83D\uDCA8 Sniping Priority Fee: %s SOL
            """;
    public static final String tokenWalletContentPart4Deal = """
            *Wallet*:
            \uD83D\uDCB5 Current Balance: %s SOL
            \uD83D\uDCB1 Average Buy Price: %s USDT
            \uD83D\uDC8E Holding Value: %s SOL
            \uD83D\uDCB9 Holding Price Change: %s
            """;

    public static final String dealAutoSellNoConfig = "Currently no configurations";
    public static final String dealAutoSellConfigTitle = "Configured:\n";
    public static final String dealAutoSellConfigContent = "Config %s: Increase %s, Sell %s\n";
    public static final String dealAutoSellConfigCancel = "Cancel Configuration";
    public static final String initStartLanguageText = "\uD83C\uDF0D Switch Language\n";



}
