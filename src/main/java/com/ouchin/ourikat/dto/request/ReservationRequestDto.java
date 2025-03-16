package com.ouchin.ourikat.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {
    private Long trekId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}