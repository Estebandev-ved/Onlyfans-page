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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscription_tiers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionTier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Creator is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @NotBlank(message = "Tier name is required")
    @Size(max = 100, message = "Tier name must be less than 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Billing period is required")
    @Enumerated(EnumType.STRING)
    private BillingPeriod billingPeriod;

    @Min(value = 0, message = "Trial days cannot be negative")
    @Max(value = 365, message = "Trial days cannot exceed 365")
    private Integer trialDays = 0;

    @ElementCollection
    @CollectionTable(name = "tier_benefits", joinColumns = @JoinColumn(name = "tier_id"))
    @Column(name = "benefit")
    private List<String> benefits = new ArrayList<>();

    @Min(value = 1, message = "Sort order must be at least 1")
    private Integer sortOrder = 1;

    private boolean isActive = true;
    private String welcomeMessage;

    @Min(value = 0, message = "Discount percentage cannot be negative")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    private Integer discountPercentage = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // Relationships
    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subscription> subscriptions = new ArrayList<>();

    // Helper methods
    public BigDecimal getDiscountedPrice() {
        if (discountPercentage == null || discountPercentage == 0) {
            return price;
        }
        BigDecimal discount = price.multiply(BigDecimal.valueOf(discountPercentage))
                .divide(BigDecimal.valueOf(100));
        return price.subtract(discount);
    }

    public boolean hasTrial() {
        return trialDays != null && trialDays > 0;
    }

    public boolean isAvailable() {
        return isActive && deletedAt == null;
    }

    public long getTotalSubscribers() {
        return subscriptions.stream()
                .filter(sub -> sub.getStatus() == Subscription.SubscriptionStatus.ACTIVE)
                .count();
    }

    public BigDecimal getMonthlyRevenue() {
        return subscriptions.stream()
                .filter(sub -> sub.getStatus() == Subscription.SubscriptionStatus.ACTIVE)
                .map(Subscription::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addBenefit(String benefit) {
        if (this.benefits == null) {
            this.benefits = new ArrayList<>();
        }
        this.benefits.add(benefit);
    }

    public void removeBenefit(String benefit) {
        if (this.benefits != null) {
            this.benefits.remove(benefit);
        }
    }

    public void activate() {
        this.isActive = true;
        this.deletedAt = null;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void delete() {
        this.isActive = false;
        this.deletedAt = LocalDateTime.now();
    }

    // Enums
    public enum BillingPeriod {
        MONTHLY, QUARTERLY, YEARLY, LIFETIME
    }
}