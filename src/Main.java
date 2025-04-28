import crawler.Crawler;
import invertedIndex.InvertedIndex;
import textProcessor.TextProcessor;
import TfIdfCalculator.AlgoCalculator;
import queryProcessor.QueryProcessor;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Step 1: Crawl the web
        System.out.println("Starting the crawler...");
        Crawler crawler = new Crawler();
        crawler.buildCrawler();
        List<String> pages = crawler.getCrawledPages();
        Map<String, String> pageTexts = crawler.getPageTexts();
        System.out.println("Crawled " + pages.size() + " pages.");

        // Step 2: Preprocess text and build inverted index
        System.out.println("\nProcessing text and building inverted index...");
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

        // Step 3: Calculate TF-IDF scores
        System.out.println("\nCalculating TF-IDF...");
        AlgoCalculator algoCalculator = new AlgoCalculator(invertedIndex, crawler);

        // Step 4: Print Inverted Index
        System.out.println("\nInverted Index:");
        invertedIndex.printIndex();

        // Step 5: Handle user query
        System.out.println("\nProcessing Query:");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your search query: ");
        String query = scanner.nextLine();

        QueryProcessor queryProcessor = new QueryProcessor();
        queryProcessor.processQuery(query, invertedIndex);
    }
}
