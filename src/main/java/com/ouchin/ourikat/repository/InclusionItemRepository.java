package com.ouchin.ourikat.repository;


import com.ouchin.ourikat.entity.InclusionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InclusionItemRepository extends JpaRepository<InclusionItem, Long> {
}