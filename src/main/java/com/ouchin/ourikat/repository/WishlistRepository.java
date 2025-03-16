package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByTouristId(Long touristId);
    boolean existsByTouristIdAndTrekId(Long touristId, Long trekId);
    void deleteByTouristIdAndTrekId(Long touristId, Long trekId);
}