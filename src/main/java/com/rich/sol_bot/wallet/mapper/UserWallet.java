package com.rich.sol_bot.wallet.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserWallet {
    private Long id;
    private Long uid;
    private String name;
    private String address;
    private String priKey;
    private Timestamp createdAt;
    private Integer deleted;
}
