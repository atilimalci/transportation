package com.transportation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class City extends AbstractEntity {

    @Column(length = 100, nullable = false)
    public String name;

    @Column(length = 10, unique = true, nullable = false)
    private String code;
}