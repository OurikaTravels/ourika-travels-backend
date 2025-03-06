package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.enums.Role;
import lombok.Data;

@Data
public class GuideResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Integer birthday;
    private String language;
    private Integer experience;
    private String phone;
    private String speciality;
    private String licenseNumber;
}