package com.ouchin.ourikat.dto.response;

import lombok.Data;

@Data
public class ReservationStatisticsResponseDto {
    private long totalReservations;
    private long totalPendingReservations;

    public ReservationStatisticsResponseDto(long totalReservations, long totalPendingReservations) {
        this.totalReservations = totalReservations;
        this.totalPendingReservations = totalPendingReservations;
    }
}