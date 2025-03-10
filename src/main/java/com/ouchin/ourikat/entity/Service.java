package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service name is required")
    @Size(min = 2, max = 100, message = "Service name must be between 2 and 100 characters")
    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "service_treks",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "trek_id")
    )
    private Set<Trek> treks = new HashSet<>();

    public void addTrek(Trek trek) {
        this.treks.add(trek);
    }

    public void removeTrek(Trek trek) {
        this.treks.remove(trek);
    }
}