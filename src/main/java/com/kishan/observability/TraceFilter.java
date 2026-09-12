package com.kishan.observability;

import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;


import java.io.IOException;

/**
 * Wires {@link CorrelationId} into the request lifecycle and attaches it to
 * outbound responses so clients (and downstream services) can echo it back.
 */
@Provider    @Priority(100)
public class TraceFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    CorrelationId correlationId;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String incoming = requestContext.getHeaderString(correlationId.headerName);
        correlationId.init(incoming);
        if (Log.isTraceEnabled()) {
            Log.tracef("traceId=%s method=%s path=%s", correlationId.current(),
                    requestContext.getMethod(), requestContext.getUriInfo().getPath());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        responseContext.getHeaders().put(correlationId.headerName,
                java.util.Collections.singletonList(correlationId.current()));
        try {
            correlationId.close();
        } catch (Throwable t) {
            Log.warnf(t, "Error clearing trace context");
        }
    }
}
