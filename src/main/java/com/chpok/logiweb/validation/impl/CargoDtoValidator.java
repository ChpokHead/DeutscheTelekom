package com.chpok.logiweb.validation.impl;

import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.validation.ValidationProvider;
import org.springframework.stereotype.Component;

@Component
public class CargoDtoValidator implements ValidationProvider<CargoDto> {
    private static final int MAX_WEIGHT = 100000;

    @Override
    public void validate(CargoDto cargo) {
        validateWeightIsNull(cargo.getWeight());
        validateWeightIsNegativeOrZero(cargo.getWeight());
        validateWeightIsLessThanMaxValue(cargo.getWeight());

        validateNameIsNull(cargo.getName());
        validateNameIsEmpty(cargo.getName());
        validateNameContainsOnlySpacesOrBlanks(cargo.getName());
    }

    private void validateWeightIsLessThanMaxValue(Integer weight) {
        if (weight > MAX_WEIGHT) {
            throw new IllegalArgumentException();
        }
    }

    private void validateWeightIsNegativeOrZero(Integer weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateWeightIsNull(Integer weight) {
        if (weight == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNameIsNull(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNameIsEmpty(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNameContainsOnlySpacesOrBlanks(String name) {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

}
