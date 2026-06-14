package com.haider.LinkFlow.repo;

import com.haider.LinkFlow.entity.UrlEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UrlRepo extends JpaRepository<UrlEntity, Long> {
    boolean existsByShortCode(String code);

    Optional<UrlEntity> findByShortCodeAndActiveTrue(String shortCode);

    List<UrlEntity> findByCreator_IdAndActiveTrue(Long creatorId, Pageable pageable);
}
