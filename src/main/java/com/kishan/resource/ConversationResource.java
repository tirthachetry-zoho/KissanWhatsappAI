package com.kishan.resource;

import com.kishan.dto.ConversationCreate;
import com.kishan.dto.ConversationResponse;
import com.kishan.entity.Conversation;
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

@Path("/api/conversations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Conversations", description = "WhatsApp conversation threads per farmer")
public class ConversationResource {

    @GET
    @Operation(summary = "List all conversations")
    @APIResponse(responseCode = "200", description = "List of conversations",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ConversationResponse.class)))
    public List<ConversationResponse> list() {
        return Conversation.<Conversation>listAll().stream().map(ConversationResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a conversation by id")
    @APIResponse(responseCode = "200", description = "The conversation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ConversationResponse.class)))
    @APIResponse(responseCode = "404", description = "Conversation not found")
    public Response get(@Parameter(description = "Conversation id", required = true) @PathParam("id") Long id) {
        Conversation conversation = Conversation.findById(id);
        if (conversation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ConversationResponse.from(conversation)).build();
    }

    @GET
    @Path("/by-farmer/{farmerId}")
    @Operation(summary = "List conversations for a farmer")
    @APIResponse(responseCode = "200", description = "Conversations belonging to the farmer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ConversationResponse.class)))
    public List<ConversationResponse> byFarmer(@Parameter(description = "Farmer id", required = true) @PathParam("farmerId") Long farmerId) {
        return Conversation.<Conversation>find("farmer.id", farmerId).list().stream()
                .map(ConversationResponse::from).toList();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a conversation", description = "The referenced farmer must exist.")
    @APIResponse(responseCode = "201", description = "Conversation created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ConversationResponse.class)))
    @APIResponse(responseCode = "400", description = "Farmer not found")
    public Response create(@Valid ConversationCreate dto) {
        Farmer farmer = Farmer.findById(dto.getFarmerId());
        if (farmer == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Farmer not found", "farmerId", dto.getFarmerId())).build();
        }
        Conversation conversation = new Conversation();
        conversation.setFarmer(farmer);
        conversation.setIntent(dto.getIntent());
        conversation.setStatus(dto.getStatus());
        conversation.persistAndFlush();
        return Response.status(Response.Status.CREATED).entity(ConversationResponse.from(conversation)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a conversation")
    @APIResponse(responseCode = "204", description = "Conversation deleted")
    @APIResponse(responseCode = "404", description = "Conversation not found")
    public Response delete(@Parameter(description = "Conversation id", required = true) @PathParam("id") Long id) {
        if (!Conversation.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}