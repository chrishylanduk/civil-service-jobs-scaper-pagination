package com.chrishyland.csjobsdataset.infrastructure;

import com.chrishyland.csjobsdataset.domain.entity.SitemapEntry;
import com.chrishyland.csjobsdataset.domain.interfaces.SitemapEntryFetch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SitemapEntryFetchImpl implements SitemapEntryFetch {

    @Override
    public List<SitemapEntry> listAllSitemapEntries(String sitemapUrl) throws IOException {

        List<SitemapEntry> sitemapEntryList = new ArrayList<>();

        Document doc = Jsoup.connect(sitemapUrl).get();
        Elements pages = doc.getElementsByTag("url");

        LocalDateTime currentTime = LocalDateTime.now();

        System.out.println("Sitemap scrape page title is: " + doc.title());

        System.out.println("Found " + pages.size() + " elements in the sitemap");

        for (Element page : pages) {
            Element loc = page.getElementsByTag("loc").first();
            Element lastmod = page.getElementsByTag("lastmod").first();

            if (loc != null) {
                String[] jcodesplit = loc.text().split("jcode=");


                if (lastmod != null && jcodesplit.length > 1) {
                    int jcode = Integer.parseInt(jcodesplit[1]);

                    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                    LocalDateTime updatedTime = LocalDateTime.parse(lastmod.text(), formatter);

                    sitemapEntryList.add(SitemapEntry.builder().jcode(jcode).updatedTime(updatedTime).checkedTime(currentTime).build());
                }
            }
        }
        return sitemapEntryList;
    }
}
