package queryProcessor;

import java.util.*;

import textProcessor.TextProcessor;


public class QueryProcessor {

    private String query = new String();

    private List<String> Tokenize()   // normalizing the query
    {
        Map<String,String>inputText = new HashMap<>();
        inputText.put("query",query);
        Map<String,List<String>> tokenizedQuery = TextProcessor.process(inputText);
        return tokenizedQuery.get("query");
    }

    public Set<String> processQuery(String query){
        this.query = query;
        List<String> tokenizedQuery = Tokenize();

        Set<String> queryResults = new HashSet<>();
        for( String token : tokenizedQuery)
        {
            // get urls from the inverted index
            // add them to the result
            // use similarity mechanisms
        }

        return queryResults;

    }


}
