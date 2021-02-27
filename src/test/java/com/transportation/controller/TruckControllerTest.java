package com.transportation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transportation.model.dto.TruckDto;
import com.transportation.model.entity.City;
import com.transportation.model.entity.Truck;
import com.transportation.repository.CityRepository;
import com.transportation.repository.TruckRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TruckControllerTest {
    private static final String API_TRUCK = "/api/truck";

    @Mock
    private TruckController truckController;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        MockMvcBuilders.standaloneSetup(truckController).build();
    }

    @After
    public void after() {
        truckRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    public void saveCity_TruckMissingFromCityField_Fails() {
        //Arrange
        prepareCityData();
        TruckDto truckDto = prepareTruckDto();
        truckDto.setFromCity(null);

        //Act
        ResponseEntity<String> result = rest.postForEntity(API_TRUCK,
                truckDto, String.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void saveCity_TruckMissingToCityField_Fails() {
        //Arrange
        prepareCityData();
        TruckDto truckDto = prepareTruckDto();
        truckDto.setToCity(null);

        //Act
        ResponseEntity<String> result = rest.postForEntity(API_TRUCK,
                truckDto, String.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void saveCity_TruckMissingTruckNoField_Fails() {
        //Arrange
        prepareCityData();
        TruckDto truckDto = prepareTruckDto();
        truckDto.setTruckNo(null);

        //Act
        ResponseEntity<String> result = rest.postForEntity(API_TRUCK,
                truckDto, String.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void saveCity_TruckMissingTruckDateField_Fails() {
        //Arrange
        prepareCityData();
        TruckDto truckDto = prepareTruckDto();
        truckDto.setDate(null);

        //Act
        ResponseEntity<String> result = rest.postForEntity(API_TRUCK,
                truckDto, String.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void saveTruck_savesTheTruck() {
        //Arrange
        prepareCityData();
        TruckDto truckDto = prepareTruckDto();

        //Act
        ResponseEntity<TruckDto> result = rest.postForEntity(API_TRUCK,
                truckDto, TruckDto.class);

        //Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        TruckDto returnedTruck = result.getBody();
        assertEquals(LocalDate.now(), returnedTruck.getDate());
        assertEquals("06", returnedTruck.getFromCity());
        assertEquals("34", returnedTruck.getToCity());
        assertEquals("1", returnedTruck.getTruckNo());
        Long id = result.getBody().getId();
        Optional<Truck> savedTruckOptional = truckRepository.findById(id);
        assertTrue(savedTruckOptional.isPresent());
        Truck savedTruck = savedTruckOptional.get();
        assertEquals("1", savedTruck.getTruckNo());
        assertEquals("06", savedTruck.getFromCity().getCode());
        assertEquals("34", savedTruck.getToCity().getCode());
        assertEquals(LocalDate.now(), savedTruck.getDate());
        assertNotNull(savedTruck.getCreateDate());
        assertNotNull(savedTruck.getLastModifiedDate());
    }

    @Test
    public void findTrucks_NoTrucksExist_GetsEmptyList() {
        //Arrange
        URI uri = getRequestUri();

        //Act
        ResponseEntity<String> result = rest.getForEntity(uri, String.class);

        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("[]", result.getBody());
    }

    @Test
    public void findTrucksTruckExist_GetsList() throws IOException {
        //Arrange
        URI uri = getRequestUri();
        insertTruckData();

        //Act
        ResponseEntity<String> result = rest.getForEntity(uri, String.class );

        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        TruckDto[] trucks = objectMapper.readValue(result.getBody(), TruckDto[].class);
        assertNotNull(trucks);
        assertEquals(1, trucks.length);
        assertEquals("06", trucks[0].getFromCity());
        assertEquals("34", trucks[0].getToCity());
        assertEquals(LocalDate.now(), trucks[0].getDate());
    }

    @Test
    public void findById_NoResult_GetsNotFoundResponse() {
        //Act
        ResponseEntity<String> result = rest.getForEntity(API_TRUCK + "/1", String.class);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void findById_ResultExist_GetsResult() {
        //Arrange
        Long id = insertTruckData();

        //Act
        ResponseEntity<TruckDto> result = rest.getForEntity(API_TRUCK + "/" + id, TruckDto.class);

        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        TruckDto truck = result.getBody();
        assertEquals("1", truck.getTruckNo());
        assertEquals("06", truck.getFromCity());
        assertEquals("34", truck.getToCity());
        assertEquals(LocalDate.now(), truck.getDate());
    }

    private Long insertTruckData() {
        City ankara = new City("Ankara", "06");
        City istanbul = new City("Istanbul", "34");
        cityRepository.save(istanbul);
        cityRepository.save(ankara);
        Truck truck = new Truck();
        truck.setToCity(istanbul);
        truck.setFromCity(ankara);
        truck.setTruckNo("1");
        truck.setDate(LocalDate.now());
        return truckRepository.save(truck).getId();
    }

    public URI getRequestUri() {
        String url = "http://localhost:" + this.port;
        return UriComponentsBuilder.fromHttpUrl(url).path(API_TRUCK)
                .queryParam("fromCity", "Ankara")
                .queryParam("toCity", "Istanbul")
                .build().toUri();
    }

    private void prepareCityData() {
        City istanbul = new City("Ankara", "06");
        City ankara = new City("Istanbul", "34");
        cityRepository.save(istanbul);
        cityRepository.save(ankara);
    }

    private TruckDto prepareTruckDto() {
        TruckDto truckDto = new TruckDto();
        truckDto.setFromCity("06");
        truckDto.setToCity("34");
        truckDto.setDate(LocalDate.now());
        truckDto.setTruckNo("1");
        return truckDto;
    }
}
