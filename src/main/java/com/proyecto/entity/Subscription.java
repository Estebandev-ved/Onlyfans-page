package com.proyecto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Subscriber is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private User subscriber;

    @NotNull(message = "Creator is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @NotNull(message = "Subscription tier is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    private SubscriptionTier tier;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 2 decimal places")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status = SubscriptionStatus.PENDING;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private boolean autoRenew = true;
    private int renewalCount = 0;

    private String paymentMethodId;
    private String stripeSubscriptionId;
    private String stripeCustomerId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods
    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    public boolean isExpired() {
        return status == SubscriptionStatus.EXPIRED || 
               (endDate != null && LocalDateTime.now().isAfter(endDate));
    }

    public boolean isCancelled() {
        return status == SubscriptionStatus.CANCELLED || cancelledAt != null;
    }

    public boolean isRenewable() {
        return autoRenew && !isCancelled() && !isExpired();
    }

    public boolean isInTrial() {
        return status == SubscriptionStatus.TRIAL;
    }

    public long getDaysRemaining() {
        if (endDate == null) {
            return -1;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now(), endDate);
    }

    public long getDaysSinceStart() {
        if (startDate == null) {
            return -1;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, LocalDateTime.now());
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = LocalDateTime.now();
        if (this.endDate == null) {
            this.endDate = LocalDateTime.now().plusMonths(1);
        }
    }

    public void cancel(String reason) {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.cancellationReason = reason;
        this.autoRenew = false;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
        this.endDate = LocalDateTime.now();
    }

    public void renew() {
        this.renewalCount++;
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now().plusMonths(1);
        this.status = SubscriptionStatus.ACTIVE;
        this.cancelledAt = null;
        this.cancellationReason = null;
    }

    // Enums
    public enum SubscriptionStatus {
        PENDING, ACTIVE, CANCELLED, EXPIRED, TRIAL, SUSPENDED
    }
}