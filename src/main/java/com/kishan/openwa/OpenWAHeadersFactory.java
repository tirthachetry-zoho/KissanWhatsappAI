package com.kishan.openwa;

import com.kishan.config.OpenWAConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.MultivaluedHashMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

/** Adds the required X-API-Key header to every OpenWA REST client call. */
@ApplicationScoped
public class OpenWAHeadersFactory implements ClientHeadersFactory {

    @Inject
    OpenWAConfig config;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
                                                 MultivaluedMap<String, String> clientOutgoingHeaders) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        config.apiKey().ifPresent(key -> headers.add("X-API-Key", key));
        return headers;
    }
}