package com.kishan.openwa;


import java.util.List;

/** An OpenWA webhook registration as returned by GET/POST /api/sessions/{id}/webhooks. */
public class OpenWAWebhook {

    private String id;

    private String url;

    private List<String> events;

    private Boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}