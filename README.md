# Regular whole website scraper by sitemap

## Description

A Java application which, when pointed to an XML sitemap and a PostgreSQL database, stores the list of all pages present
that have a "lastmod" property, and a full HTML dump of each page.

If it's re-run, it only re-stores a page's HTML if "lastmod" has changed.

A GitHub Action workflow is included that runs it every 1AM UTC.

It is designed for cases where regular scraping is important, but the structure of pages may vary over time, hence the
full HTML dump.

## Usage

Four environment variables need to be set:

1. `SITEMAP_URL` - URL to XML sitemap, e.g. `https://www.civilservicejobs.service.gov.uk/sitemap.xml`
2. `DATABASE_URL` - URL to PostgreSQL database, e.g. `postgresql://localhost:5432/mydatabase`
3. `DATABASE_USERNAME` - PostgreSQL database username
4. `DATABASE_PASSWORD` - PostgreSQL database password
