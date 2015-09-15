package stack.boxes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TallestStack {
    public static class Box {
        public int height = 0;
        public int width = 0;
        public int depth = 0;
        public Box(int h, int w, int d) { height = h; width = w; depth = d; }
        public Box() {}

        public boolean canBeAbove(Box other) {
            return other == null || (other.height > height && other.width > width && other.depth > depth);
        }

        @Override
        public String toString() {
            return "[" + height + " x " + width + " x " + depth  + "]";
        }
    }
    public static int stackHeight(ArrayList<Box> stack) {
        int acc = 0;
        for (Box b: stack) {
            acc += b.height;
        }
        return acc;
    }

    public static ArrayList<Box> createStackR(Box[] boxes, Box bottom, Map<Box, ArrayList<Box>> memory) {
        if (bottom != null && memory.containsKey(bottom)) {
            return memory.get(bottom);
        }
        System.out.println("Trying " + bottom);
        int max_height = 0;
        ArrayList<Box> max_stack = new ArrayList<Box>();
        for (Box b: boxes) {
            if (b.canBeAbove(bottom)) {
                ArrayList<Box> new_stack = createStackR(boxes, b, memory);
                int new_height = stackHeight(new_stack);
                if (new_height > max_height) {
                    max_height = new_height;
                    max_stack = new_stack;
                }
            }
        }
        System.out.println("Done with bottom " + bottom);
        if (bottom != null) { max_stack.add(0, bottom); }
        memory.put(bottom, max_stack);
        return max_stack;
    }
    public static ArrayList<Box> createStackR(Box[] boxes) { return createStackR(boxes, null, new HashMap<Box, ArrayList<Box>>()); }

    public static void main(String[] args) {
        Box[] testBoxes = {new Box(1,1,1), new Box(2,2,2), new Box(3,3,3)};
        System.out.println(createStackR(testBoxes));
    }
}
