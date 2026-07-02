package com.haider.LinkFlow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class UrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String originalUrl;
    private String shortCode;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity creator;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean active;
    private Long clickCount;
}
