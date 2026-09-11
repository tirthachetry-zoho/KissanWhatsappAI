package com.kishan.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.OffsetDateTime;
import java.util.Map;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Health", description = "Liveness probe for load balancers and PaaS health checks")
public class HealthResource {

    @Inject
    @ConfigProperty(name = "quarkus.application.name", defaultValue = "kissan-ai")
    String serviceName;

    @GET
    @Operation(summary = "Service health", description = "Returns status ok with the service name and current timestamp.")
    @APIResponse(responseCode = "200", description = "Service is healthy")
    public Map<String, Object> health() {
        return Map.of(
                "status", "ok",
                "service", serviceName,
                "timestamp", OffsetDateTime.now().toString()
        );
    }
}