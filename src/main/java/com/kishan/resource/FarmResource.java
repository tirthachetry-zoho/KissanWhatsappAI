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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/farms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Farms", description = "Farm plots owned by farmers")
public class FarmResource {

    @GET
    @Operation(summary = "List all farms")
    @APIResponse(responseCode = "200", description = "List of farms",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmResponse.class)))
    public List<FarmResponse> list() {
        return Farm.<Farm>listAll().stream().map(FarmResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a farm by id")
    @APIResponse(responseCode = "200", description = "The farm",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmResponse.class)))
    @APIResponse(responseCode = "404", description = "Farm not found")
    public Response get(@Parameter(description = "Farm id", required = true) @PathParam("id") Long id) {
        Farm farm = Farm.findById(id);
        if (farm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FarmResponse.from(farm)).build();
    }

    @GET
    @Path("/by-farmer/{farmerId}")
    @Operation(summary = "List farms for a farmer")
    @APIResponse(responseCode = "200", description = "Farms owned by the farmer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmResponse.class)))
    public List<FarmResponse> byFarmer(@Parameter(description = "Farmer id", required = true) @PathParam("farmerId") Long farmerId) {
        return Farm.<Farm>find("farmer.id", farmerId).list().stream().map(FarmResponse::from).toList();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a farm", description = "The referenced farmer must exist.")
    @APIResponse(responseCode = "201", description = "Farm created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmResponse.class)))
    @APIResponse(responseCode = "400", description = "Farmer not found")
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
    @Operation(summary = "Delete a farm")
    @APIResponse(responseCode = "204", description = "Farm deleted")
    @APIResponse(responseCode = "404", description = "Farm not found")
    public Response delete(@Parameter(description = "Farm id", required = true) @PathParam("id") Long id) {
        if (!Farm.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}