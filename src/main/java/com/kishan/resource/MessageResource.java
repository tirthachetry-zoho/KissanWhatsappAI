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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Messages", description = "Incoming/outgoing chat messages inside conversations")
public class MessageResource {

    @GET
    @Operation(summary = "List all messages")
    @APIResponse(responseCode = "200", description = "List of messages",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MessageResponse.class)))
    public List<MessageResponse> list() {
        return Message.<Message>listAll().stream().map(MessageResponse::from).toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a message by id")
    @APIResponse(responseCode = "200", description = "The message",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MessageResponse.class)))
    @APIResponse(responseCode = "404", description = "Message not found")
    public Response get(@Parameter(description = "Message id", required = true) @PathParam("id") Long id) {
        Message message = Message.findById(id);
        if (message == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(MessageResponse.from(message)).build();
    }

    @GET
    @Path("/by-conversation/{conversationId}")
    @Operation(summary = "List messages in a conversation")
    @APIResponse(responseCode = "200", description = "Messages in the conversation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MessageResponse.class)))
    public List<MessageResponse> byConversation(@Parameter(description = "Conversation id", required = true) @PathParam("conversationId") Long conversationId) {
        return Message.<Message>find("conversation.id", conversationId).list().stream()
                .map(MessageResponse::from).toList();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a message", description = "The referenced conversation must exist.")
    @APIResponse(responseCode = "201", description = "Message created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = MessageResponse.class)))
    @APIResponse(responseCode = "400", description = "Conversation not found")
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
    @Operation(summary = "Delete a message")
    @APIResponse(responseCode = "204", description = "Message deleted")
    @APIResponse(responseCode = "404", description = "Message not found")
    public Response delete(@Parameter(description = "Message id", required = true) @PathParam("id") Long id) {
        if (!Message.deleteById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}