package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(name = "WhatsAppValue",
        description = "Change payload: product, metadata, contacts (sender profiles) and messages (inbound items)")
public class WhatsAppValue {

    @JsonProperty("messaging_product")
    @Schema(description = "Always 'whatsapp'", example = "whatsapp")
    private String messagingProduct;

    @Schema(description = "Business metadata: {display_phone_number, phone_number_id}")
    private Map<String, Object> metadata;

    @Schema(description = "Sender contacts with profile names")
    private List<WhatsAppContact> contacts;

    @Schema(description = "Inbound messages (text, image, audio, video, document)")
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