package com.rich.sol_bot.grpc_compile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangqiyun
 * @since 2024/10/17 19:38
 */

@Data
public class MyConfirmedTransaction {
    private Meta meta;
    private Transaction transaction;
    private String signature;

    @Data
    public static class Meta {
        private List<InnerInstructions> inner_instructions = new ArrayList<>();
        private Long fee;
        private List<String> loaded_writable_addresses = new ArrayList<>();
        private List<String> loaded_readonly_addresses = new ArrayList<>();
//        private LoadedAddresses loaded_addresses = new LoadedAddresses();
    }

    @Data
    public static class Transaction {
        private Message message;
    }

    @Data
    public static class Message {
        private ArrayList<String> account_keys = new ArrayList<>();
        private ArrayList<Instruction> instructions = new ArrayList<>();
    }

    @Data
    public static class Instruction {
        private Integer program_id_index;
        private String data;
        private String accounts;
    }

    @Data
    public static class LoadedAddresses {
        private List<String> writable = new ArrayList<>();
        private List<String> readonly = new ArrayList<>();
    }
}
