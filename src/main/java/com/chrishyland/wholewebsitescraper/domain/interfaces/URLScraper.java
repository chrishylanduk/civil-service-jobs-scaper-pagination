package com.chrishyland.wholewebsitescraper.domain.interfaces;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public interface URLScraper {
    String getHtmlOfUrl(String url) throws IOException, ParseException;
}
