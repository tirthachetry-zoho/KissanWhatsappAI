package com.kishan.openwa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

/**
 * Payload delivered by OpenWA to a registered webhook URL.
 * Header fields: event, timestamp, sessionId, idempotencyKey, deliveryId, data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "OpenWAWebhookEvent",
        description = "OpenWA gateway envelope {event, timestamp, sessionId, idempotencyKey, deliveryId, data}. Inbound farmer messages arrive as event=message.received with data={id, from, to, body, type, timestamp, isGroup, contact}.")
public class OpenWAWebhookEvent {

    @Schema(description = "Event name; only 'message.received' from 1:1 chats is processed",
            example = "message.received")
    private String event;

    @Schema(description = "Gateway timestamp", example = "2024-07-01T10:00:00Z")
    private String timestamp;

    @Schema(description = "Gateway session id", example = "kissan-assistant")
    private String sessionId;

    @Schema(description = "Idempotency key for deduplication")
    private String idempotencyKey;

    @Schema(description = "Delivery attempt id")
    private String deliveryId;

    @Schema(description = "Message object: {id, from, to, body, type, timestamp, isGroup, contact{pushName,name}}")
    private Map<String, Object> data;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}