package com.ouchin.ourikat.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GuideRegistrationRequestDto {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Min(value = 1960, message = "Birth year must be after 1900")
    @Max(value = 2010, message = "Birth year must be before 2010")
    private Integer birthday;

    @NotBlank(message = "Language is required")
    @Size(min = 2, max = 100, message = "Language must be between 2 and 100 characters")
    private String language;

    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 31, message = "Experience cannot exceed 30 years")
    private Integer experience;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must contain 10-15 digits, optionally starting with +")
    private String phone;

    @NotBlank(message = "Speciality is required")
    @Size(min = 2, max = 100, message = "Speciality must be between 2 and 100 characters")
    private String speciality;

    @NotBlank(message = "License number is required")
    @Pattern(regexp = "^[A-Z0-9]{5,20}$", message = "License number must be 5-20 alphanumeric characters (uppercase)")
    private String licenseNumber;

    @Column(columnDefinition = "TEXT")
    private String aboutYou;
}