package com.ouchin.ourikat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrekImageResponse {
    private Long id;
    private String path;
    private Boolean isPrimary;
}