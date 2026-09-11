package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

@Schema(name = "WhatsAppMessage",
        description = "One inbound message: sender, id, type, and the matching content object (text, image, audio, video, document)")
public class WhatsAppMessage {

    @JsonProperty("from")
    @Schema(description = "Sender phone in international format", example = "919000000001")
    private String from;

    @Schema(description = "Provider message id, e.g. wamid.*", example = "wamid.1")
    private String id;

    @Schema(description = "Unix timestamp as string", example = "1700000000")
    private String timestamp;

    @Schema(description = "Message type", example = "text",
            enumeration = {"text", "image", "audio", "video", "document"})
    private String type;

    @Schema(description = "Text payload: {body, preview_url}")
    private Map<String, Object> text;

    @Schema(description = "Image payload: {caption, url, ...}")
    private Map<String, Object> image;

    @Schema(description = "Audio payload: {url, ...}")
    private Map<String, Object> audio;

    @Schema(description = "Video payload: {url, ...}")
    private Map<String, Object> video;

    @Schema(description = "Document payload: {url, ...}")
    private Map<String, Object> document;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getText() {
        return text;
    }

    public void setText(Map<String, Object> text) {
        this.text = text;
    }

    public Map<String, Object> getImage() {
        return image;
    }

    public void setImage(Map<String, Object> image) {
        this.image = image;
    }

    public Map<String, Object> getAudio() {
        return audio;
    }

    public void setAudio(Map<String, Object> audio) {
        this.audio = audio;
    }

    public Map<String, Object> getVideo() {
        return video;
    }

    public void setVideo(Map<String, Object> video) {
        this.video = video;
    }

    public Map<String, Object> getDocument() {
        return document;
    }

    public void setDocument(Map<String, Object> document) {
        this.document = document;
    }
}