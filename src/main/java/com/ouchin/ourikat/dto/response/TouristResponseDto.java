package com.ouchin.ourikat.dto.response;


import com.ouchin.ourikat.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class TouristResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String nationality;
    private Boolean verified;
}
