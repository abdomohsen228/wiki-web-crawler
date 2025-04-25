import crawler.Crawler;
import textProcessor.TextProcessor;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.buildCrawler();
//        List<String> result  =  crawler.getCrawledPages();
//        System.out.println(result);
//
        Map<String, String> pagesContent = crawler.getPageTexts();
//        System.out.println(pagesContent);

        TextProcessor processor = new TextProcessor();
        Map<String, List<String>> cleanedData = processor.process(pagesContent);

        for (String url : cleanedData.keySet()) {
            System.out.println("Tokens for: " + url);
            System.out.println(cleanedData.get(url));
        }
    }
}