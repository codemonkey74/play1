package unpack.CharNumber;

public class UnpackCharNumber {
    public static class Tokenizer {
        final char[] data;
        int pos;
        public Tokenizer(final char[] a) {
            data = a;
            pos = 0;
        }

        public boolean hasNext() { return pos < data.length; }

        public char getChar() {
            char r = 0;
            if (hasNext()) {
                r = data[pos];
            }
            pos += 1;
            return r;
        }

        public int getNumber() {
            int acc = 0;
            while(hasNext() && data[pos] >= '0' && data[pos] <= '9') {
                acc = acc * 10 + (data[pos] - '0');
                pos += 1;
            }
            return acc;
        }
    }

    public static char[] unpack(final char[] a) {
        Tokenizer tk = new UnpackCharNumber.Tokenizer(a);
        StringBuilder sb = new StringBuilder();
        while (tk.hasNext()) {
            char c = tk.getChar();
            int number = tk.getNumber();
            while (number > 0) {
                sb.append(c);
                number -= 1;
            }
        }
        return sb.toString().toCharArray();
    }

    public static void main (String[] args) {
        char[] a = {'a', '1', 'f', '0', 'b', '1', '2'};
        System.out.println(unpack(a));
    }
}
