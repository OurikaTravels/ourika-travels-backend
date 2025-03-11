package com.ouchin.ourikat.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "treks")
@Data
@NoArgsConstructor
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "trek_services",
            joinColumns = @JoinColumn(name = "trek_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceEntity> services = new HashSet<>();


    @OneToMany(mappedBy = "trek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "trek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrekImage> images = new ArrayList<>();

    public void addHighlight(Highlight highlight) {
        this.highlights.add(highlight);
    }

    public void removeHighlight(Highlight highlight) {
        this.highlights.remove(highlight);
    }

    public void addService(ServiceEntity service) {
        this.services.add(service);
        service.getTreks().add(this);
    }

    public void removeService(ServiceEntity service) {
        this.services.remove(service);
        service.getTreks().remove(this);
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
        activity.setTrek(this);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
        activity.setTrek(null);
    }



    public void addImage(TrekImage image) {
        images.add(image);
        image.setTrek(this);
    }

    public void removeImage(TrekImage image) {
        images.remove(image);
        image.setTrek(null);
    }
}