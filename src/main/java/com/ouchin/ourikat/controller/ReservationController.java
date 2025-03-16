package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.ReservationRequestDto;
import com.ouchin.ourikat.dto.request.AssignGuideRequestDto;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.ReservationResponseDto;
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

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Long>> getTotalReservations() {
        long totalReservations = reservationService.getTotalReservations();
        return ResponseEntity.ok(new ApiResponse<>(true, "Total reservations retrieved", totalReservations));
    }
}