package com.chrishyland.csjobsdataset.domain.interfaces;

import com.chrishyland.csjobsdataset.domain.entity.SitemapEntry;

import java.io.IOException;
import java.util.List;

public interface SitemapEntryFetch {
    List<SitemapEntry> listAllSitemapEntries(String sitemapUrl) throws IOException;
}
