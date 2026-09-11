package com.kishan.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.OffsetDateTime;
import java.util.Map;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @Inject
    @ConfigProperty(name = "quarkus.application.name", defaultValue = "kissan-ai")
    String serviceName;

    @GET
    public Map<String, Object> health() {
        return Map.of(
                "status", "ok",
                "service", serviceName,
                "timestamp", OffsetDateTime.now().toString()
        );
    }
}