package com.chrishyland.wholewebsitescraper.domain.interfaces;

import java.io.IOException;

public interface URLScraper {
    String getHtmlOfUrl(String url) throws IOException, InterruptedException;
}
