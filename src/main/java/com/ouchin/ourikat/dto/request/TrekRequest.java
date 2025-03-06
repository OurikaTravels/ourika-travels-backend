package com.ouchin.ourikat.dto.request;

import lombok.Data;

import java.time.Duration;

@Data
public class TrekRequest {
    private String title;
    private String description;
    private Duration duration;
    private String startLocation;
    private String endLocation;
    private String fullDescription;
    private Double price;
}
