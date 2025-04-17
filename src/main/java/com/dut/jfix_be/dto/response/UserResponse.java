package com.dut.jfix_be.dto.response;

import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
    private UserRole role;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String refreshToken;
    private String accessToken;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .build();
    }

    public static UserResponse fromUserWithTokens(User user, String accessToken, String refreshToken) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}