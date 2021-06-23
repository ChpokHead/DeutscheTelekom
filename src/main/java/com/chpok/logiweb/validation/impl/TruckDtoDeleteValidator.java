package com.chpok.logiweb.validation.impl;

import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.validation.ValidationProvider;
import org.springframework.stereotype.Component;

@Component
public class TruckDtoDeleteValidator implements ValidationProvider<TruckDto> {
    @Override
    public void validate(TruckDto entity) {
        validateCurrentOrderNotNull(entity);
    }

    private void validateCurrentOrderNotNull(TruckDto entity) {
        if (entity.getCurrentOrder() != null) {
            throw new IllegalArgumentException();
        }
    }
}
