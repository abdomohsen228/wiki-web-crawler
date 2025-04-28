package queryProcessor;

import java.util.*;

import textProcessor.TextProcessor;
import TfIdfCalculator.AlgoCalculator;
import similarityCalculator.*;
import invertedIndex.*;

public class QueryProcessor {

    private String query = new String();

    public static void addOrMerge(Map<Integer, Double> map, Integer key, Double value) {
        map.merge(key, value, Double::sum);
    }

    public List<String> Tokenize()   // normalizing the query
    {
        Map<String, String> inputText = new HashMap<>();
        inputText.put("query", query);
        Map<String, List<String>> tokenizedQuery = TextProcessor.process(inputText);
        return tokenizedQuery.get("query");
    }

    public void processQuery(String query, InvertedIndex invertedIndex) {
        this.query = query;
        List<String> tokenizedQuery = Tokenize();

        Set<String> queryResults = new HashSet<>();
        Map<Integer, Map<String, Double>> allDocs = AlgoCalculator.calculateTFIDF_allDocuments(); // (DocID, (term, TF-IDF))

        // use CosineSimilarity to calculate similarity between the query and the documents
        CosineSimilarity cosineSimilarity = new CosineSimilarity();

        // calculate tf-idf for query
        Map<String, Double> queryTfIdf = cosineSimilarity.calculateQueryTfIdf(tokenizedQuery);

        // print cosine similarities
        cosineSimilarity.printCosineSimilarity(allDocs, queryTfIdf);

        Map<Integer, Double> finalMap = new HashMap<>();
        Set<String> usedWords = new HashSet<>();
        for (String token : tokenizedQuery) {
            // get postings from inverted index
            Posting posting = invertedIndex.getIndex().get(token);
            Posting cur = posting;


            while (cur != null) {
                usedWords.add(token);
                int docId = cur.getDocID();
                Map<String, Double> docTfIdf = allDocs.get(docId);

                if (docTfIdf != null) {
                    Double value = docTfIdf.get(token);
                    if (value != null) {
                        addOrMerge(finalMap, docId, value); // mapping docs with term tf-idf values
                    }
                }

                cur = cur.next;
            }
        }

        List<Map.Entry<Integer, Double>> sortedList = new ArrayList<>(finalMap.entrySet());
        sortedList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        printResults(usedWords);
    }

    public void printResults(Set<String> results) {
        if (!results.isEmpty()) {
            StringBuilder Words = new StringBuilder("The Matched Query words: ");

            for (String entry : results) {
                Words.append(entry).append(" ");
            }

            System.out.println(Words);
        }
    }

}
