package com.haider.LinkFlow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "url_clicks")
@Data
public class UrlClickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UrlEntity urlEntity;

    private Instant clickedAt;
    private String ipAddress;
    private String userAgent;
    private String referer;
}
