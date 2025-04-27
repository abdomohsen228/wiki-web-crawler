package invertedIndex;
import java.util.HashMap;
import java.util.Map;


public class InvertedIndex {

        private Map<String, Posting> index;

        public InvertedIndex() {
            index = new HashMap<>();
        }

        public void addToken(String token, int docId) {
            if (!index.containsKey(token)) {
                index.put(token, new Posting(docId));
            } else {
                Posting head = index.get(token);
                Posting current = head;
                boolean found = false;
                while (current != null) {
                    if (current.docId == docId) {
                        current.dtf++;
                        found = true;
                        break;
                    }
                    if (current.next == null) break;
                    current = current.next;
                }
                if (!found) {
                    current.next = new Posting(docId);
                }
            }
        }

        public void printIndex() {
            for (Map.Entry<String, Posting> entry : index.entrySet()) {
                System.out.print(entry.getKey() + " -> ");
                Posting current = entry.getValue();
                while (current != null) {
                    System.out.print("(DocID: " + current.docId + ", Freq: " + current.dtf + ") ");
                    current = current.next;
                }
                System.out.println();
            }
        }
        public Map<String, Posting> getIndex() {
            return index;
        }
    }



