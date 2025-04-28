package queryProcessor;

import java.util.*;

import textProcessor.TextProcessor;

import TfIdfCalculator.AlgoCalculator;

import similarityCalculato.CosineSimilarity;

import invertedIndex.*;

public class QueryProcessor {

    private String query = new String();

    public static void addOrMerge(Map<Integer, Double> map, Integer key, Double value) {
        map.merge(key, value, Double::sum);
    }

    public List<String> Tokenize()   // normalizing the query
    {
        Map<String,String>inputText = new HashMap<>();
        inputText.put("query",query);
        Map<String,List<String>> tokenizedQuery = TextProcessor.process(inputText);
        return tokenizedQuery.get("query");
    }

    public void processQuery(String query, InvertedIndex invertedIndex){
        this.query = query;
        List<String> tokenizedQuery = Tokenize();

        Set<String> queryResults = new HashSet<>();
        Map<Integer,Map<String,Double>> allDocs = AlgoCalculator.calculateTFIDF_allDocuments(); // (DocID,(term,IDF))


        // use CosineSimilarity to calculate similarity between the query and the documents
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        // calculate tf-idf
        Map<String, Double> queryTfIdf = cosineSimilarity.calculateQueryTfIdf(tokenizedQuery);
        cosineSimilarity.printCosineSimilarity(allDocs, queryTfIdf);

        Map<Integer,Double>finalMap = new HashMap<>();
        for( String token : tokenizedQuery)
        {
            // get docs from the inverted index
            Posting posting = invertedIndex.getIndex().get(token);
            Posting cur = posting;
            while(cur!=null){
                addOrMerge(finalMap,cur.getDocID(),allDocs.get(cur.getDocID()).get(token)); // mapping docs with term idfs
                cur = cur.next;
            }

        }

        List<Map.Entry<Integer, Double>> sortedList = new ArrayList<>(finalMap.entrySet());
        sortedList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        printResults(sortedList);
    }

    public void printResults(List<Map.Entry<Integer,Double>> results){
        for (Map.Entry<Integer, Double> entry : results) {
            System.out.println("Doc" + entry.getKey());
        }
    }


}
