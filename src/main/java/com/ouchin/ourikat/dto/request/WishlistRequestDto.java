package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WishlistRequestDto {

    @NotNull(message = "Trek ID cannot be null")
    @Positive(message = "Trek ID must be a positive number")
    private Long trekId;
}