package invertedIndex;
import sharedModels.PostingElement;

import java.util.HashMap;
import java.util.Map;


public class InvertedIndex {

        private final Map<String, PostingElement> invertedIndex;

        public InvertedIndex() {
            invertedIndex = new HashMap<>();
        }

        public void addToken(String token, int docId) {
            if (!invertedIndex.containsKey(token)) {
                invertedIndex.put(token, new PostingElement(docId));
            } else {
                PostingElement currentWord = invertedIndex.get(token);
                boolean found = false;
                while (currentWord != null) {
                    if (currentWord.docId == docId) {
                        currentWord.dtf++;
                        found = true;
                        break;
                    }
                    if (currentWord.next == null) break;
                    currentWord = currentWord.next;
                }
                if (!found && currentWord != null) {
                    currentWord.next = new PostingElement(docId);
                }

            }
        }

        public void printIndex() {
            for (Map.Entry<String, PostingElement> entry : invertedIndex.entrySet()) {
                System.out.print(entry.getKey() + " -> ");
                PostingElement current = entry.getValue();
                while (current != null) {
                    System.out.print("(DocID: " + current.docId + ", Freq: " + current.dtf + ") ");
                    current = current.next;
                }
                System.out.println();
            }
        }
        public Map<String, PostingElement> getIndex() {
            return invertedIndex;
        }
    }



