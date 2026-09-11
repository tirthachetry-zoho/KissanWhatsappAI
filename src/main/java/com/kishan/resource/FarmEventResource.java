package com.kishan.resource;

import com.kishan.dto.FarmEventCreate;
import com.kishan.dto.FarmEventResponse;
import com.kishan.entity.Farm;
import com.kishan.entity.FarmEvent;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/farm-events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Farm events", description = "Sowing, irrigation, harvest and other dated farm activities")
public class FarmEventResource {

    @GET
    @Operation(summary = "List all farm events")
    @APIResponse(responseCode = "200", description = "List of farm events",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmEventResponse.class)))
    public List<FarmEventResponse> list() {
        return FarmEvent.<FarmEvent>listAll().stream().map(FarmEventResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a farm event by id")
    @APIResponse(responseCode = "200", description = "The farm event",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmEventResponse.class)))
    @APIResponse(responseCode = "404", description = "Farm event not found")
    public Response get(@Parameter(description = "Farm event id", required = true) @PathParam("id") Long id) {
        FarmEvent event = FarmEvent.findById(id);
        if (event == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FarmEventResponse.from(event)).build();
    }

    @GET
    @Path("/by-farm/{farmId}")
    @Operation(summary = "List events for a farm")
    @APIResponse(responseCode = "200", description = "Events recorded for the farm",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmEventResponse.class)))
    public List<FarmEventResponse> byFarm(@Parameter(description = "Farm id", required = true) @PathParam("farmId") Long farmId) {
        return FarmEvent.<FarmEvent>find("farm.id", farmId).list().stream().map(FarmEventResponse::from).toList();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a farm event", description = "The referenced farm must exist.")
    @APIResponse(responseCode = "201", description = "Farm event created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmEventResponse.class)))
    @APIResponse(responseCode = "400", description = "Farm not found")
    public Response create(@Valid FarmEventCreate dto) {
        Farm farm = Farm.findById(dto.getFarmId());
        if (farm == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Farm not found", "farmId", dto.getFarmId())).build();
        }
        FarmEvent event = new FarmEvent();
        event.setFarm(farm);
        event.setEventType(dto.getEventType());
        event.setDate(dto.getDate());
        event.setQuantity(dto.getQuantity());
        event.setCost(dto.getCost());
        event.setNotes(dto.getNotes());
        event.persistAndFlush();
        return Response.status(Response.Status.CREATED).entity(FarmEventResponse.from(event)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a farm event")
    @APIResponse(responseCode = "204", description = "Farm event deleted")
    @APIResponse(responseCode = "404", description = "Farm event not found")
    public Response delete(@Parameter(description = "Farm event id", required = true) @PathParam("id") Long id) {
        if (!FarmEvent.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}