package com.example.crawler.api;

import com.example.crawler.processor.WebCrawlerProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class CrawlerController {

    @GetMapping("/completeCrawl")
    public ResponseEntity<List<String>> completeCrawl(@RequestParam("url") String url) {
        try {
            WebCrawlerProcessor crawler = new WebCrawlerProcessor(url);
            crawler.crawlEntirePage(crawler.getStartUrl());  // Use entire page crawl for depth
            List<String> foundPages = crawler.getFoundPages();
            return ResponseEntity.ok(foundPages);  // Returns JSON list
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/singleCrawl")
    public ResponseEntity<List<String>> singleCrawl(@RequestParam("url") String url) {
        try {
            WebCrawlerProcessor crawler = new WebCrawlerProcessor(url);
            crawler.crawlSinglePage(crawler.getStartUrl());
            List<String> foundPages = crawler.getFoundPages();
            return ResponseEntity.ok(foundPages);  // Returns JSON list
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of("Error: " + e.getMessage()));
        }
    }
}