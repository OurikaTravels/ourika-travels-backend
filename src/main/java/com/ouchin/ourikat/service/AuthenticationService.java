package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.LoginRequestDto;
import com.ouchin.ourikat.dto.request.TouristRegistrationRequestDto;
import com.ouchin.ourikat.dto.request.GuideRegistrationRequestDto;
import com.ouchin.ourikat.dto.response.LoginResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.entity.User;

public interface AuthenticationService {

    LoginResponseDto login(LoginRequestDto request);
    TouristResponseDto registerTourist(TouristRegistrationRequestDto request);
    GuideResponseDto registerGuide(GuideRegistrationRequestDto request);
    boolean isEmailAlreadyRegistered(String email);
    void generateVerificationToken(User user, String token);
    void verifyEmail(String token);
    void generatePasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
    GuideResponseDto validateGuide(Long guideId);
    void updateProfileImage(Long guideId, String fileName);
}