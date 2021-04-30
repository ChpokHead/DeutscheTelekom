package com.chpok.logiweb.model.enums;

public enum WaypointType {
    LOADING, SHIPPING;

    public static WaypointType fromInteger(int index) {
        switch (index) {
            case 0:
                return LOADING;
            case 1:
                return SHIPPING;
            default:
                return null;
        }
    }
}
