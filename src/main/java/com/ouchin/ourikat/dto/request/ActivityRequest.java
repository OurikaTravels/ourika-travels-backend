package com.ouchin.ourikat.dto.request;


import com.ouchin.ourikat.enums.ActivityType;
import com.ouchin.ourikat.enums.TransportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Activity type is required")
    private ActivityType type;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    private Boolean isOptional = false;

    private TransportType transportType;

    private Duration transportDuration;

    @NotNull(message = "Activity order is required")
    private Integer activityOrder;
}