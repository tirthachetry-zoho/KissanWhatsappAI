package com.kishan.resource;

import com.kishan.dto.CropCreate;
import com.kishan.dto.CropResponse;
import com.kishan.entity.Crop;
import com.kishan.entity.Farm;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/crops")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CropResource {

    @GET
    public List<CropResponse> list() {
        return Crop.<Crop>listAll().stream().map(CropResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Crop crop = Crop.findById(id);
        if (crop == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(CropResponse.from(crop)).build();
    }

    @GET
    @Path("/by-farm/{farmId}")
    public List<CropResponse> byFarm(@PathParam("farmId") Long farmId) {
        return Crop.<Crop>find("farm.id", farmId).list().stream().map(CropResponse::from).toList();
    }

    @POST
    @Transactional
    public Response create(@Valid CropCreate dto) {
        Farm farm = Farm.findById(dto.getFarmId());
        if (farm == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Farm not found", "farmId", dto.getFarmId())).build();
        }
        Crop crop = new Crop();
        crop.setFarm(farm);
        crop.setCrop(dto.getCrop());
        crop.setVariety(dto.getVariety());
        crop.setPlantingDate(dto.getPlantingDate());
        crop.setArea(dto.getArea());
        crop.setGrowthStage(dto.getGrowthStage());
        crop.persistAndFlush();
        return Response.status(Response.Status.CREATED).entity(CropResponse.from(crop)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!Crop.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}