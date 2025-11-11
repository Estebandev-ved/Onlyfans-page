package com.proyecto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "follows")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Follower is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @NotNull(message = "Following is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @Enumerated(EnumType.STRING)
    private FollowStatus status = FollowStatus.ACCEPTED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Helper methods
    public boolean isMutual() {
        return status == FollowStatus.ACCEPTED;
    }

    public boolean isPending() {
        return status == FollowStatus.PENDING;
    }

    public boolean isBlocked() {
        return status == FollowStatus.BLOCKED;
    }

    // Enums
    public enum FollowStatus {
        PENDING, ACCEPTED, BLOCKED
    }
}