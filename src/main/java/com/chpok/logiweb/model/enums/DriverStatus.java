package com.chpok.logiweb.model.enums;

public enum DriverStatus {
    RESTING, SHIFTING, DRIVING;

    public static DriverStatus fromInteger(int index) {
        switch(index) {
            case 0:
                return RESTING;
            case 1:
                return SHIFTING;
            case 2:
                return DRIVING;
        }
        return null;
    }
}
