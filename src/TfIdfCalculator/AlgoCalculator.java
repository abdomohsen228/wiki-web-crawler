package TfIdfCalculator;
import crawler.Crawler;
import invertedIndex.InvertedIndex;
import invertedIndex.Posting;
import java.util.HashMap;
import java.util.Map;

public class AlgoCalculator {
    private InvertedIndex invertedIndex;
    private Crawler crawler;
    public AlgoCalculator(InvertedIndex invertedIndex,Crawler crawler) {
        this.invertedIndex = invertedIndex;
        this.crawler = crawler;
    }

    private double calculateTF(String term, int docID){
        Posting termPosting = invertedIndex.getIndex().get(term);
        if (termPosting == null) {
            return 0.0;
        }
        while (termPosting != null) { //keep until u find the desired doc
            if (termPosting.docId == docID) {
                return (1 + Math.log10(termPosting.dtf));
            }
            termPosting = termPosting.next;
        }
        return 0.0;
    }

    private double calculateIDF(String term){
        double totalNumOfDocuments = crawler.getNumOfDocuments();
        double numOfDocumentsContainTerm = 0.0 ;
        Posting termPosting = invertedIndex.getIndex().get(term);
        if (termPosting == null) {
            return 0.0;
        }
        while (termPosting != null) {
            numOfDocumentsContainTerm++;
            termPosting = termPosting.next;
        }
        return (Math.log10(totalNumOfDocuments / numOfDocumentsContainTerm));
    }

    public double calculateTFIDF(String term, int docID){
        double tf = calculateTF(term, docID);
        double idf = calculateIDF(term);
        return (Math.round((tf * idf) * 10000.0) / 10000.0);
    }

    public Map<Integer, Map<String, Double>> calculateTFIDF_allDocuments(){
        double totalNumOfDocuments = crawler.getNumOfDocuments();
        //    docID --> (term --> TFIDF value)
        Map<Integer, Map<String, Double>> tfidfVector = new HashMap<>();
        for (int docID = 0; docID < totalNumOfDocuments; docID++) {

            Map<String, Double> tfidfVectorForDoc = new HashMap<>();
            for (String term : invertedIndex.getIndex().keySet()) {
                double tfidfValue = calculateTFIDF(term, docID);
                tfidfVectorForDoc.put(term, tfidfValue);
            }
            tfidfVector.put(docID, tfidfVectorForDoc);
        }
        return tfidfVector;
    }

    public void printTFIDFVector(){
        Map<Integer, Map<String, Double>> tfidfResults = calculateTFIDF_allDocuments();
        for (Integer docId : tfidfResults.keySet()) {
            System.out.println("TF-IDF Values For Document " + docId+1 + ":");
            System.out.println("----------------------------------------------------------------------------------------------------");
            Map<String, Double> termTfIdf = tfidfResults.get(docId);
            for (Map.Entry<String, Double> entry : termTfIdf.entrySet()) {
                System.out.println("Term: " + entry.getKey() + " - TF-IDF: " + entry.getValue());
            }
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.println("Document ID: " + docId+1 + " Done");
        }
    }
}
