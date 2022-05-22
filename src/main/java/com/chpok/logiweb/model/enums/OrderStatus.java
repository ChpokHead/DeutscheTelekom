package com.chpok.logiweb.model.enums;

public enum OrderStatus {
    COMPLETED,
    CLOSED,
    IN_PROGRESS;

    public static OrderStatus fromInteger(int index) {
        switch(index) {
        case 0:
            return COMPLETED;
        case 1:
            return CLOSED;
        case 2:
            return IN_PROGRESS;
        default:
            return null;
        }
    }
}
