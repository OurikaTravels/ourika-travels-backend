package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.GuideProfileUpdateDto;
import com.ouchin.ourikat.dto.request.TouristProfileUpdateDto;
import com.ouchin.ourikat.dto.response.AdminStatisticsResponseDto;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/tourists/{touristId}/profile")
    public ResponseEntity<ApiResponse<TouristResponseDto>> updateTouristProfile(
            @PathVariable Long touristId,
            @RequestBody TouristProfileUpdateDto request) {
        TouristResponseDto updatedTourist = userService.updateTouristProfile(touristId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tourist profile updated successfully", updatedTourist));
    }

    @PatchMapping("/guides/{guideId}/profile")
    public ResponseEntity<ApiResponse<GuideResponseDto>> updateGuideProfile(
            @PathVariable Long guideId,
            @RequestBody GuideProfileUpdateDto request) {
        GuideResponseDto updatedGuide = userService.updateGuideProfile(guideId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Guide profile updated successfully", updatedGuide));
    }

    @GetMapping("/admin/statistics")
    public ResponseEntity<ApiResponse<AdminStatisticsResponseDto>> getAdminStatistics() {
        AdminStatisticsResponseDto statistics = userService.getAdminStatistics();
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin statistics retrieved successfully", statistics));
    }
}