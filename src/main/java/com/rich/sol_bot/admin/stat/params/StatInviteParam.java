package com.rich.sol_bot.admin.stat.params;

import com.rich.sol_bot.admin.stat.enums.OrderValue;
import com.rich.sol_bot.system.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class StatInviteParam extends PageQuery {
    @Schema(description = "用户名")
    private String username;
    @NotEmpty
    @Schema(description = "排序字段")
    private OrderValue orderValue;
    @NotEmpty
    @Schema(description = "正序/倒序")
    private Boolean asc;
}
