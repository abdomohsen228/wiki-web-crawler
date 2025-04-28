package invertedIndex;

public class Posting {

    Posting(int id) {
        docId = id;
    }
    public Posting next = null;
    int docId;
    int dtf = 1;

    public int getDocID(){
        return docId;
    }
    public int getDTF(){
        return dtf;
    }
}
