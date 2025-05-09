package com.rich.sol_bot.trade.params;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadBase64Param {
    private Long uid;
    private String base64Data;
}
