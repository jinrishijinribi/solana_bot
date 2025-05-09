package com.rich.sol_bot.grpc_compile;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/11/25 18:09
 */

@Data
public class MyTransaction {
    private UpdateOneOf UpdateOneof;

    @Data
    public static class UpdateOneOf {
        private Transaction Transaction;
    }

    @Data
    public static class Transaction {
        private Long slot;
        private MyConfirmedTransaction transaction;
    }
}
