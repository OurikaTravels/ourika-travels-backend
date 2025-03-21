package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tourists")
public class Tourist extends User {

    @Column
    private String nationality;
}