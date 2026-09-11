package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class WhatsAppValue {

    @JsonProperty("messaging_product")
    private String messagingProduct;

    private Map<String, Object> metadata;

    private List<WhatsAppContact> contacts;

    private List<WhatsAppMessage> messages;

    public String getMessagingProduct() {
        return messagingProduct;
    }

    public void setMessagingProduct(String messagingProduct) {
        this.messagingProduct = messagingProduct;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public List<WhatsAppContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<WhatsAppContact> contacts) {
        this.contacts = contacts;
    }

    public List<WhatsAppMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<WhatsAppMessage> messages) {
        this.messages = messages;
    }
}