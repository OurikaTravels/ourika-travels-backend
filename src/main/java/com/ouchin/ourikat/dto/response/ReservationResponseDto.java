package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.entity.Reservation;
import com.ouchin.ourikat.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {
    private Long id;
    private Long touristId;
    private Long trekId;
    private Long guideId;
    private LocalDateTime reservationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    private ReservationStatus status;
}