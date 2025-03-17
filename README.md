# Simple Web Crawler

A simple Java-based web crawler designed to explore and list all unique pages within a single domain (e.g., `sedna.com`), without following external links.

## Features
- Crawls all pages within a specified domain (e.g., `https://sedna.com`).
- Normalizes URLs to avoid duplicates (e.g., `https://sedna.com` and `https://sedna.com/` are treated as one).
- Outputs fully qualified URLs with schemes (e.g., `https://`).
- Ignores external links (e.g., to LinkedIn or Microsoft).
- Handles basic error cases like malformed URLs or network issues.

## jsoup: Java HTML Parser
jsoup is a Java library that simplifies working with real-world HTML and XML. It offers an easy-to-use API for URL fetching, data parsing, extraction, and manipulation using DOM API methods, CSS, and xpath selectors.
See https://jsoup.org/cookbook/input/parse-document-from-string for more.


## Prerequisites
- **Java**: JDK 11 or higher (tested with Java 17).
- **Gradle**: Version 8.x or compatible
- **Jsoup**: HTML parsing library (version 1.15.3 or newer).

## Setup
### Clone the Repository
```bash
git clone <repository-url>
cd Simple-Web-Crawler
```
### Build the project
```bash
./gradlew build
```
### Run the Application
```bash
./gradlew run --args='sedna.com'
```