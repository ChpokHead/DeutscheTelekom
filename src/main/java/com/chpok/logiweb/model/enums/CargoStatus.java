package com.chpok.logiweb.model.enums;

public enum CargoStatus {
    PREPARED, SHIPPED, DELIVERED;

    public static CargoStatus fromInteger(int index) {
        switch(index) {
            case 0:
                return PREPARED;
            case 1:
                return SHIPPED;
            case 2:
                return DELIVERED;
            default:
                return null;
        }
    }
}
