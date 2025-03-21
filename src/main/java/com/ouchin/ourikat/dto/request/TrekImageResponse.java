package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrekImageResponse {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotEmpty(message = "Path cannot be empty")
    private String path;

    @NotNull(message = "isPrimary cannot be null")
    private Boolean isPrimary;
}