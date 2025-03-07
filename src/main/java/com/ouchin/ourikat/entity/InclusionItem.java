package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "inclusion_items")
@Data
public class InclusionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Content is required")
    @Size(min = 5, max = 500, message = "Content must be between 5 and 500 characters")
    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "inclusionItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrekInclusion> trekInclusions = new HashSet<>();
}