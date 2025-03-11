package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.TrekImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrekImageRepository extends JpaRepository<TrekImage, Long> {
    List<TrekImage> findByTrekId(Long trekId);
    long countByTrekId(Long trekId);
    Optional<TrekImage> findFirstByTrekIdAndIdNot(Long trekId, Long imageId);
    List<TrekImage> findByTrekIdAndIsPrimaryTrue(Long trekId);
    boolean existsByTrekIdAndIsPrimaryTrue(Long trekId);
}