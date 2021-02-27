package com.transportation.controller;

import com.transportation.model.dto.TruckDto;
import com.transportation.model.entity.City;
import com.transportation.model.entity.Truck;
import com.transportation.service.CityService;
import com.transportation.service.TruckService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/api/truck")
public class TruckController {

    private final TruckService truckService;
    private final CityService cityService;


    @Autowired
    public TruckController(TruckService truckService, CityService cityService) {
        this.truckService = truckService;
        this.cityService = cityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TruckDto save(@Valid @RequestBody TruckDto truckDto) {
        City from = cityService.findByCode(truckDto.getFromCity());
        City to = cityService.findByCode(truckDto.getToCity());
        Truck truck = new Truck();
        truck.setDate(truckDto.getDate());
        truck.setFromCity(from);
        truck.setToCity(to);
        truck.setTruckNo(truckDto.getTruckNo());
        return new TruckDto(truckService.save(truck));
    }

    @GetMapping
    public ResponseEntity<List<TruckDto>> findTrucks(@RequestParam(value = "fromCity") String fromCityCode,
                                                     @RequestParam(value = "toCity") String toCityCode) {
        return ok(truckService.findTrucks(fromCityCode, toCityCode));
    }

    @GetMapping("/{truckId}")
    public ResponseEntity<TruckDto> findById(@PathVariable(name="truckId") Long truckId) {
        return ok(new TruckDto(truckService.findById(truckId)));
    }
}
