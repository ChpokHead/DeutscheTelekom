package com.chpok.logiweb.service.validation.impl;

import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.service.validation.ValidationProvider;
import org.springframework.stereotype.Component;

@Component
public class CargoDtoValidator implements ValidationProvider<CargoDto> {

    @Override
    public void validate(CargoDto cargo) {
        validateWeightIsNegativeOrZero(cargo);
        validateWeightIsNull(cargo);
    }

    private void validateWeightIsNegativeOrZero(CargoDto cargo) {
        if (cargo.getWeight() <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateWeightIsNull(CargoDto cargo) {
        if (cargo.getWeight() == null) {
            throw new IllegalArgumentException();
        }
    }

}
