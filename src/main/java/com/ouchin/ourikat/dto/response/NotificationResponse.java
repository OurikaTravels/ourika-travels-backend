package com.ouchin.ourikat.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String message;
    private Long guideId;
    private Long reservationId;
    private LocalDateTime reservationDate;
}
