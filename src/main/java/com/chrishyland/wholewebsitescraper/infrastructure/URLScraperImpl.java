package com.chrishyland.wholewebsitescraper.infrastructure;

import com.chrishyland.wholewebsitescraper.domain.interfaces.URLScraper;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class URLScraperImpl implements URLScraper {
    private final CloseableHttpClient httpClient = HttpClientBuilder
            .create()
            .setRetryStrategy(new RetryUnlessHttp200Strategy(4, TimeValue.ofSeconds(30)))
            .build();

    // Adapted from https://stackoverflow.com/a/77930380
    static class RetryUnlessHttp200Strategy implements HttpRequestRetryStrategy {
        private final int maxRetries;
        private final TimeValue retryInterval;

        public RetryUnlessHttp200Strategy(final int maxRetries, final TimeValue retryInterval) {
            this.maxRetries = maxRetries;
            this.retryInterval = retryInterval;
        }

        @Override
        public boolean retryRequest(
                final HttpRequest request,
                final IOException exception,
                final int execCount,
                final HttpContext context) {

            return execCount <= this.maxRetries;
        }

        @Override
        public boolean retryRequest(
                final HttpResponse response,
                final int execCount,
                final HttpContext context) {

            if (response.getCode() == 200) {
                return false;
            } else {
                return execCount <= this.maxRetries;
            }
        }

        @Override
        public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
            System.out.println("Retrying HTTP request after " + retryInterval.toString());
            return retryInterval;
        }}

    @Override
    public String getHtmlOfUrl(String url) throws IOException, ParseException {
        System.out.println("Scraping " + url);
        HttpGet httpGet = new HttpGet(URI.create(url));

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }
}
