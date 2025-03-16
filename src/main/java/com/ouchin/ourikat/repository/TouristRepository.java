package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {
    long count();
}
