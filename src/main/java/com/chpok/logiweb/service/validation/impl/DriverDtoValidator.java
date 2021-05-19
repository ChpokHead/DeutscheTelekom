package com.chpok.logiweb.service.validation.impl;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.service.validation.ValidationProvider;
import org.springframework.stereotype.Component;

@Component
public class DriverDtoValidator implements ValidationProvider<DriverDto> {
    private static final short MAX_MONTH_WORKED_HOURS = 176;

    @Override
    public void validate(DriverDto driver) {
        validateCredentialIsNull(driver.getFirstName());
        validateCredentialIsNull(driver.getLastName());

        validateCredentialIsEmpty(driver.getFirstName());
        validateCredentialIsEmpty(driver.getLastName());

        validateCredentialContainsOnlySpacesOrTabs(driver.getFirstName());
        validateCredentialContainsOnlySpacesOrTabs(driver.getLastName());

        validateCredentialIsNotANumber(driver.getFirstName());
        validateCredentialIsNotANumber(driver.getLastName());

        validateMonthWorkedHoursIsNull(driver.getMonthWorkedHours());
        validateMonthWorkedHoursIsPositive(driver.getMonthWorkedHours());
        validateMonthWorkedHoursLessOrEqualMaxMonthWorkedHours(driver.getMonthWorkedHours());
    }

    private void validateCredentialIsNull(String credential) {
        if (credential == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateCredentialIsEmpty(String credential) {
        if (credential.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateCredentialContainsOnlySpacesOrTabs(String credential) {
        if (credential.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateCredentialIsNotANumber(String credential) {
        if (credential.matches("-?\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMonthWorkedHoursIsPositive(Short monthWorkedHours) {
        if (monthWorkedHours < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMonthWorkedHoursIsNull(Short monthWorkedHours) {
        if (monthWorkedHours == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMonthWorkedHoursLessOrEqualMaxMonthWorkedHours(Short monthWorkedHours) {
        if (monthWorkedHours > MAX_MONTH_WORKED_HOURS) {
            throw new IllegalArgumentException();
        }
    }

}
