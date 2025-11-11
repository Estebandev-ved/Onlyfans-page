package com.proyecto.service;

import com.proyecto.entity.User;
import com.proyecto.entity.User.UserRole;
import com.proyecto.entity.User.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private UUID id;
    private String username;
    private String displayName;
    private String bio;
    private String profileImageUrl;
    private String coverImageUrl;
    private User.UserRole role;
    private User.AccountStatus status;
    private boolean emailVerified;
    private boolean isCreator;
    private boolean isOnline;

    // Statistics
    private int totalSubscribers;
    private int totalFollowing;
    private double totalEarnings;
    private int contentCount;
    private int viewCount;
    private int likeCount;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;

    // Creator-specific fields
    private BigDecimal subscriptionPrice;
    private String subscriptionCurrency;
    private boolean hasSubscriptionTiers;
    private boolean isAcceptingTips;
    private boolean isAcceptingCustomRequests;
}