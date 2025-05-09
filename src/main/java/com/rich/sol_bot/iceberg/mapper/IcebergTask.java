package com.rich.sol_bot.iceberg.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class IcebergTask {
    private Long id;
    private Long uid;
    private Long successCount;
    private Long failCount;
    private Long allCount;
    private Long submitCount;
    private String tokenAccount;
    private Timestamp createdAt;
}
