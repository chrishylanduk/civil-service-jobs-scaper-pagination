package com.chrishyland.csjobsdataset.infrastructure;

import com.chrishyland.csjobsdataset.domain.interfaces.URLScraper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class URLScraperImpl implements URLScraper {
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public String getHtmlOfUrl(String url) throws IOException, InterruptedException {
        System.out.println("Scraping " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
