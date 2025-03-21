package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trek_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrekImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Image path is required")
    private String path;

    @NotNull(message = "Primary status is required")
    private Boolean isPrimary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trek_id", nullable = false)
    private Trek trek;

    public boolean isPrimary() {
        return isPrimary != null && isPrimary;
    }
}