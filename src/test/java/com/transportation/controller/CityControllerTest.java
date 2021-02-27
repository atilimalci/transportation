package com.transportation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transportation.model.dto.CityDto;
import com.transportation.model.entity.City;
import com.transportation.repository.CityRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerTest {

    private static final String API_CITY = "/api/city/";

    @Mock
    CityController cityController;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private CityRepository cityRepository;

    private static final City CITY_OF_ISTANBUL = new City("Istanbul", "34");

    @Before
    public void setup() {
        MockMvcBuilders.standaloneSetup(cityController).build();
    }

    @After
    public void after() {
        cityRepository.deleteAll();
    }

    @Test
    public void getAll_NoCitiesExist_GetsEmptyResponse() {
        ResponseEntity<String> result = rest.getForEntity(API_CITY, String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("[]", result.getBody());
    }

    @Test
    public void getAll_CitiesExist_GetsCities() {
        //Arrange
        prepareCityData();

        //Act
        ResponseEntity<CityDto[]> result = rest.getForEntity(API_CITY, CityDto[].class);

        //Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        CityDto[] cities = result.getBody();
        assertNotNull(cities);
        assertEquals(1, cities.length);
        assertEquals("Istanbul", cities[0].getName());
        assertEquals("34", cities[0].getCode());
    }

    @Test
    public void save_SavesTheCity() throws JsonProcessingException {

        //Act
        ResponseEntity<CityDto> result = rest.postForEntity(API_CITY,
                CITY_OF_ISTANBUL, CityDto.class);

        //Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Long id = result.getBody().getId();
        Optional<City> savedCityOptional = cityRepository.findById(id);
        assertTrue(savedCityOptional.isPresent());
        City savedCity = savedCityOptional.get();
        assertEquals("Istanbul", savedCity.getName());
        assertEquals("34", savedCity.getCode());
        assertNotNull(savedCity.getCreateDate());
        assertNotNull(savedCity.getLastModifiedDate());
    }

    @Test
    public void save_CityWithNoName_Fails() {
        City istanbul = new City(null, "34");

        //Act
        ResponseEntity<String> result = rest.postForEntity(API_CITY,
                istanbul, String.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertThat(result.getBody(), containsString("Please provide a name"));
    }

    @Test
    public void save_CityWithNoCode_Fails() {
        CityDto istanbul = new CityDto(null,"istanbul",null);

        //Act
        ResponseEntity<String> result = rest.postForEntity(API_CITY,
                istanbul, String.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertThat(result.getBody(), containsString("Please provide a city code"));
    }

    private void prepareCityData() {
        cityRepository.save(CITY_OF_ISTANBUL);
    }


}
