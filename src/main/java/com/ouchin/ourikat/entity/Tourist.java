package com.ouchin.ourikat.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("tourists")
public class Tourist extends User {
    private String nationality;

    @ElementCollection
    private List<String> preferences;
}
