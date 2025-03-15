package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private Role role;
    private String lastName;
    private String email;
}