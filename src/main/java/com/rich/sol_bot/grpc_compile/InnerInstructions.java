package com.rich.sol_bot.grpc_compile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangqiyun
 * @since 2024/10/17 16:45
 */

@Data
public class InnerInstructions {
    private Integer index;
    private List<MyConfirmedTransaction.Instruction> instructions = new ArrayList<>();
}
