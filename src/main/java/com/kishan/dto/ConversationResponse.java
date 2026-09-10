package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Conversation;
import com.kishan.entity.ConversationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationResponse {

    private Long id;
    private Long farmerId;
    private String intent;
    private ConversationStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static ConversationResponse from(Conversation conversation) {
        ConversationResponse dto = new ConversationResponse();
        dto.setId(conversation.id);
        dto.setFarmerId(conversation.getFarmer() != null ? conversation.getFarmer().id : null);
        dto.setIntent(conversation.getIntent());
        dto.setStatus(conversation.getStatus());
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setUpdatedAt(conversation.getUpdatedAt());
        return dto;
    }
}
