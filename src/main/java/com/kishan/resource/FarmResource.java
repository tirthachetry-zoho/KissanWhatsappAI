package com.kishan.resource;

import com.kishan.dto.FarmCreate;
import com.kishan.dto.FarmResponse;
import com.kishan.entity.Farm;
import com.kishan.entity.Farmer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/farms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FarmResource {

    @GET
    public List<FarmResponse> list() {
        return Farm.<Farm>listAll().stream().map(FarmResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Farm farm = Farm.findById(id);
        if (farm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FarmResponse.from(farm)).build();
    }

    @GET
    @Path("/by-farmer/{farmerId}")
    public List<FarmResponse> byFarmer(@PathParam("farmerId") Long farmerId) {
        return Farm.<Farm>find("farmer.id", farmerId).list().stream().map(FarmResponse::from).toList();
    }

    @POST
    @Transactional
    public Response create(@Valid FarmCreate dto) {
        Farmer farmer = Farmer.findById(dto.getFarmerId());
        if (farmer == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Farmer not found", "farmerId", dto.getFarmerId())).build();
        }
        Farm farm = new Farm();
        farm.setFarmer(farmer);
        farm.setLocation(dto.getLocation());
        farm.setArea(dto.getArea());
        farm.setAreaUnit(dto.getAreaUnit());
        farm.setSoilType(dto.getSoilType());
        farm.persistAndFlush();
        return Response.status(Response.Status.CREATED).entity(FarmResponse.from(farm)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!Farm.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}