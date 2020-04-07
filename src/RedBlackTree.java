public class RedBlackTree {
    private Node root;

    public RedBlackTree() {
    }

    public RedBlackTree(int[] values) {
        for (int value : values) {
            add(value);
        }
    }

    public void add(int value) {
        Node node = new Node(value);
        if (root == null) {
            root = node;
            return;
        }

        Node curNode = root;
        while (true) {
            TypeChild typeChild = node.getValue() > curNode.getValue()
                    ? TypeChild.RIGHT
                    : TypeChild.LEFT;
            if (!curNode.childIsNil(typeChild)) {
                curNode = curNode.getChild(typeChild);
            } else {
                break;
            }
        }

        curNode.setChild(node);
        correctViolations(node);
    }

    private void correctViolations(Node node) {
        if (node == null) {
            return;
        }

        if (node.getParentColor() == Color.RED) {
            Node grandParent = node.getGrandParent();
            if (node.getUncleColor() == Color.RED) {
                setColor(node.getParent(), Color.BLACK);
                setColor(grandParent, Color.RED);
                setColor(node.getUncle(), Color.BLACK);
                correctViolations(grandParent);
            } else {
                Node parent = node.getParent();
                if (node.type() != parent.type()) {
                    if (node.type() == TypeChild.RIGHT) {
                        parent = rotateToLeft(parent);
                        node = parent.getChild(TypeChild.LEFT);
                    } else {
                        parent = rotateToRight(parent);
                        node = parent.getChild(TypeChild.RIGHT);
                    }
                }
                if (parent.type() == TypeChild.RIGHT) {
                    grandParent = rotateToLeft(grandParent);
                    parent = grandParent.getChild(TypeChild.RIGHT);
                } else {
                    grandParent = rotateToRight(grandParent);
                    parent = grandParent.getChild(TypeChild.LEFT);
                }
                grandParent.setColor(Color.BLACK);
                parent.setColor(Color.RED);
            }
        }
    }

    public void setColor(Node node, Color color) {
        if (node != null) {
            node.setColor(color);
        }
    }

    public Node rotateToRight(Node node) {
        Node nodeParent = node.getParent();
        Node nodeLeft = node.getChild(TypeChild.LEFT);
        Node nodeMid = nodeLeft.getChild(TypeChild.RIGHT);

        nodeMid.setParent(node);
        node.setParent(nodeLeft);
        nodeLeft.setParent(nodeParent);

        if (nodeLeft.getParent() == null) {
            root = nodeLeft;
        }

        return nodeLeft;
    }

    public Node rotateToLeft(Node node) {
        Node nodeParent = node.getParent();
        Node nodeRight = node.getChild(TypeChild.RIGHT);
        Node nodeMid = nodeRight.getChild(TypeChild.LEFT);

        nodeMid.setParent(node);
        node.setParent(nodeRight);
        nodeRight.setParent(nodeParent);

        if (nodeRight.getParent() == null) {
            root = nodeRight;
        }

        return nodeRight;
    }
}
