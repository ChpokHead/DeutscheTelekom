package com.chpok.logiweb.dto;

import java.util.ArrayList;
import java.util.List;

public class WaypointsPair {
    private List<WaypointDto> pair = new ArrayList<>();

    public List<WaypointDto> getPair() {
        return pair;
    }

    public void setPair(List<WaypointDto> pair) {
        this.pair = pair;
    }
}
