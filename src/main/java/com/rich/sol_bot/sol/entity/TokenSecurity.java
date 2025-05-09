package com.rich.sol_bot.sol.entity;


/**
 * @author wangqiyun
 * @since 2024/3/17 23:33
 */

@lombok.Data
public class TokenSecurity {
    private Boolean success;
    private Integer statuscode;
    private Data data;

    @lombok.Data
    public static class Data {
        private String creatorAddress;
        private String creatorOwnerAddress;
        private String ownerAddress;
        private String ownerOfOwnerAddress;
        private String creationTx;
        private String creationTime;
        private String creationSlot;
        private String mintTx;
        private String mintTime;
        private String mintSlot;
        private String creatorBalance;
        private String ownerBalance;
        private String ownerPercentage;
        private String creatorPercentage;
        private String metaplexUpdateAuthority;
        private String metaplexOwnerUpdateAuthority;
        private String metaplexUpdateAuthorityBalance;
        private String metaplexUpdateAuthorityPercent;
        private String mutableMetadata;
        private String top10HolderBalance;
        private String top10HolderPercent;
        private String top10UserBalance;
        private String top10UserPercent;
        private String isTrueToken;
        private String totalSupply;
        private String lockInfo;
        private String freezeable;
        private String freezeAuthority;
        private String transferFeeEnable;
//        private String transferFeeData;
        private String isToken2022;
        private String nonTransferable;
    }
}
