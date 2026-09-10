package com.kishan.dto;

import com.kishan.entity.MessageDirection;
import com.kishan.entity.MessageType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageCreate {

    @NotNull
    private Long conversationId;

    @NotNull
    private MessageDirection direction;

    private MessageType messageType = MessageType.TEXT;

    private String content;

    @Size(max = 500)
    private String mediaUrl;
}
