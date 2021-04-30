package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.CargoDto;

import java.util.List;

public interface CargoService {
    List<CargoDto> getAllCargos();
    void saveCargo(CargoDto cargo);
    CargoDto getCargoById(Long id);
    void deleteCargo(Long id);
    void updateCargo(CargoDto cargo);
}
