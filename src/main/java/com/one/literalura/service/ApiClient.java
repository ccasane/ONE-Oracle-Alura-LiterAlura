package com.one.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    public String getData(String url) {
        try {
            return HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder().uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString())
                .body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
