package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_entities")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "treks")
@ToString(exclude = "treks")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "services", fetch = FetchType.LAZY)
    private Set<Trek> treks = new HashSet<>();
}
