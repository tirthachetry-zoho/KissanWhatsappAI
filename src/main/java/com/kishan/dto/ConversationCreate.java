package com.kishan.dto;

import com.kishan.entity.ConversationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ConversationCreate", description = "Payload to open a conversation thread for a farmer")
public class ConversationCreate {

    @NotNull
    @Schema(description = "Farmer id (must exist)", example = "1", required = true)
    private Long farmerId;

    @Size(max = 50)
    @Schema(description = "Detected intent, e.g. crop_advice, weather, market_price",
            example = "crop_advice", maxLength = 50)
    private String intent;

    @Schema(description = "Initial status", defaultValue = "ACTIVE")
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
