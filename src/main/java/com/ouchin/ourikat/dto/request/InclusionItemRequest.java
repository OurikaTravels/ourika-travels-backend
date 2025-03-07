package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InclusionItemRequest {
    @NotBlank(message = "Content is required")
    @Size(min = 5, max = 500, message = "Content must be between 5 and 500 characters")
    private String content;
}