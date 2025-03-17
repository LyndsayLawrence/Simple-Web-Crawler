package com.example.crawler.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WebCrawlerProcessorTest {
    private WebCrawlerProcessor crawler;

    @BeforeEach
    public void setUp() {
        crawler = new WebCrawlerProcessor("https://sedna.com");
    }

    @Test
    public void testNormalizeUrlRemovesTrailingSlash() {
        String url = crawler.normalizeUrl("https://sedna.com/about/");
        assertEquals("https://sedna.com/about", url, "Should remove trailing slash");
    }

    @Test
    public void testNormalizeUrlAddsHttps() {
        String url = crawler.normalizeUrl("sedna.com");
        assertEquals("https://sedna.com", url, "Should add https:// to scheme-less URL");
    }

    @Test
    public void testIsInDomainForSameDomain() {
        assertTrue(crawler.isInDomain("https://sedna.com/about"), "Should return true for same domain");
    }

    @Test
    public void testIsInDomainForSubdomain() {
        assertTrue(crawler.isInDomain("https://sub.sedna.com"), "Should return true for subdomain");
    }

    @Test
    public void testIsInDomainForExternalDomain() {
        assertFalse(crawler.isInDomain("https://example.com"), "Should return false for external domain");
    }

    @Test
    public void testAddPageSkipsDuplicates() {
        boolean firstAdd = crawler.addPage("https://sedna.com/about");
        boolean secondAdd = crawler.addPage("https://sedna.com/about");

        assertTrue(firstAdd, "First add should succeed");
        assertFalse(secondAdd, "Second add of same URL should fail");
        assertEquals(1, crawler.getFoundPages().size(), "Should only add URL once");
    }
}