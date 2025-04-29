package sharedModels;

public class PostingElement {

    public PostingElement(int id) {
        docId = id;
    }
    public PostingElement next = null;
    public int docId;
    public int dtf = 1;

    public int getDocID(){
        return docId;
    }
    public int getDTF(){
        return dtf;
    }
}
