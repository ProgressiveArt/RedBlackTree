import javafx.util.Pair;

import java.util.*;

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
        List<Pair<Integer, String>>[] appendAnsi = new List[height];
        for (int i = 0; i < height; i++) {
            appendAnsi[i] = new ArrayList<>();
        }
        Map<String, NodePoint> nodeConnections = new HashMap<>();
        for (NodeHeight nodeHeight : heights) {
            int curHeight = nodeHeight.height;
            int y = curHeight * 2;
            Node node = nodeHeight.node;
            String strHeight = String.valueOf(node.getValue());
            if (node.getColor() == Color.RED) {
                appendAnsi[y].add(new Pair<>(widthIter, ANSI_RED));
            }
            int x = widthIter + strHeight.length() / 2;
            if (strHeight.length() % 2 == 0 && node.type() != TypeChild.LEFT) {
                x--;
            }
            nodeConnections.put(node.getGuid(), new NodePoint(node, x, y));
            for (int i = 0; i < strHeight.length(); i++) {
                map[y][widthIter++] = strHeight.charAt(i);
            }
            if (node.getColor() == Color.RED) {
                appendAnsi[y].add(new Pair<>(widthIter, ANSI_RESET));
            }
            widthIter++;
        }
        paintConnections(nodeConnections, map);

        for (int i = 0; i < height; i++) {
            StringBuilder sb = new StringBuilder(new String(map[i]));
            for (int j = appendAnsi[i].size() - 1; j >= 0; j--) {
                Pair<Integer, String> pair = appendAnsi[i].get(j);
                sb.insert(pair.getKey(), pair.getValue());
            }
            System.out.println(sb);
        }
    }

    private static void paintConnections(Map<String, NodePoint> nodeConnections, char[][] map) {
        for (NodePoint nodePoint : nodeConnections.values()) {
            Point startPair = nodePoint.getBottomPoint();
            int startX = startPair.x;
            int startY = startPair.y;
            Node node = nodePoint.node;
            Node leftChild = node.getChild(TypeChild.LEFT);
            Point leftNodePoint = leftChild != null
                    ? nodeConnections.get(leftChild.getGuid()).getTopPoint()
                    : null;
            Node rightChild = node.getChild(TypeChild.RIGHT);
            Point rightNodePoint = rightChild != null
                    ? nodeConnections.get(rightChild.getGuid()).getTopPoint()
                    : null;
            if (leftChild != null && rightChild != null) {
                map[startY][startX] = '╩';
                paintConnection(map, startX - 1, startY, leftNodePoint.x, leftNodePoint.y);
                paintConnection(map, startX + 1, startY, rightNodePoint.x, rightNodePoint.y);
            } else if (leftChild != null) {
                map[startY][startX] = '╝';
                paintConnection(map, startX - 1, startY, leftNodePoint.x, leftNodePoint.y);
            } else if (rightChild != null) {
                map[startY][startX] = '╚';
                paintConnection(map, startX + 1, startY, rightNodePoint.x, rightNodePoint.y);
            }
        }
    }

    private static void paintConnection(char[][] map, int x1, int y1, int x2, int y2) {
        if (y1 <= y2) {
            int temp = y1;
            y1 = y2;
            y2 = temp;
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        for (int i = minX; i <= maxX; i++) {
            map[y2][i] = '═';
        }
        map[y2][x1] = x1 < x2 ? '╔' : '╗';
        for (int i = y2 + 1; i <= y1; i++) {
            map[i][x1] = '║';
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
    }

    static class NodePoint {
        private final Node node;
        private final int x;
        private final int y;

        public NodePoint(Node node, int centerX, int centerY) {
            this.node = node;
            this.x = centerX;
            this.y = centerY;
        }

        public Point getTopPoint() {
            return new Point(x, y - 1);
        }

        public Point getBottomPoint() {
            return new Point(x, y + 1);
        }
    }

    static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
