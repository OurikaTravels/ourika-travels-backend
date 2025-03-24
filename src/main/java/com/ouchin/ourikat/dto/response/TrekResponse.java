package com.ouchin.ourikat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrekResponse {
    private Long id;
    private String title;
    private String description;
    private String formattedDuration;
    private String startLocation;
    private String endLocation;
    private String fullDescription;
    private Double price;
    private Long categoryId;
    private String categoryName;
    private Set<HighlightResponse> highlights;
    private Set<ServiceEntityResponse> services;
    private List<ActivityResponse> activities = new ArrayList<>();
    private List<TrekImageResponse> images = new ArrayList<>();

}
