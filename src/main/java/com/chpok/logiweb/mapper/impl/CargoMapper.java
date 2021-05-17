package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.enums.CargoStatus;
import org.springframework.stereotype.Component;

@Component
public class CargoMapper implements Mapper<Cargo, CargoDto> {
    @Override
    public CargoDto mapEntityToDto(Cargo cargo) {
        if (cargo == null) {
            return null;
        }

        final CargoDto cargoDto = new CargoDto();

        cargoDto.setId(cargo.getId());
        cargoDto.setName(cargo.getName());
        cargoDto.setWeight(cargo.getWeight());
        cargoDto.setStatus(cargo.getStatus().ordinal());

        return cargoDto;
    }

    @Override
    public Cargo mapDtoToEntity(CargoDto cargoDto) {
        if (cargoDto == null) {
            return null;
        }

        return Cargo.builder()
                .withId(cargoDto.getId())
                .withName(cargoDto.getName())
                .withWeight(cargoDto.getWeight())
                .withStatus(CargoStatus.fromInteger(cargoDto.getStatus()))
                .build();
    }

}
