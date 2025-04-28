package similarityCalculator;

import TfIdfCalculator.AlgoCalculator;

import java.util.*;

public class CosineSimilarity {

    // calculate Cosine Similarity between query , document
    public double calculateCosineSimilarity(Map<String, Double> queryTfIdf, Map<String, Double> documentTfIdf) {
        double dotProduct = 0.0;
        double queryMagnitude = 0.0;
        double docMagnitude = 0.0;

        // calculate dot product, Magnitude
        for (String term : queryTfIdf.keySet()) {
            if (documentTfIdf.containsKey(term)) {
                dotProduct += queryTfIdf.get(term) * documentTfIdf.get(term);
            }
        }

        //  magnitude for query
        for (double value : queryTfIdf.values()) {
            queryMagnitude += Math.pow(value, 2);
        }

        // magnitude for document
        for (double value : documentTfIdf.values()) {
            docMagnitude += Math.pow(value, 2);
        }

        // If either the query or document magnitude is zero, return 0 to avoid division by zero
        if (queryMagnitude == 0 || docMagnitude == 0) {
            return 0.0;
        }

        // calculate cosine similarity
        return dotProduct / (Math.sqrt(queryMagnitude) * Math.sqrt(docMagnitude));
    }

    //display cosine Similarity between the query and documents
    public void printCosineSimilarity(Map<Integer, Map<String, Double>> allDocs, Map<String, Double> queryTfIdf) {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();

        // calculate cosine Similarity between the query and each document
        Map<Integer, Double> similarityScores = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Double>> docEntry : allDocs.entrySet()) {
            int docId = docEntry.getKey();
            Map<String, Double> docTfIdf = docEntry.getValue();

            double similarity = cosineSimilarity.calculateCosineSimilarity(queryTfIdf, docTfIdf);
            similarityScores.put(docId, similarity);
        }

        // sort documents based on cosine Similarity
        List<Map.Entry<Integer, Double>> sortedSimilarityScores = new ArrayList<>(similarityScores.entrySet());
        sortedSimilarityScores.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        // display results
        for (Map.Entry<Integer, Double> entry : sortedSimilarityScores) {
            System.out.println("Document ID: " + entry.getKey() + " | Cosine Similarity: " + entry.getValue());
        }
    }

    public static Map<String, Double> calculateQueryTfIdf(List<String> tokenizedQuery) {
        Map<String, Double> queryTfIdf = new HashMap<>();

        // calculate TF-IDF for each word in the query
        for (String term : tokenizedQuery) {
            double tf = 1 + Math.log10(Collections.frequency(tokenizedQuery, term)); // calculate TF for the query
            double idf = AlgoCalculator.calculateIDF(term);  // Calculate IDF using AlgoCalculator
            double tfidf = tf * idf;
            queryTfIdf.put(term, tfidf);
        }

        return queryTfIdf;
    }
}
