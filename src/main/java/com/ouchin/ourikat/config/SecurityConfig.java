package com.ouchin.ourikat.config;

import com.ouchin.ourikat.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/uploads/images/**").permitAll()

                        //category
                        .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                        .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")

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
                        .requestMatchers(HttpMethod.GET, "/posts").permitAll()


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
                        .requestMatchers(HttpMethod.PATCH, "/auth/validate-guide/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/upload-profile-image").hasRole("ADMIN")

                        // Profile update endpoints
                        .requestMatchers(HttpMethod.PATCH, "/users/tourists/{touristId}/profile").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.PATCH, "/users/guides/{guideId}/profile").hasRole("GUIDE")

                        // Admin statistics endpoint
                        .requestMatchers(HttpMethod.GET, "/users/admin/statistics").hasRole("ADMIN")



                        .requestMatchers(HttpMethod.POST, "/reservations/tourists/{touristId}/reserve").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.PATCH, "/reservations/{reservationId}/cancel").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.GET, "/reservations").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/reservations/{reservationId}/assign-guide").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservations/statistics").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/reservations/{reservationId}/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/reservations/{reservationId}/cancel-by-admin").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.POST, "/wishlists/tourists/{touristId}/add").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.DELETE, "/wishlists/tourists/{touristId}/remove/{trekId}").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.GET, "/wishlists/tourists/{touristId}").hasRole("TOURIST")



                        .requestMatchers(HttpMethod.GET, "/users/tourists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/guides/ordered-by-reservation-date").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/guides").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/auth/validate-guide/{guideId}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/guides/{guideId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/reservation/notify-guide/{guideId}").hasRole("GUIDE")

                        .requestMatchers(HttpMethod.GET, "/users/tourists/{touristId}").hasRole("TOURIST")

                        .requestMatchers(HttpMethod.GET, "/posts").permitAll()

                        .requestMatchers(HttpMethod.GET, "/treks/category/{categoryId}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/treks/search").permitAll()

                        .requestMatchers(HttpMethod.GET, "/reservations/tourist/{touristId}").hasRole("TOURIST")

                        .requestMatchers(HttpMethod.GET, "/wishlists/count/{touristId}").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.GET, "/wishlists/trek-ids/{touristId}").hasRole("TOURIST")

                        .requestMatchers(HttpMethod.GET, "/reservations/count/{touristId}").hasRole("TOURIST")

                        .requestMatchers(HttpMethod.GET, "/posts/liked-posts/{touristId}").authenticated()

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