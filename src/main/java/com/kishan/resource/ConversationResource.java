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

import java.util.List;
import java.util.Map;

@Path("/api/conversations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConversationResource {

    @GET
    public List<ConversationResponse> list() {
        return Conversation.<Conversation>listAll().stream().map(ConversationResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Conversation conversation = Conversation.findById(id);
        if (conversation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ConversationResponse.from(conversation)).build();
    }

    @GET
    @Path("/by-farmer/{farmerId}")
    public List<ConversationResponse> byFarmer(@PathParam("farmerId") Long farmerId) {
        return Conversation.<Conversation>find("farmer.id", farmerId).list().stream()
                .map(ConversationResponse::from).toList();
    }

    @POST
    @Transactional
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
    public Response delete(@PathParam("id") Long id) {
        if (!Conversation.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}