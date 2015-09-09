package invert.text.j;

public class TextInverter {
    static public char[] inverter(char[] text, int from, int to) {
        for(int a = from, b = to - 1; a < b; ++a, --b) {
            char c = text[a];
            text[a] = text[b];
            text[b] = c;
        }
        return text;
    }

    static public char[] inverter(char[] text) { return inverter(text, 0, text.length); }

    static public char[] wordInverter(char[] text) {
        int l = 0;
        for (int a = 0; a < text.length; ++a) {
            if (text[a] == ' ') {
                inverter(text, l, a);
                l = a + 1;
            }
        }
        inverter(text, l, text.length);
        inverter(text);
        return text;
    }


    public static void main(String[] args) {
        System.out.println(inverter("Hi you there".toCharArray()));
        System.out.println(wordInverter("Hi you there".toCharArray()));
    }
}
