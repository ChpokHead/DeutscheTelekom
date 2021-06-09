package com.chpok.logiweb.controller;

import com.chpok.logiweb.service.LocationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(produces = "application/json")
    public String getAvailableLocations() {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(locationService.getAllLocations());
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }
}
