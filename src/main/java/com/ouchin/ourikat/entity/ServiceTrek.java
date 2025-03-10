package com.ouchin.ourikat.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_treks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTrek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "trek_id")
    private Long trekId;
}
