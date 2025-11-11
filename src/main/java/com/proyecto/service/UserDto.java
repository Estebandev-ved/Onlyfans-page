package com.proyecto.service;

import com.proyecto.entity.User;
import com.proyecto.entity.User.UserRole;
import com.proyecto.entity.User.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String profileImageUrl;
    private String coverImageUrl;
    private String phoneNumber;
    private User.UserRole role;
    private User.AccountStatus status;
    private boolean emailVerified;
    private boolean phoneVerified;
    private boolean twoFactorEnabled;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}