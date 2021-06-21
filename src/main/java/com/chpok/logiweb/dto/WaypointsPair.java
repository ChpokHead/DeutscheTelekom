package com.chpok.logiweb.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaypointsPair {
    private List<WaypointDto> pair = new ArrayList<>();

    public List<WaypointDto> getPair() {
        return pair;
    }

    public void setPair(List<WaypointDto> pair) {
        this.pair = pair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaypointsPair that = (WaypointsPair) o;
        return Objects.equals(pair, that.pair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair);
    }
}
