package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.GuideProfileUpdateDto;
import com.ouchin.ourikat.dto.request.TouristProfileUpdateDto;
import com.ouchin.ourikat.dto.response.AdminStatisticsResponseDto;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/tourists")
    public ResponseEntity<ApiResponse<List<TouristResponseDto>>> getAllTourists() {
        List<TouristResponseDto> tourists = userService.getAllTourists();
        return ResponseEntity.ok(new ApiResponse<>(true, "Tourists retrieved successfully", tourists));
    }

    @GetMapping("/guides/ordered-by-reservation-date")
    public ResponseEntity<ApiResponse<List<GuideResponseDto>>> getAllGuidesOrderByReservationAssignmentDate() {
        List<GuideResponseDto> guides = userService.getAllGuidesOrderByReservationAssignmentDate();
        return ResponseEntity.ok(new ApiResponse<>(true, "Guides retrieved successfully", guides));
    }

    @GetMapping("/guides")
    public ResponseEntity<ApiResponse<List<GuideResponseDto>>> getAllGuides() {
        List<GuideResponseDto> guides = userService.getAllGuides();
        return ResponseEntity.ok(new ApiResponse<>(true, "Guides retrieved successfully", guides));
    }

    @GetMapping("/guides/{guideId}")
    public ResponseEntity<ApiResponse<GuideResponseDto>> getGuideById(@PathVariable Long guideId) {
        try {
            // Fetch the guide by ID
            GuideResponseDto guide = userService.getGuideById(guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Guide fetched successfully", guide));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to fetch guide: " + e.getMessage(), null));
        }
    }
}