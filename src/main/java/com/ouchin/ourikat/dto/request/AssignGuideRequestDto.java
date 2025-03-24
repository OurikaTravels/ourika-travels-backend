package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AssignGuideRequestDto {

    @NotNull(message = "Guide ID cannot be null")
    @Positive(message = "Guide ID must be a positive number")
    private Long guideId;
}