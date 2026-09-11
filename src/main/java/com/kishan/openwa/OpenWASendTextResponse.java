package com.kishan.openwa;


/** Response of POST /api/sessions/{sessionId}/messages/send-text. */
public class OpenWASendTextResponse {

    private String messageId;

    private long timestamp;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}