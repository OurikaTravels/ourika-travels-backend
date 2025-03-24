package com.ouchin.ourikat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatisticsResponseDto {
    private long totalUsers;
    private long totalTourists;
    private long totalGuides;
    private long verifiedUsers;
    private long validatedGuides;
    private long nonValidatedGuides;
    private long reservationsCount;
    private long reservationRevenue;

}