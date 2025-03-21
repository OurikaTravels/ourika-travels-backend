package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.ReservationRequestDto;
import com.ouchin.ourikat.dto.request.AssignGuideRequestDto;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.ReservationResponseDto;
import com.ouchin.ourikat.dto.response.ReservationStatisticsResponseDto;
import com.ouchin.ourikat.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/tourists/{touristId}/reserve")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> createReservation(
            @PathVariable Long touristId,
            @RequestBody ReservationRequestDto request) {
        ReservationResponseDto reservation = reservationService.createReservation(touristId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Reservation created successfully", reservation));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reservation cancelled successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(new ApiResponse<>(true, "Reservations retrieved successfully", reservations));
    }

    @PatchMapping("/{reservationId}/assign-guide")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> assignGuideToReservation(
            @PathVariable Long reservationId,
            @RequestBody AssignGuideRequestDto request) {
        ReservationResponseDto reservation = reservationService.assignGuideToReservation(reservationId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Guide assigned successfully", reservation));
    }

    @PatchMapping("/{reservationId}/approve")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> approveReservation(@PathVariable Long reservationId) {
        ReservationResponseDto reservation = reservationService.approveReservation(reservationId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reservation approved successfully", reservation));
    }

    @PatchMapping("/{reservationId}/cancel-by-admin")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> cancelReservationByAdmin(@PathVariable Long reservationId) {
        ReservationResponseDto reservation = reservationService.cancelReservationByAdmin(reservationId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reservation cancelled by admin", reservation));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<ReservationStatisticsResponseDto>> getReservationStatistics() {
        ReservationStatisticsResponseDto statistics = reservationService.getReservationStatistics();
        return ResponseEntity.ok(new ApiResponse<>(true, "Reservation statistics retrieved successfully", statistics));
    }


    @GetMapping("/notify-guide/{guideId}")
    public ResponseEntity<ReservationResponseDto> notifyGuideAboutLatestReservation(@PathVariable Long guideId) {
        ReservationResponseDto response = reservationService.notifyGuideAboutLatestReservation(guideId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/tourist/{touristId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByTouristId(@PathVariable Long touristId) {
        List<ReservationResponseDto> reservations = reservationService.getAllByTouristId(touristId);
        return ResponseEntity.ok(reservations);
    }


}