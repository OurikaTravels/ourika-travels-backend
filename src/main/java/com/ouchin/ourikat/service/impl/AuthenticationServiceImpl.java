package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.config.security.JwtTokenProvider;
import com.ouchin.ourikat.dto.request.LoginRequestDto;
import com.ouchin.ourikat.dto.request.TouristRegistrationRequestDto;
import com.ouchin.ourikat.dto.request.GuideRegistrationRequestDto;
import com.ouchin.ourikat.dto.response.LoginResponseDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Tourist;
import com.ouchin.ourikat.entity.User;
import com.ouchin.ourikat.exception.AuthenticationFailedException;
import com.ouchin.ourikat.exception.EmailAlreadyExistsException;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.GuideMapper;
import com.ouchin.ourikat.mapper.TouristMapper;
import com.ouchin.ourikat.repository.GuideRepository;
import com.ouchin.ourikat.repository.TouristRepository;
import com.ouchin.ourikat.repository.UserRepository;
import com.ouchin.ourikat.service.AuthenticationService;
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

            // Generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenProvider.generateToken(userDetails);

            // Fetch user details
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Return the role without the ROLE_ prefix
            return new LoginResponseDto(jwt, user.getRole(), user.getEmail());
        } catch (BadCredentialsException e) {
            log.error("Invalid login credentials for email: {}", request.getEmail());
            throw new AuthenticationFailedException("Invalid email or password");
        }
    }

    @Override
    @Transactional
    public TouristResponseDto registerTourist(TouristRegistrationRequestDto request) {
        // Check if email is already registered
        if (isEmailAlreadyRegistered(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already in use");
        }

        // Map request to Tourist entity
        Tourist tourist = touristMapper.toEntity(request);

        // Encode password
        tourist.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save tourist
        Tourist savedTourist = touristRepository.save(tourist);

        log.info("Tourist registered successfully: {}", savedTourist.getEmail());

        // Map and return response
        return touristMapper.toResponseDto(savedTourist);
    }

    @Override
    @Transactional
    public GuideResponseDto registerGuide(GuideRegistrationRequestDto request) {
        // Check if email is already registered
        if (isEmailAlreadyRegistered(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already in use");
        }

        // Map request to Guide entity
        Guide guide = guideMapper.toEntity(request);

        // Encode password
        guide.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save guide
        Guide savedGuide = guideRepository.save(guide);

        log.info("Guide registered successfully: {}", savedGuide.getEmail());

        // Map and return response
        return guideMapper.toResponseDto(savedGuide);
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }
}