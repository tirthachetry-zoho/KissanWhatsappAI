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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/crops")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Crops", description = "Crops planted on farms")
public class CropResource {

    @GET
    @Operation(summary = "List all crops")
    @APIResponse(responseCode = "200", description = "List of crops",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CropResponse.class)))
    public List<CropResponse> list() {
        return Crop.<Crop>listAll().stream().map(CropResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a crop by id")
    @APIResponse(responseCode = "200", description = "The crop",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CropResponse.class)))
    @APIResponse(responseCode = "404", description = "Crop not found")
    public Response get(@Parameter(description = "Crop id", required = true) @PathParam("id") Long id) {
        Crop crop = Crop.findById(id);
        if (crop == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(CropResponse.from(crop)).build();
    }

    @GET
    @Path("/by-farm/{farmId}")
    @Operation(summary = "List crops for a farm")
    @APIResponse(responseCode = "200", description = "Crops planted on the farm",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CropResponse.class)))
    public List<CropResponse> byFarm(@Parameter(description = "Farm id", required = true) @PathParam("farmId") Long farmId) {
        return Crop.<Crop>find("farm.id", farmId).list().stream().map(CropResponse::from).toList();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a crop", description = "The referenced farm must exist.")
    @APIResponse(responseCode = "201", description = "Crop created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CropResponse.class)))
    @APIResponse(responseCode = "400", description = "Farm not found")
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
    @Operation(summary = "Delete a crop")
    @APIResponse(responseCode = "204", description = "Crop deleted")
    @APIResponse(responseCode = "404", description = "Crop not found")
    public Response delete(@Parameter(description = "Crop id", required = true) @PathParam("id") Long id) {
        if (!Crop.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}