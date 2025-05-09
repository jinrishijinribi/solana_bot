package com.rich.sol_bot.admin.stat.dto;

import com.rich.sol_bot.admin.stat.mapper.StatInvite;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StatInviteDTO {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "TG用户名")
    private String username;
    @Schema(description = "TG昵称")
    private String nickname;
    @Schema(description = "邀请人数")
    private Long inviteCount;
    @Schema(description = "今日邀请人数")
    private Long inviteCountToday;
    @Schema(description = "有效邀请人数")
    private Long validCount;
    @Schema(description = "总邀请交易额")
    private BigDecimal tradeAmount;
    @Schema(description = "今日邀请交易额")
    private BigDecimal tradeAmountToday;
    @Schema(description = "总佣金")
    private BigDecimal rebate;
    @Schema(description = "今日佣金")
    private BigDecimal rebateToday;
    @Schema(description = "返佣比例")
    private BigDecimal rebateRate;

    public static StatInviteDTO transfer(StatInvite item) {
        return StatInviteDTO.builder()
                .id(item.getId()).username(item.getUsername()).nickname(item.getNickname())
                .inviteCount(item.getInviteCount()).inviteCountToday(item.getInviteCountToday())
                .validCount(item.getValidCount()).tradeAmount(item.getTradeAmount())
                .tradeAmountToday(item.getTradeAmountToday())
                .rebate(item.getRebate()).rebateToday(item.getRebateToday()).rebateRate(item.getRebateRate())
                .id(item.getId())
                .build();
    }


}
