package com.kishan.observability;

import com.kishan.entity.WebhookTrace;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

/**
 * Times inbound webhook processing and persists a {@link WebhookTrace} row.
 *
 * <p>Use it around webhook handlers like:
 * <pre>
 *   try (var span = tracer.start("meta", "message.received", phone)) {
 *       … handle …
 *       span.mark("accepted");
 *   } catch (RuntimeException e) {
 *       span.markFailed("handle-failed", e);
 *   }
 * </pre>
 */
@ApplicationScoped
public class WebhookTracer {

    @Inject
    CorrelationId correlationId;

    /** Start timing a webhook event. Call {@link #mark(String)} before the try-with closes. */
    public Span start(String channel, String eventType, String phone) {
        return new Span(channel, eventType, phone, correlationId.current());
    }

    /** Timing span for one webhook event. Auto-persists on close. */
    public static class Span implements AutoCloseable {

        private final String channel;
        private final String eventType;
        private final String phone;
        private final String traceId;
        private final long startNanos;
        private String status = "started";
        private String error;
        private boolean closed;

        Span(String channel, String eventType, String phone, String traceId) {
            this.channel = channel;
            this.eventType = eventType;
            this.phone = phone;
            this.traceId = traceId;
            this.startNanos = System.nanoTime();
        }

        /** Mark the outcome; call before the try-with-resources block ends. */
        public void mark(String status) {
            this.status = status;
        }

        /** Record a failure; closes with an error status. */
        public void markFailed(String status, Throwable t) {
            this.status = status;
            this.error = t.getMessage();
            Log.warnf(t, "Webhook [%s/%s] failed: %s", channel, eventType, t.getMessage());
        }

        @Override
        public void close() {
            if (closed) {
                return;
            }
            closed = true;
            long ms = (System.nanoTime() - startNanos) / 1_000_000;
            try {
                WebhookTrace.create(channel, eventType, phone, status, ms, error, traceId);
            } catch (Throwable t) {
                Log.warnf(t, "Failed to persist webhook trace for %s/%s", channel, eventType);
            }
            if (Log.isDebugEnabled()) {
                Log.debugf("[%s/%s] %s in %dms phone=%s traceId=%s",
                        channel, eventType, status, ms, phone, traceId);
            }
        }
    }
}
