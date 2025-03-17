package com.example.crawler.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebCrawlerProcessor {
    private final Set<String> visitedUrls = new HashSet<>();
    private final String domain;

    public List<String> foundPages = new ArrayList<>();
    public String startUrl;

    public WebCrawlerProcessor(String url) {
        startUrl = normalizeUrl(url);
        domain = URI.create(startUrl).getHost();
    }

    public void crawlEntirePage(String url) {
        if (!addPage(url)) {
            return;
        }
        Elements links = getLinks(url);

        if (links == null) {
            return;
        }
        for (Element link : links) {
            String nextUrl = link.attr("abs:href");  // Get absolute URL
            if (!nextUrl.isEmpty()) {
                crawlEntirePage(nextUrl);  // Recursively crawl
            }
        }
    }

    public void crawlSinglePage(String url) {
        addPage(url);
        Elements links = getLinks(url);

        if (links == null) {
            return;
        }

        for (Element link : links) {
            String nextUrl = link.attr("abs:href");  // Get absolute URL
            if (!nextUrl.isEmpty()) {
               addPage(nextUrl);
            }
        }
    }

    protected Elements getLinks(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(5000)  // 5 second timeout
                    .get();

            // Find all links on the page
            return doc.select("a[href]");
        }  catch (IOException e) {
            //No need to make note of errors trying to crawl pdfs
            if (!url.endsWith(".pdf")) {
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }
        return null;
    }

    protected boolean addPage(String incomingUrl) {
        String url = normalizeUrl(incomingUrl);

        // Skip if already visited or not in target domain
        if (visitedUrls.contains(url) || !isInDomain(url)) {
            return false;
        }

        visitedUrls.add(url);
        foundPages.add(url);
        return true;
    }

    protected String normalizeUrl(String url) {
        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme() != null ? uri.getScheme() : "https"; // Default to https
            String path = uri.getPath() != null && !uri.getPath().isEmpty() ? uri.getPath() : "/";
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            URI normalized = new URI(scheme, uri.getHost(), path, null);
            return normalized.toString();
        } catch (Exception e) {
            return url.contains("://") ? url : "https://" + url; // Fallback with default scheme
        }
    }

    protected boolean isInDomain(String url) {
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            return host != null && (host.equalsIgnoreCase(domain) ||
                    host.endsWith("." + domain));
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getFoundPages() {
        return foundPages;
    }

    public String getStartUrl() {
        return startUrl;
    }
}
