package find.alone.one;

public class FindAlone {
    public static int findTheLoner(int[] data) throws Exception {
        if (data.length % 2 == 0) {
            throw new Exception("Element not found");
        }
        int i = 0;
        for (; i < data.length - 1; i += 2) {
            if (data[i] != data[i + 1]) {
                break;
            }
        }
        return data[i];
    }
    
    public static int findTheLonerRec(int[] data, int s, int e) throws Exception {
        int c = (s + e) / 2;
        int center = c * 2;
        if (center + 1 < data.length && data[center] == data[center + 1]) {
            return findTheLonerRec(data, c + 1, e);
        } else {
            if (center > 0) {
                if (data[center] == data[center - 1] && c > s) {
                    return findTheLonerRec(data, s, c - 1);
                }
            }
        }
        return data[center];
    }

    public static int findTheLonerRec(int[] data) throws Exception {
        if (data.length % 2 == 0) {
            throw new Exception("Element no found");
        }
        return findTheLonerRec(data, 0, data.length / 2);
    }


    public static void main(String[] args) {
        int[][] data = {
                { 1, 1, -5},
                { 1, 1, 7, 7, -5, 2, 2},
                { 1, 1, -5, 7, 7, 2, 2},
                { -5, 1, 1},
                { 1, 1, 2, 2, 3, 3, 4, 4, 6, 6, 7, 7, 8, 8, 9, 9, -5}
        };
        try {
            for (int[] sdata: data) {
                System.out.println(findTheLoner(sdata));
                System.out.println(findTheLonerRec(sdata));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
