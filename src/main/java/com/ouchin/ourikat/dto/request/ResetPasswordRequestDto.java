package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    @NotBlank(message = "New password is required")
    private String newPassword;
}
