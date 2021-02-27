package com.transportation.model.dto;

import com.transportation.model.entity.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Long id;
    @NotEmpty(message = "Please provide a name")
    public String name;
    @NotEmpty(message = "Please provide a city code")
    private String code;

    public CityDto(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.code = city.getCode();
    }
}
