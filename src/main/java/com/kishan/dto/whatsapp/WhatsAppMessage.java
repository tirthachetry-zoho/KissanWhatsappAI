package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class WhatsAppMessage {

    @JsonProperty("from")
    private String from;

    private String id;

    private String timestamp;

    private String type;

    private Map<String, Object> text;

    private Map<String, Object> image;

    private Map<String, Object> audio;

    private Map<String, Object> video;

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