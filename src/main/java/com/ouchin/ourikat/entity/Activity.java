package com.ouchin.ourikat.entity;

import com.ouchin.ourikat.enums.ActivityType;
import com.ouchin.ourikat.enums.TransportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Duration;

@Entity
@Table(name = "activities")
@Data
@EqualsAndHashCode(exclude = "trek")
@ToString(exclude = "trek")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Activity type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ActivityType type;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Optional status is required")
    @Column(name = "is_optional")
    private Boolean isOptional = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "trpt_type")
    private TransportType transportType;

    @Column(name = "transport_duration")
    private Duration transportDuration;

    @NotNull(message = "Activity order is required")
    @Column(name = "activity_order")
    private Integer activityOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trek_id", nullable = false)
    private Trek trek;
}
