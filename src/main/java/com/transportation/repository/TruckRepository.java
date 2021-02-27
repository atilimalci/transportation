package com.transportation.repository;

import com.transportation.model.dto.TruckDto;
import com.transportation.model.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {
    @Query("SELECT new com.transportation.model.dto.TruckDto(t) " +
            "FROM Truck t " +
            "WHERE t.fromCity.name = :fromCity and t.toCity.name = :toCity")
    List<TruckDto> findTrucks(@Param("fromCity") String from, @Param("toCity") String to);
}

