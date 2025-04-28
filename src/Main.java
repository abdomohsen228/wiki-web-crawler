import crawler.Crawler;
import invertedIndex.InvertedIndex;
import textProcessor.TextProcessor;
import TfIdfCalculator.AlgoCalculator;
import queryProcessor.QueryProcessor;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // crawl the web
        System.out.println("start the crawler");
        Crawler crawler = new Crawler();
        crawler.buildCrawler();
        List<String> pages = crawler.getCrawledPages();
        Map<String, String> pageTexts = crawler.getPageTexts();
        System.out.println(pages.size() + " page");

        // preprocess text and build inverted index
        System.out.println("\nprocessing text and building inverted index");
        InvertedIndex invertedIndex = new InvertedIndex();
        Map<String, List<String>> processedTexts = TextProcessor.process(pageTexts);

        int docID = 0;
        Map<Integer, String> docIdToUrl = new HashMap<>();
        for (String url : pages) {
            List<String> tokens = processedTexts.get(url);
            if (tokens == null) continue;
            for (String token : tokens) {
                invertedIndex.addToken(token, docID);
            }
            docIdToUrl.put(docID, url);
            docID++;
        }

        // calculate TF-IDF scores
        System.out.println("\nCalculating TF-IDF...");
        AlgoCalculator algoCalculator = new AlgoCalculator(invertedIndex, crawler);
        algoCalculator.printTFIDFVector();

        // print Inverted Index
        System.out.println("\nInverted Index:");
        invertedIndex.printIndex();

        // andle user query
        System.out.println("\nProcessing Query:");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your search query: ");
        String query = scanner.nextLine();

        QueryProcessor queryProcessor = new QueryProcessor();
        queryProcessor.processQuery(query, invertedIndex);
    }
}
