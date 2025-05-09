package com.rich.sol_bot.sol.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SignSendJitoResult {
    private String result;
    private List<String> txid;
}
