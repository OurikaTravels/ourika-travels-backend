package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Reservation;
import com.ouchin.ourikat.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    long countByStatus(ReservationStatus status);
    Optional<Reservation> findFirstByGuideIdOrderByReservationDateDesc(Long guideId);

    List<Reservation> getByTouristId(Long touristId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.tourist.id = :touristId")
    int countByTouristId(@Param("touristId") Long touristId);

    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r")
    long getTotalRevenue();
}