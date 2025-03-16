package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.*;
import com.ouchin.ourikat.dto.response.*;
import com.ouchin.ourikat.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        try {
            log.info("Login attempt for user: {}", request.getEmail());
            LoginResponseDto response = authenticationService.login(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid email or password", null));
        }
    }

    @PostMapping("/register/tourist")
    public ResponseEntity<ApiResponse<TouristResponseDto>> registerTourist(
            @Valid @RequestBody TouristRegistrationRequestDto request) {
        try {
            log.info("Tourist registration attempt for: {}", request.getEmail());
            TouristResponseDto tourist = authenticationService.registerTourist(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tourist registered successfully", tourist));
        } catch (Exception e) {
            log.error("Tourist registration failed: {}", e.getMessage());
            String errorMessage = e.getMessage().contains("Email already in use")
                    ? "Email already in use"
                    : "Registration failed. Please try again.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, errorMessage, null));
        }
    }

    @PostMapping("/register/guide")
    public ResponseEntity<ApiResponse<GuideResponseDto>> registerGuide(
            @Valid @RequestBody GuideRegistrationRequestDto request) {
        try {
            log.info("Guide registration attempt for: {}", request.getEmail());
            GuideResponseDto guide = authenticationService.registerGuide(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Guide registered successfully", guide));
        } catch (Exception e) {
            log.error("Guide registration failed: {}", e.getMessage());
            String errorMessage = e.getMessage().contains("Email already in use")
                    ? "Email already in use"
                    : "Registration failed. Please try again.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, errorMessage, null));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        try {
            authenticationService.verifyEmail(token);
            return ResponseEntity.ok(new ApiResponse<>(true, "Email verified successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
        try {
            authenticationService.generatePasswordResetToken(email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Password reset email sent", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestParam String token,
            @Valid @RequestBody ResetPasswordRequestDto request) {
        try {
            authenticationService.resetPassword(token, request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PatchMapping("/validate-guide/{guideId}")
    public ResponseEntity<ApiResponse<GuideResponseDto>> validateGuide(@PathVariable Long guideId) {
        try {
            log.info("Validating guide with ID: {}", guideId);
            GuideResponseDto validatedGuide = authenticationService.validateGuide(guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Guide validated successfully", validatedGuide));
        } catch (Exception e) {
            log.error("Failed to validate guide: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}