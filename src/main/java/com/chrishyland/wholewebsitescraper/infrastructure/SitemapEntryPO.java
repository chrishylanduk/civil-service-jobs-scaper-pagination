package com.chrishyland.wholewebsitescraper.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sitemap_entries")
public class SitemapEntryPO {
    @Id
    @SequenceGenerator(name = "id_generator", sequenceName = "sitemap_entries_seq", initialValue = 1, allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    private Long sitemapEntryId;
    @Column(columnDefinition = "TEXT")
    private String url;
    private Instant updatedTime;
    private Instant checkedTime;
    private Long scrapeId;
    private Boolean scrapeIsNew;
}
