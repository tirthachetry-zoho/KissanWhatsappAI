package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Message;
import com.kishan.entity.MessageDirection;
import com.kishan.entity.MessageType;

import java.time.LocalDateTime;

public class MessageResponse {

    private Long id;
    private Long conversationId;
    private MessageDirection direction;
    private MessageType messageType;
    private String content;
    private String mediaUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public static MessageResponse from(Message message) {
        MessageResponse dto = new MessageResponse();
        dto.setId(message.id);
        dto.setConversationId(message.getConversation() != null ? message.getConversation().id : null);
        dto.setDirection(message.getDirection());
        dto.setMessageType(message.getMessageType());
        dto.setContent(message.getContent());
        dto.setMediaUrl(message.getMediaUrl());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
