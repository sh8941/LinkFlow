package com.haider.LinkFlow.repo;

import com.haider.LinkFlow.entity.UrlClickEntity;
import com.haider.LinkFlow.entity.UrlEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface UrlClickRepo extends JpaRepository<UrlClickEntity, Long> {
    List<UrlClickEntity> findByUrlEntity(UrlEntity urlEntity, Pageable pageable);

    @Query("SELECT SUM(u.id) FROM UrlClickEntity u WHERE u.urlEntity.creator.id = :id AND u.urlEntity.active = TRUE")
    Long countCreatedBy(Long id);
    @Query("SELECT COUNT(DISTINCT u.ipAddress) FROM UrlClickEntity u WHERE u.urlEntity.creator.id = :id AND u.urlEntity.active = TRUE")
    Long countUniqueByIpAddress(Long id);

    @Query("SELECT u FROM UrlClickEntity u WHERE u.urlEntity = :url AND u.clickedAt BETWEEN :start AND :end")
    List<UrlClickEntity> findByUrlEntityAndCreatedBetween(UrlEntity url, Pageable unpaged, Instant start, Instant end);

    @Query("SELECT COUNT(DISTINCT u.ipAddress) FROM UrlClickEntity u WHERE u.urlEntity.creator.id = :id AND u.urlEntity.active = TRUE AND u.clickedAt BETWEEN :start AND :end")
    Long countUniqueByIpAddress(Long id, Instant start, Instant end);

    @Query("SELECT count(u.id) FROM UrlClickEntity u WHERE u.urlEntity.creator.id = :id AND u.urlEntity.active = TRUE AND u.clickedAt BETWEEN :start AND :end")
    Long clickCountByUserIdBetweenDate(Long id, Instant start, Instant end);
}
