package com.kishan.observability;

import com.kishan.entity.WebhookTrace;
import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Operational observability surface.
 *
 * <p>Exposes:
 * <ul>
 *   <li>{@code GET /q/health} and {@code GET /q/health/ready} from SmallRye Health.</li>
 *   <li>{@code GET /observability/recent-traces} — last inbound webhook traces (for ops).</li>
 *   <li>{@code GET /observability/status} — app-level operational summary.</li>
 * </ul>
 */
@Path("/observability")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Observability", description = "Operational health, readiness and recent webhook traces")
@PermitAll
public class ObservabilityResource {

    @Inject
    CorrelationId correlationId;

    @GET
    @Path("/recent-traces")
    @Operation(summary = "Recent webhook traces",
            description = "Last inbound webhook traces for debugging missed replies, retries and latency.")
    @APIResponse(responseCode = "200", description = "Recent traces",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = RecentTracesResponse.class)))
    public Response recentTraces(@QueryParam("limit") @DefaultValue("200") int limit,
                                 @QueryParam("channel") String channel) {
        try {
            List<WebhookTrace> rows = channel != null
                    ? WebhookTrace.find("channel = ?1 order by receivedAt desc", channel).page(0, limit).list()
                    : WebhookTrace.find("order by receivedAt desc").page(0, limit).list();
            List<TraceView> views = new ArrayList<>(rows.size());
            for (WebhookTrace t : rows) {
                views.add(new TraceView(t.channel, t.eventType, t.phone, t.status, t.durationMs,
                        t.error, t.traceId, t.receivedAt));
            }
            return Response.ok(new RecentTracesResponse(views,
                    rows.isEmpty() ? null : rows.get(0).receivedAt)).build();
        } catch (RuntimeException e) {
            Log.warnf(e, "Failed to read recent webhook traces");                return Response.serverError().entity(
                        Map.of("error", "failed to read traces", "message", e.getMessage())).build();
        }
    }

    @GET
    @Path("/status")
    @Operation(summary = "Operational status",
            description = "Lightweight operational summary: uptime-relative now, active trace id, configured channels.")
    @APIResponse(responseCode = "200", description = "Operational status",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = StatusResponse.class)))
    public Response status() {
        return Response.ok(new StatusResponse(
                LocalDateTime.now().toString(),
                correlationId.current(),
                Map.of(
                        "channels", List.of("meta", "openwa"),
                        "ai", aiDescription(),
                        "openwa_configured", openwaConfigured()
                )
        )).build();
    }

    private String aiDescription() {
        return "rule-based";
    }

    private boolean openwaConfigured() {
        return false;
    }

    // ---------- shapes ----------

    public record TraceView(String channel, String eventType, String phone, String status,
                            long durationMs, String error, String traceId,
                            LocalDateTime receivedAt) {}

    public record RecentTracesResponse(List<TraceView> traces, LocalDateTime lastReceived) {}

    public record StatusResponse(String now, String traceId, Map<String, Object> details) {}
}
