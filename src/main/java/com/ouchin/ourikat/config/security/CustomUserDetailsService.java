package com.ouchin.ourikat.config.security;

import com.ouchin.ourikat.entity.User;
import com.ouchin.ourikat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user from the database by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Map your User entity to Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()) // Use roles() without the ROLE_ prefix
                .build();
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        // Strip the ROLE_ prefix if it exists
        String roleName = user.getRole().name();
        if (roleName.startsWith("ROLE_")) {
            roleName = roleName.substring(5); // Remove the "ROLE_" prefix
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
    }
}