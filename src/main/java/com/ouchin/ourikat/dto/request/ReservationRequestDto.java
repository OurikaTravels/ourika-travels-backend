package com.ouchin.ourikat.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {
    @NotNull(message = "Trek ID cannot be null")
    private Long trekId;

    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDateTime startDate;

    @Column(nullable = false)
        private int adultCount;

    @Column(nullable = false)
    private int childCount;


    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;


}