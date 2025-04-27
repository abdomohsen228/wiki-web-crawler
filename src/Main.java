import TfIdfCalculator.AlgoCalculator;
import crawler.Crawler;
import textProcessor.TextProcessor;
import queryProcessor.QueryProcessor;
import invertedIndex.InvertedIndex;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.buildCrawler();
        List<String> result  =  crawler.getCrawledPages();
//        System.out.println(result);
        Map<String, String> pagesContent = crawler.getPageTexts();
//        System.out.println(pagesContent);

        TextProcessor processor = new TextProcessor();
        Map<String, List<String>> cleanedData = processor.process(pagesContent);
//
//        for (String url : cleanedData.keySet()) {
//            System.out.println("Tokens for: " + url);
//            System.out.println(cleanedData.get(url));
//        }
        InvertedIndex invertedIndex = new InvertedIndex();
        int docIdCounter = 1;

        for (String url : cleanedData.keySet()) {
            int docId = docIdCounter++;
            List<String> tokens = cleanedData.get(url);

            for (String token : tokens) {
                invertedIndex.addToken(token, docId);
            }
        }
        AlgoCalculator tfidfCalculator = new AlgoCalculator(invertedIndex, crawler);

        //invertedIndex.printIndex();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Search: ");
        String inputQuery = scanner.nextLine();
        QueryProcessor query = new QueryProcessor();
        query.processQuery(inputQuery,invertedIndex);

        //tfidfCalculator.printTFIDFVector();

    }
}
