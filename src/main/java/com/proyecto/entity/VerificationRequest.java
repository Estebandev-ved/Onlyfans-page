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
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @NotBlank(message = "Document URL is required")
    @Size(max = 500, message = "Document URL must be less than 500 characters")
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status = VerificationStatus.PENDING;

    private String adminNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    private LocalDateTime reviewedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods
    public boolean isPending() {
        return status == VerificationStatus.PENDING;
    }

    public boolean isApproved() {
        return status == VerificationStatus.APPROVED;
    }

    public boolean isRejected() {
        return status == VerificationStatus.REJECTED;
    }

    public void approve(User admin) {
        this.status = VerificationStatus.APPROVED;
        this.reviewedBy = admin;
        this.reviewedAt = LocalDateTime.now();
    }

    public void reject(User admin, String notes) {
        this.status = VerificationStatus.REJECTED;
        this.reviewedBy = admin;
        this.reviewedAt = LocalDateTime.now();
        this.adminNotes = notes;
    }

    // Enums
    public enum DocumentType {
        ID_CARD, PASSPORT, DRIVERS_LICENSE, BUSINESS_LICENSE, UTILITY_BILL
    }

    public enum VerificationStatus {
        PENDING, APPROVED, REJECTED
    }
}