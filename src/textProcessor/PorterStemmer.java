package textProcessor;

public class PorterStemmer {
    private char[] b;
    private int i,     /* offset into b */
                i_end, /* offset to end of stemmed word */
                j, k;
    private static final int INC = 50;

    public PorterStemmer() {
        b = new char[INC];
        i = 0;
        i_end = 0;
    }

    public void add(char ch) {
        if (i == b.length) {
            char[] new_b = new char[i + INC];
            System.arraycopy(b, 0, new_b, 0, i);
            b = new_b;
        }
        b[i++] = ch;
    }

    public void add(char[] w, int wLen) {
        if (i + wLen >= b.length) {
            char[] new_b = new char[i + wLen + INC];
            System.arraycopy(b, 0, new_b, 0, i);
            b = new_b;
        }
        System.arraycopy(w, 0, b, i, wLen);
        i += wLen;
    }

    public String toString() {
        return new String(b, 0, i_end);
    }

    private  boolean cons(int i) {
        switch (b[i]) {
            case 'a': case 'e': case 'i': case 'o': case 'u':
                return false;
            case 'y':
                return (i == 0) ? true : !cons(i - 1);
            default:
                return true;
        }
    }

    private  int m() {
        int n = 0;
        int i = 0;
        while (true) {
            if (i > j) return n;
            if (!cons(i)) break;
            i++;
        }
        i++;
        while (true) {
            while (true) {
                if (i > j) return n;
                if (cons(i)) break;
                i++;
            }
            i++;
            n++;
            while (true) {
                if (i > j) return n;
                if (!cons(i)) break;
                i++;
            }
            i++;
        }
    }

    private  boolean vowelInStem() {
        for (int i = 0; i <= j; i++) if (!cons(i)) return true;
        return false;
    }

    private  boolean doublec(int j) {
        if (j < 1) return false;
        if (b[j] != b[j - 1]) return false;
        return cons(j);
    }

    private  boolean cvc(int i) {
        if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2)) return false;
        int ch = b[i];
        if (ch == 'w' || ch == 'x' || ch == 'y') return false;
        return true;
    }

    private  boolean ends(String s) {
        int l = s.length();
        int o = k - l + 1;
        if (o < 0) return false;
        for (int i = 0; i < l; i++) if (b[o + i] != s.charAt(i)) return false;
        j = k - l;
        return true;
    }

    private  void setTo(String s) {
        int l = s.length();
        int o = j + 1;
        for (int i = 0; i < l; i++) b[o + i] = s.charAt(i);
        k = j + l;
    }

    private  void step1() {
        if (b[k] == 's') {
            if (ends("sses")) k -= 2;
            else if (ends("ies")) setTo("i");
            else if (b[k - 1] != 's') k--;
        }
        if (ends("eed")) {
            if (m() > 0) k--;
        } else if ((ends("ed") || ends("ing")) && vowelInStem()) {
            k = j;
            if (ends("at")) setTo("ate");
            else if (ends("bl")) setTo("ble");
            else if (ends("iz")) setTo("ize");
            else if (doublec(k)) {
                k--;
                int ch = b[k];
                if (ch == 'l' || ch == 's' || ch == 'z') k++;
            } else if (m() == 1 && cvc(k)) setTo("e");
        }
    }

    private  void step2() {
        if (ends("y") && vowelInStem()) b[k] = 'i';
    }

    public void stem() {
        k = i - 1;
        if (k > 1) {
            step1();
            step2();
        }
        i_end = k + 1;
        i = 0;
    }
}
