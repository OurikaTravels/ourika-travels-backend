package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.entity.Trek;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WishlistResponseDto {
    private Long id;
    private Long touristId;
    private TrekResponse trek;
    private LocalDateTime addedDate;
}