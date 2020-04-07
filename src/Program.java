public class Program {
    public static void main(String[] args) {
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.add(12);
        redBlackTree.add(12);
        redBlackTree.add(12);
        redBlackTree.add(12);
        redBlackTree.add(12);
        redBlackTree.add(12);
        redBlackTree.add(2);
        redBlackTree.add(3);
        RedBlackTreePainter.paint(redBlackTree);
    }
}
