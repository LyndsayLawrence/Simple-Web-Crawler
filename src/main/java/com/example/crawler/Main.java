package com.example.crawler;

import com.example.crawler.processor.WebCrawlerProcessor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a starting URL as an argument.");
            System.out.println("Example: ./gradlew run --args='https://sedna.com'");
            return;
        }

        String startUrl = args[0];
        WebCrawlerProcessor crawler = new WebCrawlerProcessor(startUrl);

        crawler.crawlEntirePage(crawler.getStartUrl());
//        crawler.crawlSinglePage(crawler.getStartUrl());
        List<String> pages = crawler.getFoundPages();

        System.out.println("Found " + pages.size() + " pages:");
        for (String page : pages) {
            System.out.println(page);
        }
    }
}