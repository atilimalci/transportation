package com.transportation.service;

import com.transportation.exception.TruckNotFoundException;
import com.transportation.model.dto.TruckDto;
import com.transportation.model.entity.City;
import com.transportation.model.entity.Truck;
import com.transportation.repository.TruckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
class TruckServiceImpl implements TruckService {
    private final TruckRepository truckRepository;

    @Autowired
    public TruckServiceImpl(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    @Override
    public Truck findById(Long truckId) {
        log.debug("Getting truck by id {}", truckId);
        Optional<Truck> truckOptional = truckRepository.findById(truckId);
        if(! truckOptional.isPresent()) {
            throw new TruckNotFoundException("Truck not found with id:" + truckId);
        }
        return truckOptional.get();
    }

    @Override
    public List<TruckDto> findTrucks(String fromCityCode, String toCityCode) {
        log.debug("Getting trucks from {} to {} ", fromCityCode, toCityCode);
        return truckRepository.findTrucks(fromCityCode, toCityCode);
    }

    @Override
    public Truck save(Truck truck) {
        log.debug("Saving new truck: {}", truck);
        return truckRepository.save(truck);
    }
}
