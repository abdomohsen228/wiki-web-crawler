package TfIdfCalculator;
import crawler.Crawler;
import invertedIndex.InvertedIndex;
import sharedModels.PostingElement;
import java.util.HashMap;
import java.util.Map;

// this class is responsible for calculating TF, IDF, and TF-IDF values and store it in a TF-IDF vector
public class AlgoCalculator {
    private static InvertedIndex invertedIndex;
    private static Crawler crawler;
    public AlgoCalculator(InvertedIndex invertedIndex,Crawler crawler) {
        this.invertedIndex = invertedIndex; // used to retrieve term postings
        this.crawler = crawler; // used to get total number of documents
    }

    // calculate the Term Frequency "TF" of a term in a specific document
    private static double calculateTF(String term, int docID){
        PostingElement termPosting = invertedIndex.getIndex().get(term);
        if (termPosting == null) {
            return 0.0;
        }
        // keep traversing the posting list to find the term frequency for the desired document
        while (termPosting != null) {
            if (termPosting.getDocID() == docID) {
                return (1 + Math.log10(termPosting.getDTF())); // calculate the TF
            }
            termPosting = termPosting.next;
        }
        return 0.0;
    }

    // calculate the Inverse Document Frequency (IDF) of a term across all documents
    public static double calculateIDF(String term){
        double totalNumOfDocuments = crawler.getNumOfDocuments();
        double numOfDocumentsContainTerm = 0.0 ;
        PostingElement termPosting = invertedIndex.getIndex().get(term);
        if (termPosting == null) {
            return 0.0;
        }
        // count number of documents contain the term
        while (termPosting != null) {
            numOfDocumentsContainTerm++;
            termPosting = termPosting.next;
        }
        return (Math.log10(totalNumOfDocuments / numOfDocumentsContainTerm));
    }

    // calculate the TF-IDF of a term in a specific document
    public static double calculateTFIDF(String term, int docID){
        double tf = calculateTF(term, docID);
        double idf = calculateIDF(term);
        return (tf * idf);
    }

    // build the TF-IDF vectors for all documents
    // { doc1 : {term : TF-IDF value},...}
    public static Map<Integer, Map<String, Double>> calculateTFIDF_allDocuments(){
        double totalNumOfDocuments = crawler.getNumOfDocuments();
        //    docID --> (term --> TFIDF value) vector structure
        Map<Integer, Map<String, Double>> tfidfVector = new HashMap<>();
        // loop over all documents
        for (int docID = 1; docID <= totalNumOfDocuments; docID++) {
            Map<String, Double> tfidfVectorForDoc = new HashMap<>();
            // loop over all terms in the inverted index
            for (String term : invertedIndex.getIndex().keySet()) {
                double tfidfValue = calculateTFIDF(term, docID);
                tfidfVectorForDoc.put(term, tfidfValue);
            }
            tfidfVector.put(docID, tfidfVectorForDoc);
        }
        return tfidfVector;
    }

    // print the TF, IDF, and TF-IDF values for each term in each document
    public void printTFIDFVector(){
        Map<Integer, Map<String, Double>> tfidfResults = calculateTFIDF_allDocuments();
        for (Integer docId : tfidfResults.keySet()) {
            System.out.println("TF-IDF Values For Document " + docId + ":");
            System.out.println("----------------------------------------------------------------------------------------------------");
            Map<String, Double> termTfIdf = tfidfResults.get(docId);

            for (String term : termTfIdf.keySet()) {
                double tfidf = termTfIdf.get(term);
                double tf = calculateTF(term, docId);
                double idf = calculateIDF(term);
                // formatted output for each term
                System.out.printf("Term: %-15s TF: %.4f | IDF: %.4f | TF-IDF: %.4f %n", term, tf, idf, tfidf);
            }
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.println("Document ID: " + docId + " Done");
        }
    }
}
