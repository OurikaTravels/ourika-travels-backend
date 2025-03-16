package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.GuideProfileUpdateDto;
import com.ouchin.ourikat.dto.request.TouristProfileUpdateDto;
import com.ouchin.ourikat.dto.response.AdminStatisticsResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;

import java.util.List;

public interface UserService {
    TouristResponseDto updateTouristProfile(Long touristId, TouristProfileUpdateDto request);
    GuideResponseDto updateGuideProfile(Long guideId, GuideProfileUpdateDto request);
    AdminStatisticsResponseDto getAdminStatistics();


    List<TouristResponseDto> getAllTourists();
    List<GuideResponseDto> getAllGuidesOrderByReservationAssignmentDate();
    List<GuideResponseDto> getAllGuides();
}