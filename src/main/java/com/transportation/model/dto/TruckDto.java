package com.transportation.model.dto;

import com.transportation.model.entity.Truck;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TruckDto {
    private Long id;
    @NotEmpty(message = "Please provide a 'from' city code")
    private String fromCity;
    @NotEmpty(message = "Please provide a 'to' city code")
    private String toCity;
    @NotEmpty(message = "Please provide truck no")
    private String truckNo;
    @NotNull(message = "Please provide truck date")
    private LocalDate date;
    public TruckDto(Truck truck) {
        this.fromCity = truck.getFromCity().getCode();
        this.toCity = truck.getToCity().getCode();
        this.truckNo = truck.getTruckNo();
        this.date = truck.getDate();
        this.id = truck.getId();
    }
}
