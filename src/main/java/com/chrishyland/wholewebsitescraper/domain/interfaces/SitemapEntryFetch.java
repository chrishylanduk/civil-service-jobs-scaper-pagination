package com.chrishyland.wholewebsitescraper.domain.interfaces;

import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;

import java.io.IOException;
import java.util.List;

public interface SitemapEntryFetch {
    List<SitemapEntry> listAllSitemapEntries(String sitemapUrl) throws IOException;
}
