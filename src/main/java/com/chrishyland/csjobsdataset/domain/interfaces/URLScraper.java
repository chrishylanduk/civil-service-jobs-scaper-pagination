package com.chrishyland.csjobsdataset.domain.interfaces;

import java.io.IOException;

public interface URLScraper {
    String getHtmlOfUrl(String url) throws IOException, InterruptedException;
}
