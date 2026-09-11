package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Conversation;
import com.kishan.entity.ConversationStatus;

import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public ConversationStatus getStatus() {
        return status;
    }

    public void setStatus(ConversationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
