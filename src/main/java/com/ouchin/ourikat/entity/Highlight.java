package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "highlight")
@EqualsAndHashCode(exclude = "treks")
@ToString(exclude = "treks")
public class Highlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Content is required")
    @Size(min = 5, max = 500, message = "Content must be between 5 and 500 characters")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany(mappedBy = "highlights", fetch = FetchType.LAZY)
    private Set<Trek> treks = new HashSet<>();
}