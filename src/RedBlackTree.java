public class RedBlackTree {
    private Node root;
    private int count;

    public RedBlackTree() {
    }

    public RedBlackTree(int[] values) {
        for (int value : values) {
            add(value);
        }
    }

    public void setRoot(Node node) {
        root = node;
        root.setColor(Color.BLACK);
    }

    public Node getRoot() {
        return root;
    }

    public int getCount() {
        return count;
    }

    public void add(int value) {
        count++;

        Node node = new Node(value);
        if (root == null) {
            setRoot(node);
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
                if (grandParent.getParent() != null) {
                    setColor(grandParent, Color.RED);
                }
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
                    grandParent.getChild(TypeChild.LEFT).setColor(Color.RED);
                } else {
                    grandParent = rotateToRight(grandParent);
                    grandParent.getChild(TypeChild.RIGHT).setColor(Color.RED);
                }
                grandParent.setColor(Color.BLACK);
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

        node.setChild(nodeMid, TypeChild.LEFT);
        nodeLeft.setChild(node, TypeChild.RIGHT);
        nodeLeft.setParent(nodeParent);

        if (nodeLeft.getParent() == null) {
            setRoot(nodeLeft);
        }

        return nodeLeft;
    }

    public Node rotateToLeft(Node node) {
        Node nodeParent = node.getParent();
        Node nodeRight = node.getChild(TypeChild.RIGHT);
        Node nodeMid = nodeRight.getChild(TypeChild.LEFT);

        node.setChild(nodeMid, TypeChild.RIGHT);
        nodeRight.setChild(node, TypeChild.LEFT);
        nodeRight.setParent(nodeParent);

        if (nodeRight.getParent() == null) {
            setRoot(nodeRight);
        }

        return nodeRight;
    }
}
