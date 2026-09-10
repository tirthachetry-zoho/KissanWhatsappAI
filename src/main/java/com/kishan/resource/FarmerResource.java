package com.kishan.resource;

import com.kishan.dto.FarmerCreate;
import com.kishan.dto.FarmerResponse;
import com.kishan.entity.Farmer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/farmers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FarmerResource {

    @GET
    public List<FarmerResponse> list() {
        return Farmer.<Farmer>listAll().stream().map(FarmerResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Farmer farmer = Farmer.findById(id);
        if (farmer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FarmerResponse.from(farmer)).build();
    }

    @POST
    @Transactional
    public Response create(@Valid FarmerCreate dto) {
        if (Farmer.find("phoneNumber", dto.getPhoneNumber()).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(java.util.Map.of("error", "Farmer with this phone number already exists")).build();
        }
        Farmer farmer = new Farmer();
        farmer.setPhoneNumber(dto.getPhoneNumber());
        farmer.setName(dto.getName());
        farmer.setLanguage(dto.getLanguage());
        farmer.setLocation(dto.getLocation());
        farmer.persistAndFlush();
        return Response.status(Response.Status.CREATED).entity(FarmerResponse.from(farmer)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid FarmerCreate dto) {
        Farmer farmer = Farmer.findById(id);
        if (farmer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        farmer.setPhoneNumber(dto.getPhoneNumber());
        farmer.setName(dto.getName());
        farmer.setLanguage(dto.getLanguage());
        farmer.setLocation(dto.getLocation());
        farmer.persist();
        return Response.ok(FarmerResponse.from(farmer)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!Farmer.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}