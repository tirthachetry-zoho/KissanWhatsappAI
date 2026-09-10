package com.kishan.dto;

import com.kishan.entity.ConversationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConversationCreate {

    @NotNull
    private Long farmerId;

    @Size(max = 50)
    private String intent;

    private ConversationStatus status = ConversationStatus.ACTIVE;
}
