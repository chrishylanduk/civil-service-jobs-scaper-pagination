package com.chrishyland.wholewebsitescraper.infrastructure;

import com.chrishyland.wholewebsitescraper.domain.entity.SitemapEntry;
import com.chrishyland.wholewebsitescraper.domain.interfaces.SitemapEntryFetch;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class SitemapEntryFetchImpl implements SitemapEntryFetch {

    @Override
    public List<SitemapEntry> listAllSitemapEntries(String sitemapUrl) throws IOException {

        List<SitemapEntry> sitemapEntryList = new ArrayList<>();

        Document doc = Jsoup.connect(sitemapUrl).get();
        Elements pages = doc.getElementsByTag("url");

        Instant currentTime = Instant.now();

        log.info("Found {} elements in the sitemap", pages.size());

        for (Element page : pages) {
            Element loc = page.getElementsByTag("loc").first();
            Element lastmod = page.getElementsByTag("lastmod").first();

            if (loc != null) {
                String url = loc.text();

                if (lastmod != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                    Instant updatedTime = OffsetDateTime.parse(lastmod.text(), formatter).toInstant();

                    sitemapEntryList.add(SitemapEntry.builder().url(url).updatedTime(updatedTime).checkedTime(currentTime).build());
                }
            }
        }
        return sitemapEntryList;
    }
}
