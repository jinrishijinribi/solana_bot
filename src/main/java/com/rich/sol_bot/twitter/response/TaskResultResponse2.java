package com.rich.sol_bot.twitter.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResultResponse2 {
    private String conversation_id_str;
    private String full_text;
    private String user_id_str;
    private String created_at;
}
