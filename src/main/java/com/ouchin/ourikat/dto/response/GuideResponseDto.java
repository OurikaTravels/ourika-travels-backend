package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Boolean isValidateGuide;
    private String aboutYou;
    private String profileImage;
    //private List<PostResponseDto> posts;
}