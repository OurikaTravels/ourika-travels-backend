package com.ouchin.ourikat.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("GUIDE")
public class Guide extends User {
    private Integer birthday;
    private String language;
    private Integer experience;
    private String phone;
    private String speciality;
    private String licenseNumber;
}