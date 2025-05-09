package com.rich.sol_bot.trade.params;

import com.rich.sol_bot.trade.enums.OHLCVTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OHLCVTParam {
    private String address;
    private OHLCVTypeEnum type;
    private Long timeFrom;
    private Long timeTo;
}
