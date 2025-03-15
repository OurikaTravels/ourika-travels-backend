package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.config.security.JwtTokenProvider;
import com.ouchin.ourikat.dto.request.LoginRequestDto;
import com.ouchin.ourikat.dto.request.TouristRegistrationRequestDto;
import com.ouchin.ourikat.dto.request.GuideRegistrationRequestDto;
import com.ouchin.ourikat.dto.response.LoginResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.entity.*;
import com.ouchin.ourikat.exception.AuthenticationFailedException;
import com.ouchin.ourikat.exception.EmailAlreadyExistsException;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
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

@Service
@Slf4j
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
    private final PasswordResetTokenRepository  passwordResetTokenRepository;


    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenProvider.generateToken(userDetails);

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            return new LoginResponseDto(jwt, user.getRole(), user.getEmail(), user.getLastName());
        } catch (BadCredentialsException e) {
            log.error("Invalid login credentials for email: {}", request.getEmail());
            throw new AuthenticationFailedException("Invalid email or password");
        }
    }

    @Override
    @Transactional
    public TouristResponseDto registerTourist(TouristRegistrationRequestDto request) {

        if (isEmailAlreadyRegistered(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Tourist tourist = touristMapper.toEntity(request);

        tourist.setPassword(passwordEncoder.encode(request.getPassword()));

        Tourist savedTourist = touristRepository.save(tourist);

        log.info("Tourist registered successfully: {}", savedTourist.getEmail());

        String token = UUID.randomUUID().toString();
        generateVerificationToken(savedTourist, token);

        emailService.sendVerificationEmail(savedTourist.getEmail(), token);

        return touristMapper.toResponseDto(savedTourist);
    }

    @Override
    @Transactional
    public GuideResponseDto registerGuide(GuideRegistrationRequestDto request) {
        if (isEmailAlreadyRegistered(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Guide guide = guideMapper.toEntity(request);

        guide.setPassword(passwordEncoder.encode(request.getPassword()));

        Guide savedGuide = guideRepository.save(guide);

        log.info("Guide registered successfully: {}", savedGuide.getEmail());

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
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }


    @Override
    @Transactional
    public void generatePasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());
    }


    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();

        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}