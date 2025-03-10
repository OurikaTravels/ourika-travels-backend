package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {
    Set<ServiceEntity> findByIdIn(Set<Long> ids);
    List<ServiceEntity> findByNameContainingIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
