package com.rich.sol_bot.sol.new_coin.mapper;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

/**
 * @author wangqiyun
 * @since 2024/3/24 17:29
 */

@Data
@Accessors(chain = true)
public class NewCoin {
    @TableId
    private String ammKey;
    private String baseMint;
    private String quoteMint;
    private Long poolOpenTime;
    private BigInteger baseAmount;
    private BigInteger quoteAmount;
}
