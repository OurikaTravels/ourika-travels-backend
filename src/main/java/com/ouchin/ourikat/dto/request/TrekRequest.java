package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.util.Set;

@Data
public class TrekRequest {
    private String title;
    private String description;
    private Duration duration;
    private String startLocation;
    private String endLocation;
    private String fullDescription;
    private Double price;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Set<Long> highlightIds;

}
