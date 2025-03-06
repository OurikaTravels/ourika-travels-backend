package com.ouchin.ourikat.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;

@Entity
@Table(name = "treks")
@Data
public class Trek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Duration duration;
    private String startLocation;
    private String endLocation;
    private String fullDescription;
    private Double price;
}
