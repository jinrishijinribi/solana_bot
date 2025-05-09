package com.rich.sol_bot.trade.error;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionErrorService {
    public static String gasLimit = "insufficient lamports";
    public static String accountFreeze = "Program log: Error: Account is frozen";
    public static String exceedsSlippage = "Program log: Error: exceeds desired slippage limit";

    public String getError(List<String> logs) {
        for (String log : logs) {
            if(log.contains(gasLimit)) return """
                    余额不足，请先充值！""";
            if(log.contains(accountFreeze)) return """
                    您的钱包地址已被此CA拉黑，请更换钱包交易！""";
            if(log.contains(exceedsSlippage)) return """
                    超过滑点，请重新设置！""";
        }
        return """
                请预留足够的余额以支付gas费、滑点等费用！""";
    }
}
