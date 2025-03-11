package com.ouchin.ourikat.dto.response;

import com.ouchin.ourikat.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String description;
    private List<String> images;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private PostStatus status;
}