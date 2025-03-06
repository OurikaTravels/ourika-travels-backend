package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.LoginRequestDto;
import com.ouchin.ourikat.dto.request.TouristRegistrationRequestDto;
import com.ouchin.ourikat.dto.request.GuideRegistrationRequestDto;
import com.ouchin.ourikat.dto.response.LoginResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;

public interface AuthenticationService {
    /**
     * Authenticate user and generate JWT token
     * @param request Login request with email and password
     * @return Login response with JWT token
     */
    LoginResponseDto login(LoginRequestDto request);

    /**
     * Register a new tourist user
     * @param request Tourist registration details
     * @return Registered tourist response
     */
    TouristResponseDto registerTourist(TouristRegistrationRequestDto request);

    /**
     * Register a new guide user
     * @param request Guide registration details
     * @return Registered guide response
     */
    GuideResponseDto registerGuide(GuideRegistrationRequestDto request);

    /**
     * Check if email is already registered
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    boolean isEmailAlreadyRegistered(String email);
}