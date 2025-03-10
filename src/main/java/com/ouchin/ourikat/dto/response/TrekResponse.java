package com.ouchin.ourikat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrekResponse {
    private Long id;
    private String title;
    private String description;
    private Duration duration;
    private String startLocation;
    private String endLocation;
    private String fullDescription;
    private Double price;
    private Long categoryId;
    private Set<HighlightResponse> highlights;
    private Set<ServiceEntityResponse> services;
}
