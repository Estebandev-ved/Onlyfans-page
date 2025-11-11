package com.proyecto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 1000, message = "Message must be less than 1000 characters")
    private String message;

    private String actionUrl;
    private boolean isRead = false;
    private LocalDateTime readAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    // Helper methods
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isActionable() {
        return actionUrl != null && !actionUrl.isEmpty();
    }

    // Enums
    public enum NotificationType {
        NEW_FOLLOWER,
        NEW_SUBSCRIPTION,
        NEW_TIP,
        NEW_MESSAGE,
        CONTENT_LIKE,
        CONTENT_COMMENT,
        CONTENT_MENTION,
        SUBSCRIPTION_RENEWAL,
        SUBSCRIPTION_EXPIRING,
        SYSTEM_UPDATE,
        PROMOTIONAL
    }
}