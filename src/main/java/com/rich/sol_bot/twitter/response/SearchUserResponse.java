package com.rich.sol_bot.twitter.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchUserResponse {
    private Long id;
    private String name;
    private String username;
}
