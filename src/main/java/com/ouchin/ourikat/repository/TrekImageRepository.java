package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.TrekImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrekImageRepository extends JpaRepository<TrekImage, Long> {
    List<TrekImage> findByTrekId(Long trekId);

    long countByTrekId(Long trekId);

    Optional<TrekImage> findFirstByTrekIdAndIdNot(Long trekId, Long imageId);

    boolean existsByTrekIdAndIsPrimaryTrue(Long trekId);


    @Modifying
    @Query("UPDATE TrekImage t SET t.isPrimary = false WHERE t.trek.id = :trekId AND t.isPrimary = true")
    void unsetAllPrimaryImagesForTrek(@Param("trekId") Long trekId);
}