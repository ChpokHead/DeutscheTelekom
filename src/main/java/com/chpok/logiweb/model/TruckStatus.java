package com.chpok.logiweb.model;

public enum TruckStatus {
    OK,
    BROKEN;

    public static TruckStatus fromInteger(int index) {
        switch(index) {
            case 0:
                return OK;
            case 1:
                return BROKEN;
        }
        return null;
    }
}
