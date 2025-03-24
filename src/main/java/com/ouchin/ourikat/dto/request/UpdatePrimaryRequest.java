package com.ouchin.ourikat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePrimaryRequest {

    @NotNull(message = "isPrimary cannot be null")
    private boolean isPrimary;
}