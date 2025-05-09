package com.rich.sol_bot.admin.stat.dto;

import com.rich.sol_bot.user.mapper.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    @Schema(description = "username")
    private String username;
    @Schema(description = "用户id")
    private Long id;

    public static UserDTO transfer(User item) {
        return UserDTO.builder().id(item.getId()).username(item.getUsername())
                .build();
    }
}
