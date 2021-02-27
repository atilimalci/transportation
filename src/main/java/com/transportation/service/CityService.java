package com.transportation.service;

import com.transportation.model.entity.City;

import java.util.List;

public interface CityService {
    City save(City city);
    List<City> findAll();
    City findByCode(String code);
}
