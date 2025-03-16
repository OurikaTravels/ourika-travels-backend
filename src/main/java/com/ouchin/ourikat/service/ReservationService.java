package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.ReservationRequestDto;
import com.ouchin.ourikat.dto.request.AssignGuideRequestDto;
import com.ouchin.ourikat.dto.response.ReservationResponseDto;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto createReservation(Long touristId, ReservationRequestDto request);
    void cancelReservation(Long reservationId);
    List<ReservationResponseDto> getAllReservations();
    ReservationResponseDto assignGuideToReservation(Long reservationId, AssignGuideRequestDto request);
    long getTotalReservations();
}