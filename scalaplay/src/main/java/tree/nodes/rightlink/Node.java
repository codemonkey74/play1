package tree.nodes.rightlink;

import java.util.LinkedList; //as queue.

public class Node<T> {
    public Node<T> left = null;
    public Node<T> right = null;
    public Node<T> linkright = null;
    public T value = null;

    public Node(T v, Node<T> l, Node<T> r) { value = v; left = l; right = r; }
    public Node(T v) { value = v; }
    public Node() {}

    //from here down, it is a separate class (RightLinker)
    final static Node marker = new Node();

    public static Node linkRighters(Node root) { //breadth first
        LinkedList<Node> queue = new LinkedList<Node>(); //queue for breadth first
        if (root != null) { queue.add(root); }    //add root node
        queue.add(marker);  //0th level has only one element(root). add marker.
        int treelevel = 0;  //currently on level (depth) 0 of the tree.
        int treelevelsize = 1; //2^treelevel
        int treelevelcounter = treelevelsize; //potential elements on current level.
        while(queue.size() > 0) {
            Node p = queue.removeFirst();
            if (p == marker) { //reached a marker. after this one we are on next tree treelevel.
                treelevel += 1;  treelevelsize *= 2; //2^treelevel
                treelevelcounter = treelevelsize;
                continue;
            } //else
            treelevelcounter -= 1; //consume 1 from current treelevel.
            if (p.left != null) { queue.add(p.left); } //add children
            if (p.right != null) { queue.add(p.right); }
            if (treelevelcounter == 0) { //treelevel is completed: add marker
                queue.add(marker);
            }
            if (queue.size() > 0 && queue.getFirst() != marker) { //linkright to next on treelevel (queue.front)
                p.linkright = queue.getFirst();
            }
        }
        return root;
    }

    //this is the test part. get all leftmost elements and show their rightlinks.
    public static void showBrotherhood(Node levelHead) {
        Node walker = levelHead;
        while (walker != null) {
            System.out.print("" + walker.value.toString() + ", ");
            walker = walker.linkright;
        }
        if (levelHead != null) {
            System.out.println("");
            showBrotherhood(levelHead.left);
        }
    }

    public static void main (String[] args) {
        Node testRoot =
                new Node<Character>('A',
                    new Node<Character>('B',
                            new Node<Character>('D',
                                    new Node<Character>('H'), new Node<Character>('I')),
                            new Node<Character>('E',
                                    new Node<Character>('J'), new Node<Character>('K'))),
                    new Node<Character>('C',
                            new Node<Character>('F',
                                    new Node<Character>('L'), null),
                            new Node<Character>('G')));

        showBrotherhood(linkRighters(testRoot));
    }
}