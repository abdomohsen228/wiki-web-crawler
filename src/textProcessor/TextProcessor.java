package textProcessor;

import java.util.*;
import java.util.regex.Pattern;

public class TextProcessor {

    private static final Set<String> stopWords = Set.of(
            "the", "a", "an", "and", "or", "but", "is", "are", "was", "were", "in", "on",
            "at", "for", "to", "with", "by", "of", "this", "that", "these", "those",
            "he", "she", "it", "they", "we", "you", "i", "be", "been", "being",
            "do", "does", "did", "has", "have", "had", "not", "no", "yes",
            "where", "when", "why", "how", "what", "which", "who", "whom"
    );

    public static Map<String, List<String>> process(Map<String, String> inputTexts) {
        Map<String, List<String>> processedTexts = new HashMap<>();

        for (Map.Entry<String, String> entry : inputTexts.entrySet()) {
            String url = entry.getKey();
            String text = entry.getValue().toLowerCase(); // normalization
            text = text.replaceAll("[^a-zA-Z\\s]", " "); // remove punctuation

            String[] tokens = text.split("\\s+"); // tokenization
            List<String> cleaned = new ArrayList<>();

            for (String token : tokens) {
                if (token.isBlank() || stopWords.contains(token)) continue;

                PorterStemmer stemmer = new PorterStemmer();
                for (char ch : token.toCharArray()) {
                    stemmer.add(ch);
                }
                stemmer.stem();
                String stemmed =  stemmer.toString();
                if (stemmed.length() > 2) {
                    cleaned.add(stemmed);
                }
            }

            processedTexts.put(url, cleaned);
        }

        return processedTexts;
    }
}
