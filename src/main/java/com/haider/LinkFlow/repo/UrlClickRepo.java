package com.haider.LinkFlow.repo;

import com.haider.LinkFlow.entity.UrlClickEntity;
import com.haider.LinkFlow.entity.UrlEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlClickRepo extends JpaRepository<UrlClickEntity, Long> {
    List<UrlClickEntity> findByUrlEntity(UrlEntity urlEntity, Pageable pageable);
}
