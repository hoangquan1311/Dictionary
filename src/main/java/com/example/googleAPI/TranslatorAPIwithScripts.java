package com.example.googleAPI;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TranslatorAPIwithScripts {

    public static String translate(String langFrom, String langTo, String text) throws IOException, InterruptedException {
        String urlStr = "https://script.google.com/macros/s/AKfycbz29fO5jCKw8Y-W0TePZLFM9jyI8urgaON2R9kMtC35l8dIl2aWQ9tqqYFnIkfERCI/exec";
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        URI uri = URI.create(urlStr + "?q=" + encodedText + "&target=" + langTo + "&source=" + langFrom);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", "Mozilla/5.0")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check for redirection and follow it
        if (response.statusCode() == 302) {
            String redirectedUrl = response.headers().firstValue("Location").orElse("");
            uri = URI.create(redirectedUrl);

            request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        return response.body();
    }

    public static void main(String[] args) {
        try {
            String langFrom = "En";
            String langTo = "Vi";
            String text = "Xin chào!";
            String translatedText = translate(langFrom, langTo, text);
            System.out.println(translatedText);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
