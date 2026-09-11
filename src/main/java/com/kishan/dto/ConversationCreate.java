package com.kishan.dto;

import com.kishan.entity.ConversationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ConversationCreate {

    @NotNull
    private Long farmerId;

    @Size(max = 50)
    private String intent;

    private ConversationStatus status = ConversationStatus.ACTIVE;

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
}
