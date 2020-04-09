public class Program {
    public static void main(String[] args) {
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.add(1);
        redBlackTree.add(2);
        redBlackTree.add(3);
        redBlackTree.add(4);
        redBlackTree.add(5);
        redBlackTree.add(6);
        redBlackTree.add(7);
        redBlackTree.add(8);
        redBlackTree.remove(3);
        redBlackTree.remove(5);
        RedBlackTreePainter.paint(redBlackTree);
    }
}
