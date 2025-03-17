package com.example.crawler.service;

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

public class WebCrawler {
    private final Set<String> visitedUrls = new HashSet<>();  // Track visited pages to avoid cycles
    private final List<String> foundPages = new ArrayList<>(); // Store all discovered pages
    private String domain;  // The domain to restrict crawling to

    public List<String> crawl(String startUrl) {
        try {
            // Extract domain from starting URL. Note `new URL(startUrl)` is deprecated
            startUrl = normalizeUrl(startUrl);
            URI uri = URI.create(startUrl);
            domain = uri.getHost();
            crawlPage(startUrl);
        } catch (Exception e) {
            System.err.println("Error initializing crawl: " + e.getMessage());
        }
        return foundPages;
    }

    private void crawlPage(String url) {
        // Normalize URL (remove fragments, ensure proper format)
        url = normalizeUrl(url);

        // Skip if already visited or not in target domain
        if (visitedUrls.contains(url) || !isInDomain(url)) {
            return;
        }

        try {
            // Mark as visited and add to found pages
            visitedUrls.add(url);
            foundPages.add(url);

            // Fetch the page content
            Document doc = Jsoup.connect(url)
                    .timeout(5000)  // 5 second timeout
                    .get();

            // Find all links on the page
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String nextUrl = link.attr("abs:href");  // Get absolute URL
                if (!nextUrl.isEmpty()) {
                    crawlPage(nextUrl);  // Recursively crawl
                }
            }
        } catch (IOException e) {
            //No need to make not of errors trying to crawl pdfs
            if (!url.endsWith(".pdf")) {
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }
    }

    private String normalizeUrl(String url) {
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

    private boolean isInDomain(String url) {
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            return host != null && (host.equalsIgnoreCase(domain) ||
                    host.endsWith("." + domain));
        } catch (Exception e) {
            return false;
        }
    }
}
