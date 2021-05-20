package com.chpok.logiweb.validation.impl;

import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.validation.ValidationProvider;
import org.springframework.stereotype.Component;

@Component
public class TruckDtoValidator implements ValidationProvider<TruckDto> {
    private static final String TRUCK_REG_NUMBER_PATTERN = "[A-Z]{2}[0-9]{5}";
    private static final short TRUCK_MAX_CAPACITY = 30;
    private static final short TRUCK_MAX_DRIVERS_SHIFT = 2;
    private static final short TRUCK_MIN_CAPACITY = 1;
    private static final short TRUCK_MIN_DRIVERS_SHIFT = 1;

    @Override
    public void validate(TruckDto entity) {
        validateRegNumberIsNull(entity.getRegNumber());
        validateRegNumberIsEmpty(entity.getRegNumber());
        validateRegNumberContainsOnlyTabsAndSpaces(entity.getRegNumber());
        validateRegNumberNotMatchingThePattern(entity.getRegNumber());

        validateCapacityOrDriversShiftIsLessThanMinValue(entity.getCapacity(), TRUCK_MIN_CAPACITY);
        validateCapacityOrDriversShiftIsLessThanMinValue(entity.getDriversShift(), TRUCK_MIN_DRIVERS_SHIFT);
        validateCapacityOrDriversShiftIsMoreThanMaxValue(entity.getCapacity(), TRUCK_MAX_CAPACITY);
        validateCapacityOrDriversShiftIsMoreThanMaxValue(entity.getDriversShift(), TRUCK_MAX_DRIVERS_SHIFT);
    }

    private void validateRegNumberIsNull(String regNumber) {
        if (regNumber == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateRegNumberIsEmpty(String regNumber) {
        if (regNumber.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateRegNumberContainsOnlyTabsAndSpaces(String regNumber) {
        if (regNumber.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateRegNumberNotMatchingThePattern(String regNumber) {
        if (!regNumber.matches(TRUCK_REG_NUMBER_PATTERN)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateCapacityOrDriversShiftIsLessThanMinValue(short number, short minValue) {
        if (number < minValue) {
            throw new IllegalArgumentException();
        }
    }

    private void validateCapacityOrDriversShiftIsMoreThanMaxValue(short number, short maxValue) {
        if (number > maxValue) {
            throw new IllegalArgumentException();
        }
    }
}
