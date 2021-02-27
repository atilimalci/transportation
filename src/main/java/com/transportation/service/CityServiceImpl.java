package com.transportation.service;

import com.transportation.exception.CityNotFoundException;
import com.transportation.model.entity.City;
import com.transportation.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
class CityServiceImpl implements CityService {
    private CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City save(City city) {
        log.debug("Saving new city: {}", city);
        return cityRepository.save(city);
    }

    public List<City> findAll() {
        log.debug("Finding all cities");
        return cityRepository.findAll();
    }

    public City findByCode(String code) {
        log.debug("Finding city by code {}", code);
        City city = cityRepository.findByCode(code);
        if (city == null) {
            throw new CityNotFoundException(code);
        }
        return city;
    }

}
