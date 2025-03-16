package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    long count();
    long countByIsValidateGuide(Boolean isValidateGuide);
    @Query("SELECT g FROM Guide g " +
            "LEFT JOIN g.reservations r " +
            "ORDER BY (SELECT MAX(r2.reservationDate) FROM Reservation r2 WHERE r2.guide = g) DESC NULLS LAST")
    List<Guide> findAllGuidesOrderByReservationAssignmentDate();
}
