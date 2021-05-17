package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.model.enums.CargoStatus;

import java.util.List;

public interface CargoService {
    List<CargoDto> getAllCargos();
    void saveCargo(CargoDto cargo);
    CargoDto getCargoById(Long id);
    void deleteCargo(Long id);
    void updateCargo(CargoDto cargo);
    List<CargoDto> getUnoccupiedCargos();
    void updateCargoStatus(Long cargoId, CargoStatus newCargoStatus);
}
