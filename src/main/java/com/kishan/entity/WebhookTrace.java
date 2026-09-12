package com.kishan.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Correlation record for inbound webhook events.
 *
 * <p>One row per event received at {@code /webhook} or {@code /webhook/openwa}.
 * Useful for debugging missed replies, retries, duplicate deliveries, and
 * measuring end-to-end latency between inbound event and AI reply storage.
 */
@Entity
@Table(name = "webhook_traces")
public class WebhookTrace extends PanacheEntity {

    /** Channel that delivered the event: "meta", "openwa", "console", … */
    @Column(nullable = false, length = 30)
    public String channel;

    /** Event type as received: "message.received", "verification", "status", … */
    @Column(nullable = false, length = 120)
    public String eventType;

    /** Inbound sender phone (digits only) when available; may be null for system events. */
    @Column(length = 20)
    public String phone;

    /** Human label for the event, e.g. "accepted", "reply-stored", "send-attempted". */
    @Column(nullable = false, length = 120)
    public String status;

    /** How long (ms) the handler took to process the event. */
    @Column(nullable = false)
    public long durationMs;

    /** Optional error description when handling failed. */
    @Column(columnDefinition = "TEXT")
    public String error;

    /** Raw correlation id from {@link com.kishan.observability.CorrelationId}. */
    @Column(length = 40)
    public String traceId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public LocalDateTime receivedAt;

    @Column(length = 255)
    public String rawPayloadRef;

    // ---------- helpers ----------

    public static WebhookTrace create(String channel, String eventType, String phone,
            String status, long durationMs, String error, String traceId) {
        WebhookTrace t = new WebhookTrace();
        t.channel = channel;
        t.eventType = eventType;
        t.phone = phone;
        t.status = status;
        t.durationMs = durationMs;
        t.error = error;
        t.traceId = traceId;
        t.persist();
        return t;
    }
}
