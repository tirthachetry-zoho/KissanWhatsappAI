package com.kishan.resource;

import com.kishan.dto.MessageCreate;
import com.kishan.dto.MessageResponse;
import com.kishan.entity.Conversation;
import com.kishan.entity.Message;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {

    @GET
    public List<MessageResponse> list() {
        return Message.<Message>listAll().stream().map(MessageResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Message message = Message.findById(id);
        if (message == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(MessageResponse.from(message)).build();
    }

    @GET
    @Path("/by-conversation/{conversationId}")
    public List<MessageResponse> byConversation(@PathParam("conversationId") Long conversationId) {
        return Message.<Message>find("conversation.id", conversationId).list().stream()
                .map(MessageResponse::from).toList();
    }

    @POST
    @Transactional
    public Response create(@Valid MessageCreate dto) {
        Conversation conversation = Conversation.findById(dto.getConversationId());
        if (conversation == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Conversation not found", "conversationId", dto.getConversationId())).build();
        }
        Message message = new Message();
        message.setConversation(conversation);
        message.setDirection(dto.getDirection());
        message.setMessageType(dto.getMessageType());
        message.setContent(dto.getContent());
        message.setMediaUrl(dto.getMediaUrl());
        message.persistAndFlush();
        return Response.status(Response.Status.CREATED).entity(MessageResponse.from(message)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!Message.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}