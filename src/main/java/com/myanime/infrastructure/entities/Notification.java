package com.myanime.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "type")
    private String type;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "content")
    private String content;

    @Column(name = "payload")
    private String payload;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (isRead == null) {
            isRead = false;
        }
    }
}

