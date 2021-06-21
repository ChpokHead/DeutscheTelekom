package com.chpok.logiweb.model.kafka;

public class LogiwebMessage {
    private String entityEventMessage;
    private Long entityId;

    public LogiwebMessage() {
    }

    public LogiwebMessage(String entityEventMessage, Long entityId) {
        this.entityEventMessage = entityEventMessage;
        this.entityId = entityId;
    }

    public String getEntityEventMessage() {
        return entityEventMessage;
    }

    public void setEntityEventMessage(String entityEventMessage) {
        this.entityEventMessage = entityEventMessage;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
