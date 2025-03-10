package com.ouchin.ourikat.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "treks")
@Data
public class Trek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(unique = true)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotNull(message = "Duration is required")
    private Duration duration;

    @NotBlank(message = "Start location is required")
    private String startLocation;

    @NotBlank(message = "End location is required")
    private String endLocation;

    @NotBlank(message = "Full description is required")
    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "trek_highlights",
            joinColumns = @JoinColumn(name = "trek_id"),
            inverseJoinColumns = @JoinColumn(name = "highlight_id")
    )
    private Set<Highlight> highlights = new HashSet<>();

    // Added Services relationship
    @ManyToMany(mappedBy = "treks", fetch = FetchType.LAZY)
    private Set<Service> services = new HashSet<>();


    public void addService(Service service) {
        this.services.add(service);
        service.getTreks().add(this);
    }

    public void removeService(Service service) {
        this.services.remove(service);
        service.getTreks().remove(this);
    }

    public void addHighlight(Highlight highlight) {
        this.highlights.add(highlight);
    }

    public void removeHighlight(Highlight highlight) {
        this.highlights.remove(highlight);
    }

}