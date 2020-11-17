package com.gomito.Gomitobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GUserDto {
    private Long userId;
    private String username;
    private String email;
    private String avatarUrl;
}
