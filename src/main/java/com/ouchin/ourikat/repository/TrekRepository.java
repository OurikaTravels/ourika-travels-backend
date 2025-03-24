package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Trek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrekRepository extends JpaRepository<Trek, Long> {
    List<Trek> findByCategoryId(Long categoryId);
    List<Trek> findByTitleContainingIgnoreCase(String title);
}