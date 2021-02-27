package com.transportation.service;

import com.transportation.model.dto.TruckDto;
import com.transportation.model.entity.Truck;

import java.util.List;

public interface TruckService {
    Truck findById(Long truckId);

    List<TruckDto> findTrucks(String fromCityCode, String toCityCode);

    Truck save(Truck truck);
}
