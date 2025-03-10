package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Trek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrekRepository extends JpaRepository<Trek, Long> {
}