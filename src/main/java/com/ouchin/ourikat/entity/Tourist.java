package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tourists")
@DiscriminatorValue("TOURIST")
public class Tourist extends User {
    private String nationality;

    @ElementCollection
    @CollectionTable(name = "tourist_preferences", joinColumns = @JoinColumn(name = "tourist_id"))
    @Column(name = "preference")
    private List<String> preferences;
}