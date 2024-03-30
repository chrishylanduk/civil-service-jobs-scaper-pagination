# Regular whole website scraper by sitemap

## Description

A Java application which, when pointed to an XML sitemap and a PostgreSQL database, stores the list of all pages present, and a full HTML dump of each page.

When it is re-run, pages that have a "lastmod" are only re-dumped if their "lastmod" value is new.

A GitHub Action workflow is included that runs it every 1AM UTC.

It is designed for cases where regular scraping is important, but the structure of pages may vary over time, hence the
full HTML dump. Extracting data from the HTML can be done after the fact.

It does not go recursively through the sitemap, it expects that every entry is a webpage to be scraped.

## Error tolerance
- If any entries in the sitemap.xml are missing a "lastmod" or "lastmod" cannot be parsed, they will be scraped on every run
- If any status other than HTTP 200 is received when fetching the HTML of a page, retries are attempted
- If exceptions reach the top of the application, retries of the entire scraping process are attempted
- If the application exits with an error, retries of the application run are attempted by the GitHub Action runner

## Usage

Four environment variables need to be set:

1. `SITEMAP_URL` - URL to XML sitemap, e.g. `https://www.civilservicejobs.service.gov.uk/sitemap.xml`
2. `DATABASE_URL` - URL to PostgreSQL database, e.g. `postgresql://localhost:5432/mydatabase`
3. `DATABASE_USERNAME` - PostgreSQL database username
4. `DATABASE_PASSWORD` - PostgreSQL database password

Then just start the application. See .github > workflows > scrape_jobs.yml for how GitHub Actions does this.

## How it works

### Database tables
Two tables are used. If they don't exist, they are automatically created.

#### sitemap_entries (records what was present in the sitemap at every run)

- sitemap_entry_id
- url
- checked_time
- updated_time (from the sitemap)
- scrape_id
- scrape_is_new

#### scrapes (records the HTML content of each particular page scrape)

- scrape_id
- url
- scraped_time
- updated_time (from the sitemap)
- html

### When run
1. The `SITEMAP_URL` is fetched, and 'sitemap_entries' populated with every entry immediately. updated_time is set for entries with "lastmod" that can be parsed. scrape_id and scrape_is_new are not set.
2. For every sitemap entry row: 
- 'scrapes' is searched for an entry with the same url and updated_time
- If there is an existing entry in 'scrapes', the relevant row in 'sitemap_entries' is updated with scrape_id set to that of the corresponding scrape, and scrape_is_new set to false. 
- If there isn't an existing scrape, a new scrape is performed, added to 'scrapes', and the 'sitemap_entries' row is updated with scrape_id set to that of the corresponding scrape, and scrape_is_new set to true.

## Note
This is quick personal project. Test coverage is very low. Don't rely on this in its current state.