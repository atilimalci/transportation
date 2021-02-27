package com.transportation.controller;

import com.transportation.model.dto.CityDto;
import com.transportation.model.entity.City;
import com.transportation.service.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/api/city")
public class CityController {
    private CityService cityService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CityController(CityService cityService, ModelMapper modelMapper) {
        this.cityService = cityService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto save(@Valid @RequestBody CityDto cityDto) {
        City city = modelMapper.map(cityDto, City.class);
        city = cityService.save(city);
        return convertToDto(city);

    }

    @GetMapping
    public ResponseEntity<List<CityDto>> getAll() {
        List<City> cities = cityService.findAll();
        return ok(cities.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    private CityDto convertToDto(City city) {
        return modelMapper.map(city, CityDto.class);
    }
}
