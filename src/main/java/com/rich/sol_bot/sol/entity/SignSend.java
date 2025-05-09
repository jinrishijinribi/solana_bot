package com.rich.sol_bot.sol.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SignSend {
    private List<String> secrets;
    private String transaction;
    private Boolean dedicated;
}
