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
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Reporter is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @NotBlank(message = "Reason is required")
    @Size(max = 1000, message = "Reason must be less than 1000 characters")
    private String reason;

    private String description;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    private String adminNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    private LocalDateTime resolvedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods
    public boolean isPending() {
        return status == ReportStatus.PENDING;
    }

    public boolean isResolved() {
        return status == ReportStatus.RESOLVED;
    }

    public boolean isDismissed() {
        return status == ReportStatus.DISMISSED;
    }

    public void resolve(User admin) {
        this.status = ReportStatus.RESOLVED;
        this.reviewedBy = admin;
        this.resolvedAt = LocalDateTime.now();
    }

    public void dismiss(User admin) {
        this.status = ReportStatus.DISMISSED;
        this.reviewedBy = admin;
        this.resolvedAt = LocalDateTime.now();
    }

    // Enums
    public enum ReportType {
        SPAM, HARASSMENT, INAPPROPRIATE_CONTENT, COPYRIGHT_INFRINGEMENT, IMPERSONATION, OTHER
    }

    public enum ReportStatus {
        PENDING, REVIEWING, RESOLVED, DISMISSED
    }
}