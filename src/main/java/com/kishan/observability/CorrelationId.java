package com.kishan.observability;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Attaches a stable trace id to each HTTP request.
 *
 * <p>Other components can inject {@link CorrelationId} to tag outbound calls and
 * {@link #current()} to read the id. The id is also bound to MDC so that every
 * log line emitted during the request includes {@code traceId}.
 */
@RequestScoped
public class CorrelationId {

    private static final String HEADER = "X-Trace-Id";
    private static final String MDC_KEY = "traceId";

    @ConfigProperty(name = "observability.trace-header", defaultValue = HEADER)
    String headerName;

    private String current;

    /**
     * Current trace id for this request, generated if the caller didn't supply one.
     */
    public String current() {
        if (current == null) {
            current = UUID.randomUUID().toString();
        }
        return current;
    }

    /**
     * Container request filter: materializes the id from the inbound header (if any)
     * otherwise generates one, and binds it to MDC for the lifetime of the request.
     */
    void init(String incoming) {
        if (incoming != null && !incoming.isBlank()) {
            current = incoming;
        } else {
            current = UUID.randomUUID().toString();
        }
        MDC.put(MDC_KEY, current);
    }

    void close() {
        MDC.remove(MDC_KEY);
    }
}
