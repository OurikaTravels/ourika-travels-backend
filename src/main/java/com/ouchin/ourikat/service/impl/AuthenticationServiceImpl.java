package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.config.security.JwtTokenProvider;
import com.ouchin.ourikat.dto.request.*;
import com.ouchin.ourikat.dto.response.*;
import com.ouchin.ourikat.entity.*;
import com.ouchin.ourikat.enums.Role;
import com.ouchin.ourikat.exception.*;
import com.ouchin.ourikat.mapper.GuideMapper;
import com.ouchin.ourikat.mapper.TouristMapper;
import com.ouchin.ourikat.repository.*;
import com.ouchin.ourikat.service.AuthenticationService;
import com.ouchin.ourikat.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TouristRepository touristRepository;
    private final GuideRepository guideRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TouristMapper touristMapper;
    private final GuideMapper guideMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        log.debug("Attempting to log in user with email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == Role.GUIDE) {
            Guide guide = guideRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));

            if (!guide.getIsValidateGuide()) {
                throw new GuideNotValidatedException("Guide is not validated. Please contact the administrator.");
            }
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(userDetails);

        log.info("User logged in successfully: {}", user.getEmail());
        return new LoginResponseDto(
                user.getId(),
                jwt,
                user.getRole(),
                user.getEmail(),
                user.getLastName()
        );
    }

    @Override
    @Transactional
    public TouristResponseDto registerTourist(TouristRegistrationRequestDto request) {
        log.debug("Attempting to register tourist with email: {}", request.getEmail());

        if (isEmailAlreadyRegistered(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Tourist tourist = touristMapper.toEntity(request);
        tourist.setPassword(passwordEncoder.encode(request.getPassword()));
        tourist.setRole(Role.TOURIST);
        tourist.setVerified(false);

        Tourist savedTourist = touristRepository.save(tourist);
        log.info("Tourist registered successfully: {}", savedTourist.getEmail());

        String token = UUID.randomUUID().toString();
        generateVerificationToken(savedTourist, token);

        emailService.sendVerificationEmail(savedTourist.getEmail(), token);

        return touristMapper.toResponseDto(savedTourist);
    }

    @Override
    @Transactional
    public GuideResponseDto validateGuide(Long guideId) {
        log.debug("Validating guide with ID: {}", guideId);

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));

        guide.setIsValidateGuide(!guide.getIsValidateGuide());

        Guide validatedGuide = guideRepository.save(guide);

        log.info("Guide validated successfully: {}", validatedGuide.getEmail());
        return guideMapper.toResponseDto(validatedGuide);
    }

    @Override
    @Transactional
    public GuideResponseDto registerGuide(GuideRegistrationRequestDto request) {
        log.debug("Attempting to register guide with email: {}", request.getEmail());

        if (isEmailAlreadyRegistered(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Guide guide = guideMapper.toEntity(request);
        guide.setPassword(passwordEncoder.encode(request.getPassword()));
        guide.setRole(Role.GUIDE);
        guide.setVerified(false);

        Guide savedGuide = guideRepository.save(guide);

        String token = UUID.randomUUID().toString();
        generateVerificationToken(savedGuide, token);

        emailService.sendVerificationEmail(savedGuide.getEmail(), token);

        return guideMapper.toResponseDto(savedGuide);
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void generateVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        log.debug("Verifying email with token: {}", token);

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        log.info("Email verified successfully for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void generatePasswordResetToken(String email) {
        log.debug("Generating password reset token for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());

        log.info("Password reset token generated for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        log.debug("Resetting password with token: {}", token);

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        log.info("Password reset successfully for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void updateProfileImage(Long guideId, String fileName) {
        log.debug("Updating profile image for guide with ID: {}", guideId);

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));

        guide.setProfileImage(fileName);
        guideRepository.save(guide);

        log.info("Profile image updated for guide with ID: {}", guideId);
    }
}