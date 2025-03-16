package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {
    private Long id;
    private TouristResponseDto tourist;
    private TrekResponse trek;
    private GuideResponseDto guide;
    private LocalDateTime reservationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    private ReservationStatus status;
}