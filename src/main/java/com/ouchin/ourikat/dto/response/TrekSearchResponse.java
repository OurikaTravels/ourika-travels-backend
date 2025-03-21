package com.ouchin.ourikat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrekSearchResponse {
    private Long id;
    private String title;
    private String description;
    private String primaryImageUrl;
}
