package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    long count();
    long countByIsValidateGuide(Boolean isValidateGuide);
}
