package com.rich.sol_bot.wallet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletProfitStaticDTO {
    private Long profitCount;
    private Long loseCount;
}
