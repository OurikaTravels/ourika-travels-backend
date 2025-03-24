package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrekImageRequest {
    @NotNull(message = "Primary status is required")
    private Boolean isPrimary;
}