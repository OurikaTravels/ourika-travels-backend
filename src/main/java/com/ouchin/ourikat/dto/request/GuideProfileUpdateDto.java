package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class GuideProfileUpdateDto {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must contain 10-15 digits, optionally starting with +")
    private String phone;

    @Size(max = 500, message = "About you must be less than 500 characters")
    private String aboutYou;

    @NotBlank(message = "Speciality is required")
    @Size(min = 2, max = 100, message = "Speciality must be between 2 and 100 characters")
    private String speciality;

    @NotBlank(message = "Language is required")
    @Size(min = 2, max = 100, message = "Language must be between 2 and 100 characters")
    private String language;

    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 100, message = "Experience cannot exceed 100 years")
    private Integer experience;
}