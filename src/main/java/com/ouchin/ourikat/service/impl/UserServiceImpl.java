package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.GuideProfileUpdateDto;
import com.ouchin.ourikat.dto.request.TouristProfileUpdateDto;
import com.ouchin.ourikat.dto.response.AdminStatisticsResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Tourist;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.GuideMapper;
import com.ouchin.ourikat.mapper.TouristMapper;
import com.ouchin.ourikat.repository.GuideRepository;
import com.ouchin.ourikat.repository.TouristRepository;
import com.ouchin.ourikat.repository.UserRepository;
import com.ouchin.ourikat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TouristRepository touristRepository;
    private final GuideRepository guideRepository;
    private final UserRepository userRepository;
    private final TouristMapper touristMapper;
    private final GuideMapper guideMapper;

    @Override
    @Transactional
    public TouristResponseDto updateTouristProfile(Long touristId, TouristProfileUpdateDto request) {
        Tourist tourist = touristRepository.findById(touristId)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist not found"));

        tourist.setFirstName(request.getFirstName());
        tourist.setLastName(request.getLastName());

        Tourist updatedTourist = touristRepository.save(tourist);
        return touristMapper.toResponseDto(updatedTourist);
    }

    @Override
    @Transactional
    public GuideResponseDto updateGuideProfile(Long guideId, GuideProfileUpdateDto request) {
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));

        guide.setFirstName(request.getFirstName());
        guide.setLastName(request.getLastName());
        guide.setPhone(request.getPhone());
        guide.setAboutYou(request.getAboutYou());
        guide.setSpeciality(request.getSpeciality());
        guide.setLanguage(request.getLanguage());
        guide.setExperience(request.getExperience());

        Guide updatedGuide = guideRepository.save(guide);
        return guideMapper.toResponseDto(updatedGuide);
    }

    @Override
    public AdminStatisticsResponseDto getAdminStatistics() {
        long totalUsers = userRepository.count();
        long totalTourists = touristRepository.count();
        long totalGuides = guideRepository.count();
        long verifiedUsers = userRepository.countByVerified(true);
        long validatedGuides = guideRepository.countByIsValidateGuide(true);
        long nonValidatedGuides = guideRepository.countByIsValidateGuide(false);

        return new AdminStatisticsResponseDto(
                totalUsers,
                totalTourists,
                totalGuides,
                verifiedUsers,
                validatedGuides,
                nonValidatedGuides
        );
    }


    @Override
    public List<TouristResponseDto> getAllTourists() {
        List<Tourist> tourists = touristRepository.findAll();
        return tourists.stream()
                .map(touristMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GuideResponseDto> getAllGuidesOrderByReservationAssignmentDate() {
        List<Guide> guides = guideRepository.findAllGuidesOrderByReservationAssignmentDate();
        return guides.stream()
                .map(guideMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GuideResponseDto> getAllGuides() {
        List<Guide> guides = guideRepository.findAll();
        return guides.stream()
                .map(guideMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}