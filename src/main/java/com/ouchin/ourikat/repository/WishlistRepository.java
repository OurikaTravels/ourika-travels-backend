package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByTouristId(Long touristId);
    boolean existsByTouristIdAndTrekId(Long touristId, Long trekId);
    void deleteByTouristIdAndTrekId(Long touristId, Long trekId);

    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.tourist.id = :touristId")
    int countByTouristId(@Param("touristId") Long touristId);

    @Query("SELECT w.trek.id FROM Wishlist w WHERE w.tourist.id = :touristId")
    List<Long> findTrekIdsByTouristId(@Param("touristId") Long touristId);
}