package com.haider.LinkFlow.repo;

import com.haider.LinkFlow.entity.UrlEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface UrlRepo extends JpaRepository<UrlEntity, Long> {
    boolean existsByShortCode(String code);

    Optional<UrlEntity> findByShortCodeAndActiveTrue(String shortCode);

    List<UrlEntity> findByCreator_IdAndActiveTrue(Long creatorId, Pageable pageable);

    @Query("SELECT COUNT(u.id) FROM UrlEntity u WHERE u.creator.id = :id AND u.active = TRUE")
    Long countByUserId(Long id);

    @Query("SELECT SUM(u.clickCount) FROM UrlEntity u WHERE u.creator.id = :id AND u.active = TRUE")
    Long clickCountByUserId(Long id);

    @Query("SELECT COUNT(u.id) FROM UrlEntity u WHERE u.creator.id = :id AND u.active = TRUE AND u.createdAt BETWEEN :startDate AND :endDate")
    Long countByUserIdBetweenDates(Long id, Instant startDate, Instant endDate);

  @Query("SELECT SUM(u.clickCount) FROM UrlEntity u WHERE u.creator.id = :id AND u.active = TRUE AND u.createdAt BETWEEN :startDate AND :endDate")
  Long clickCountByUserIdBetweenDate(Long id, Instant startDate, Instant endDate);

    List<UrlEntity> findTop5ByCreator_IdAndActiveTrueOrderByClickCountDesc(Long id);

}
