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

import java.util.List;
import java.util.Map;

@Path("/api/farm-events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FarmEventResource {

    @GET
    public List<FarmEventResponse> list() {
        return FarmEvent.<FarmEvent>listAll().stream().map(FarmEventResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        FarmEvent event = FarmEvent.findById(id);
        if (event == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FarmEventResponse.from(event)).build();
    }

    @GET
    @Path("/by-farm/{farmId}")
    public List<FarmEventResponse> byFarm(@PathParam("farmId") Long farmId) {
        return FarmEvent.<FarmEvent>find("farm.id", farmId).list().stream().map(FarmEventResponse::from).toList();
    }

    @POST
    @Transactional
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
    public Response delete(@PathParam("id") Long id) {
        if (!FarmEvent.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}