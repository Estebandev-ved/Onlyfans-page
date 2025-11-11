package com.proyecto.entity;

import jakarta.persistence.*;
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
@Table(name = "messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Sender is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @NotNull(message = "Receiver is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Size(max = 2000, message = "Message text must be less than 2000 characters")
    @Column(columnDefinition = "TEXT")
    private String messageText;

    @Enumerated(EnumType.STRING)
    private MessageType messageType = MessageType.TEXT;

    private String fileUrl;
    private String fileName;
    private Long fileSize;

    private boolean isDeleted = false;
    private boolean isRead = false;
    private LocalDateTime readAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods
    public boolean isTextMessage() {
        return messageType == MessageType.TEXT;
    }

    public boolean isFileMessage() {
        return messageType == MessageType.FILE || messageType == MessageType.IMAGE || 
               messageType == MessageType.VIDEO || messageType == MessageType.AUDIO;
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.messageText = "[Message deleted]";
        this.fileUrl = null;
        this.fileName = null;
        this.fileSize = null;
    }

    public boolean isOwner(User user) {
        return this.sender.getId().equals(user.getId());
    }

    // Enums
    public enum MessageType {
        TEXT, IMAGE, VIDEO, AUDIO, FILE
    }
}