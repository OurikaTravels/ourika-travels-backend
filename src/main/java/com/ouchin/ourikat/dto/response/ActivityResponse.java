package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.enums.ActivityType;
import com.ouchin.ourikat.enums.TransportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private Long id;
    private String title;
    private ActivityType type;
    private String description;
    private Boolean isOptional;
    private TransportType transportType;
    private Duration transportDuration;
    private Integer activityOrder;
    private Long trekId;
}
