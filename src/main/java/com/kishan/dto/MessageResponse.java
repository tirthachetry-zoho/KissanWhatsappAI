package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Message;
import com.kishan.entity.MessageDirection;
import com.kishan.entity.MessageType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}
