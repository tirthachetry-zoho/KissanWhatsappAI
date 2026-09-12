package com.kishan.observability;

import com.kishan.openwa.OpenWAClient;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

/**
 * Readiness/liveness check for the OpenWA gateway dependency.
 *
 * <p>Fails readiness when the configured OpenWA instance is unreachable or the
 * expected session cannot be resolved, so traffic is not routed to an app that
 * cannot send replies.
 */
@ApplicationScoped
@Liveness
public class OpenWADependency implements HealthCheck {

    @Inject
    OpenWAClient openwaClient;

    @Override
    public HealthCheckResponse call() {
        try {
            if (!openwaClient.isConfigured()) {
                return HealthCheckResponse.down("openwa not configured (OPENWA_API_KEY missing)");
            }
            openwaClient.resolveSessionId();
            return HealthCheckResponse.up("openwa reachable; session=" + openwaClient.sessionName());
        } catch (RuntimeException e) {
            Log.warnf(e, "OpenWA readiness check failed");
            return HealthCheckResponse.down("openwa unreachable: " + e.getMessage());
        }
    }
}
