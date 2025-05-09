package com.rich.sol_bot.chain;

import com.rich.sol_bot.chain.dto.ChainBalance;

public interface ChainOperator {
    ChainBalance mainBalance(String userAddress);
    ChainBalance tokenBalance(String mint, String userAddress);
}
