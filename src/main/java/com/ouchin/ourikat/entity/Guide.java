package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "guides")
@DiscriminatorValue("GUIDE")
public class Guide extends User {

    @Min(value = 1960, message = "Birth year must be after 1900")
    @Max(value = 2010, message = "Birth year must be before 2010")
    private Integer birthday;

    @NotBlank(message = "Language is required")
    @Size(min = 2, max = 100, message = "Language must be between 2 and 100 characters")
    private String language;

    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 100, message = "Experience cannot exceed 100 years")
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

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isValidateGuide = false;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
}