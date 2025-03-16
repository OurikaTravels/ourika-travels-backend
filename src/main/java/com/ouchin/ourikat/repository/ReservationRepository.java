package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Reservation;
import com.ouchin.ourikat.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    long countByStatus(ReservationStatus status);
}