package com.kishan.dto;

import com.kishan.entity.MessageDirection;
import com.kishan.entity.MessageType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MessageCreate {

    @NotNull
    private Long conversationId;

    @NotNull
    private MessageDirection direction;

    private MessageType messageType = MessageType.TEXT;

    private String content;

    @Size(max = 500)
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
