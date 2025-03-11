package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDto {
    private String title;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    private Boolean isActive;
}