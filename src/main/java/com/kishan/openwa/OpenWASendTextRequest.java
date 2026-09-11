package com.kishan.openwa;


/**
 * Request body for POST /api/sessions/{sessionId}/messages/send-text.
 * `chatId` is the WhatsApp JID, e.g. "919000000001@c.us".
 */
public class OpenWASendTextRequest {

    private String chatId;

    private String text;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OpenWASendTextRequest() {}

    public OpenWASendTextRequest(String chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }
}