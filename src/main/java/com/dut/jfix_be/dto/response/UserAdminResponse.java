package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

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
public class UserAdminResponse {
    private Integer id;
    private String username;
    private UserRole role;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private boolean isDeleted;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private LocalDateTime deleteDate;
    private String deleteBy;

    public static UserAdminResponse fromUser(User user) {
        return UserAdminResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .isDeleted(user.isDeleted())
                .createDate(user.getCreateDate())
                .createBy(user.getCreateBy())
                .updateDate(user.getUpdateDate())
                .updateBy(user.getUpdateBy())
                .deleteDate(user.getDeleteDate())
                .deleteBy(user.getDeleteBy())
                .build();
    }
} 