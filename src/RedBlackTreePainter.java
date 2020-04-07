import java.util.HashSet;
import java.util.Set;

public class RedBlackTreePainter {
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void paint(RedBlackTree redBlackTree) {
        NodeHeight[] heights = getHeights(redBlackTree);
        printToConsole(heights);
    }

    private static void printToConsole(NodeHeight[] heights) {
        if (heights.length == 0) {
            return;
        }

        int width = String.valueOf(heights[0].node.getValue()).length();
        int height = 0;
        for (int i = 1; i < heights.length; i++) {
            width += String.valueOf(heights[i].node.getValue()).length() + 1;
            height = Math.max(heights[i].height, height);
        }
        height = (height + 1) * 2 - 1;

        char[][] map = new char[height][];
        for (int i = 0; i < height; i++) {
            map[i] = new char[width];
            for (int j = 0; j < width; j++) {
                map[i][j] = ' ';
            }
        }

        int widthIter = 0;
        for (NodeHeight nodeHeight : heights) {
            int curHeight = nodeHeight.height;
            int y = curHeight * 2;
            String strHeight = String.valueOf(nodeHeight.node.getValue());
            for (int i = 0; i < strHeight.length(); i++) {
                map[y][widthIter++] = strHeight.charAt(i);
            }
            widthIter++;
        }

        for (int i = 0; i < height; i++) {
            System.out.println(map[i]);
        }
    }

    private static NodeHeight[] getHeights(RedBlackTree redBlackTree) {
        NodeHeight[] heights = new NodeHeight[redBlackTree.getCount()];
        Set<String> visited = new HashSet<>();
        Node node = redBlackTree.getRoot();
        int nodeIter = 0;
        int nodeHeight = 0;
        while (true) {
            Node leftChild = node.getChild(TypeChild.LEFT);
            if (leftChild != null && !visited.contains(leftChild.getGuid())) {
                node = leftChild;
                nodeHeight++;
                continue;
            }
            if (!visited.contains(node.getGuid())) {
                visited.add(node.getGuid());
                heights[nodeIter++] = new NodeHeight(node, nodeHeight);
            }
            Node rightChild = node.getChild(TypeChild.RIGHT);
            if (rightChild != null && !visited.contains(rightChild.getGuid())) {
                node = rightChild;
                nodeHeight++;
                continue;
            }
            Node parent = node.getParent();
            if (parent == null) {
                break;
            }
            node = parent;
            nodeHeight--;
        }
        return heights;
    }

    static class NodeHeight {
        private Node node;
        private int height;

        public NodeHeight(Node node, int height) {
            this.node = node;
            this.height = height;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
