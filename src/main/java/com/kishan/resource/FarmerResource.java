package com.kishan.resource;

import com.kishan.config.AppConfig;
import com.kishan.dto.FarmerCreate;
import com.kishan.dto.FarmerResponse;
import com.kishan.entity.Farmer;
import jakarta.inject.Inject;
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

@Path("/api/farmers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Farmers", description = "Farmer profiles identified by WhatsApp phone number")
public class FarmerResource {

    @Inject
    AppConfig appConfig;

    @GET
    @Operation(summary = "List all farmers", description = "Returns every farmer profile ordered by creation.")
    @APIResponse(responseCode = "200", description = "List of farmers",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmerResponse.class)))
    public List<FarmerResponse> list() {
        return Farmer.<Farmer>listAll().stream().map(FarmerResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a farmer by id")
    @APIResponse(responseCode = "200", description = "The farmer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmerResponse.class)))
    @APIResponse(responseCode = "404", description = "Farmer not found")
    public Response get(@Parameter(description = "Farmer id", required = true) @PathParam("id") Long id) {
        Farmer farmer = Farmer.findById(id);
        if (farmer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FarmerResponse.from(farmer)).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a farmer", description = "Phone number must be unique; location defaults to the launch region when omitted.")
    @APIResponse(responseCode = "201", description = "Farmer created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmerResponse.class)))
    @APIResponse(responseCode = "409", description = "Phone number already registered")
    public Response create(@Valid FarmerCreate dto) {
        if (Farmer.find("phoneNumber", dto.getPhoneNumber()).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(java.util.Map.of("error", "Farmer with this phone number already exists")).build();
        }
        Farmer farmer = new Farmer();
        farmer.setPhoneNumber(dto.getPhoneNumber());
        farmer.setName(dto.getName());
        farmer.setLanguage(dto.getLanguage());
        farmer.setLocation(locationOrDefault(dto.getLocation()));
        farmer.persistAndFlush();
        return Response.status(Response.Status.CREATED).entity(FarmerResponse.from(farmer)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update a farmer")
    @APIResponse(responseCode = "200", description = "Updated farmer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = FarmerResponse.class)))
    @APIResponse(responseCode = "404", description = "Farmer not found")
    public Response update(@Parameter(description = "Farmer id", required = true) @PathParam("id") Long id, @Valid FarmerCreate dto) {
        Farmer farmer = Farmer.findById(id);
        if (farmer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        farmer.setPhoneNumber(dto.getPhoneNumber());
        farmer.setName(dto.getName());
        farmer.setLanguage(dto.getLanguage());
        if (dto.getLocation() != null && !dto.getLocation().isBlank()) {
            farmer.setLocation(dto.getLocation());
        }
        farmer.persist();
        return Response.ok(FarmerResponse.from(farmer)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a farmer")
    @APIResponse(responseCode = "204", description = "Farmer deleted")
    @APIResponse(responseCode = "404", description = "Farmer not found")
    public Response delete(@Parameter(description = "Farmer id", required = true) @PathParam("id") Long id) {
        if (!Farmer.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    /** Fall back to the launch-state default when no location is given. */
    private String locationOrDefault(String location) {
        return location == null || location.isBlank() ? appConfig.defaultState() : location;
    }
}