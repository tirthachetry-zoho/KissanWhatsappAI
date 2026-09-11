package com.kishan.openwa;


/** Request body for POST /api/sessions. */
public class OpenWACreateSessionRequest {

    /** Unique session name (alphanumeric and hyphens only). */
    private String name;

    public OpenWACreateSessionRequest() {}

    public OpenWACreateSessionRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}