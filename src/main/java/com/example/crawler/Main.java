package com.example.crawler;

import com.example.crawler.service.WebCrawler;

import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WebCrawler crawler = new WebCrawler();

        System.out.print("Enter a URL: ");
        String startUrl = scanner.nextLine();

        List<String> pages = crawler.crawl(startUrl);

        System.out.println("Found " + pages.size() + " pages:");
        for (String page : pages) {
            System.out.println(page);
        }
    }
}