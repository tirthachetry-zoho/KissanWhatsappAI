package com.kishan.openwa;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * MicroProfile REST client for the OpenWA API (base path /api, auth via X-API-Key).
 * Base URL is configured through quarkus.rest-client.openwa.url.
 */
@RegisterRestClient(configKey = "openwa")
@RegisterClientHeaders(OpenWAHeadersFactory.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface OpenWARestClient {

    @GET
    @Path("/api/sessions")
    List<OpenWASession> listSessions();

    @POST
    @Path("/api/sessions")
    OpenWASession createSession(OpenWACreateSessionRequest request);

    @GET
    @Path("/api/sessions/{id}")
    OpenWASession getSession(@PathParam("id") String id);

    @POST
    @Path("/api/sessions/{id}/start")
    OpenWASession startSession(@PathParam("id") String id);

    @POST
    @Path("/api/sessions/{id}/messages/send-text")
    OpenWASendTextResponse sendText(@PathParam("id") String id, OpenWASendTextRequest request);

    @GET
    @Path("/api/sessions/{id}/webhooks")
    List<OpenWAWebhook> listWebhooks(@PathParam("id") String id);

    @POST
    @Path("/api/sessions/{id}/webhooks")
    OpenWAWebhook createWebhook(@PathParam("id") String id, OpenWAWebhookCreateRequest request);
}