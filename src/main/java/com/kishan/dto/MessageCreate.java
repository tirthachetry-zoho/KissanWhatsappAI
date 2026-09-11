package com.kishan.dto;

import com.kishan.entity.MessageDirection;
import com.kishan.entity.MessageType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "MessageCreate", description = "Payload to append a message to a conversation")
public class MessageCreate {

    @NotNull
    @Schema(description = "Conversation id (must exist)", example = "1", required = true)
    private Long conversationId;

    @NotNull
    @Schema(description = "INCOMING (farmer) or OUTGOING (assistant)", required = true)
    private MessageDirection direction;

    @Schema(description = "Message modality", defaultValue = "TEXT")
    private MessageType messageType = MessageType.TEXT;

    @Schema(description = "Text content or media caption", example = "Tomato price today")
    private String content;

    @Size(max = 500)
    @Schema(description = "Media / attachment URL for non-text messages", maxLength = 500)
    private String mediaUrl;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public MessageDirection getDirection() {
        return direction;
    }

    public void setDirection(MessageDirection direction) {
        this.direction = direction;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
