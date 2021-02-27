package com.transportation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Truck extends AbstractEntity {

    @ManyToOne(optional = false)
    public City fromCity;

    @ManyToOne(optional = false)
    private City toCity;

    @Column(unique = true, nullable = false)
    private String truckNo;

    @Column(nullable = false)
    private LocalDate date;

}