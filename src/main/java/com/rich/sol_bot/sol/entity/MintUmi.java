package com.rich.sol_bot.sol.entity;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/25 17:20
 */

@Data
public class MintUmi {
    private Mint mint;
    private Metadata metadata;

    @Data
    public static class Mint {
        private String publicKey;
        private String mintAuthority;//铸造权限
        private String supply;//发行量
        private Integer decimals;//小数位数
        private String freezeAuthority;//冻结权限
    }

    @Data
    public static class Metadata {
        private String publicKey;
        private String updateAuthority;//代币meta信息更新权限
        private String mint;
        private String name;//代币名字
        private String symbol;//代币简称
        private String uri;//代币描述路径
    }
}
