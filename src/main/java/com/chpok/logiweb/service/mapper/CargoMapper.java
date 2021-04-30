package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.enums.CargoStatus;
import org.springframework.stereotype.Component;

@Component
public class CargoMapper {
    public CargoDto mapCargoToCargoDto(Cargo cargo) {
        final CargoDto cargoDto = new CargoDto();

        cargoDto.setId(cargo.getId());
        cargoDto.setName(cargo.getName());
        cargoDto.setWeight(cargo.getWeight());
        cargoDto.setStatus(cargo.getStatus().ordinal());

        return cargoDto;
    }

    public Cargo mapCargoDtoToCargo(CargoDto cargoDto) {
        return Cargo.builder()
                .withId(cargoDto.getId())
                .withName(cargoDto.getName())
                .withWeight(cargoDto.getWeight())
                .withStatus(CargoStatus.fromInteger(cargoDto.getStatus()))
                .build();
    }
}
