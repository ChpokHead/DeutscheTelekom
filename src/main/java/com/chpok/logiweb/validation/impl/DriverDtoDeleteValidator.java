package com.chpok.logiweb.validation.impl;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.validation.ValidationProvider;
import org.springframework.stereotype.Component;

@Component
public class DriverDtoDeleteValidator implements ValidationProvider<DriverDto> {
    @Override
    public void validate(DriverDto entity) {
        validateCurrentOrderNotNull(entity);
    }

    private void validateCurrentOrderNotNull(DriverDto entity) {
        if (entity.getCurrentOrder() != null) {
            throw new IllegalArgumentException();
        }
    }
}
