package com.ouchin.ourikat.config;

import com.ouchin.ourikat.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()
                        //category
                        .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        //trek
                        .requestMatchers(HttpMethod.GET, "/treks").permitAll()
                        .requestMatchers(HttpMethod.POST, "/treks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/treks/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/treks/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/treks/{id}").hasRole("ADMIN")

                        // Highlight endpoints
                        .requestMatchers(HttpMethod.GET, "/highlights").permitAll()
                        .requestMatchers(HttpMethod.GET, "/highlights/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/highlights").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/highlights/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/highlights/{id}").hasRole("ADMIN")

                        // Trek-Highlight relationship management endpoints
                        .requestMatchers("/treks/{trekId}/highlights/**").hasRole("ADMIN")


                        // Services endpoints
                        .requestMatchers(HttpMethod.GET, "/services").permitAll()
                        .requestMatchers(HttpMethod.GET, "/services/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/services").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/services/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/services/{id}").hasRole("ADMIN")

                        // Trek-services relationship management endpoints
                        .requestMatchers("/treks/{trekId}/services/**").hasRole("ADMIN")


                        // Activity endpoints
                        .requestMatchers(HttpMethod.GET, "/activities/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/treks/{trekId}/activities").permitAll()
                        .requestMatchers(HttpMethod.POST, "/treks/{trekId}/activities").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/activities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/activities/**").hasRole("ADMIN")



                        // Trek Images endpoints
                        .requestMatchers(HttpMethod.GET, "/treks/{trekId}/images").permitAll()
                        .requestMatchers(HttpMethod.GET, "/treks/{trekId}/images/{fileName}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/treks/{trekId}/images").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/treks/{trekId}/images/{imageId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/treks/{trekId}/images/{imageId}/primary").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/treks/{trekId}/images/bulk").hasRole("ADMIN")

                        // Post endpoints for guides
                        .requestMatchers(HttpMethod.POST, "/posts/guides/{guideId}").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.GET, "/posts/guides/{guideId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/{postId}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/posts/{postId}").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.PUT, "/posts/{postId}/images").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.DELETE, "/posts/{postId}").hasRole("GUIDE")


                        // Add these to your security configuration
                        .requestMatchers(HttpMethod.POST, "/posts/{postId}/likes").authenticated()
                        .requestMatchers(HttpMethod.GET, "/posts/{postId}/likes/count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/{postId}/likes/status").authenticated()
                        .requestMatchers(HttpMethod.POST, "/posts/{postId}/comments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/posts/{postId}/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/{postId}/comments/count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/{postId}/comments/{commentId}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/posts/{postId}/comments/{commentId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/posts/{postId}/comments/{commentId}").authenticated()

                        .requestMatchers("/tourist/**").hasRole("TOURIST")
                        .requestMatchers("/guide/**").hasRole("GUIDE")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}