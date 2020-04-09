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

    public void remove(int value) {
        Node deleteNode = root;
        while (deleteNode != null && deleteNode.getValue() != value) {
            deleteNode = deleteNode.getChild(deleteNode.getValue() < value ? TypeChild.RIGHT : TypeChild.LEFT);
        }

        if (deleteNode == null) {
            return;
        }

        count--;
        Node workNode;
        if (deleteNode.getChild(TypeChild.LEFT) == null && deleteNode.getChild(TypeChild.RIGHT) == null) {
            if (deleteNode.getParent() == null) {
                root = null;
                return;
            }
            if (deleteNode.getParent().getChild(TypeChild.LEFT) == deleteNode) {
                deleteNode.getParent().setChild(null, TypeChild.LEFT);
            } else {
                deleteNode.getParent().setChild(null, TypeChild.RIGHT);
            }
            workNode = deleteNode.getParent();
            deleteNode.setParent(null);
        } else if (deleteNode.getChild(TypeChild.LEFT) == null ^ deleteNode.getChild(TypeChild.RIGHT) == null) {
            Node leftChild = deleteNode.getChild(TypeChild.LEFT);
            Node rightChild = deleteNode.getChild(TypeChild.RIGHT);
            Node child = leftChild == null ? rightChild : leftChild;
            if (deleteNode.getParent() == null) {
                root = child;
            }
            child.setParent(deleteNode.getParent());
            workNode = child;
        } else {
            workNode = deleteNode.getChild(TypeChild.RIGHT);
            while (workNode.getChild(TypeChild.LEFT) != null) {
                workNode = workNode.getChild(TypeChild.LEFT);
            }
            workNode.setChild(deleteNode.getChild(TypeChild.LEFT), TypeChild.LEFT);
            if (deleteNode.getChild(TypeChild.RIGHT) != workNode) {
                workNode.getParent().setChild(workNode.getChild(TypeChild.RIGHT), TypeChild.LEFT);
                workNode.setChild(deleteNode.getChild(TypeChild.RIGHT), TypeChild.RIGHT);
            }
            workNode.setParent(deleteNode.getParent());
            if (workNode.getParent() == null) {
                root = workNode;
            }
        }

        if (workNode.getColor() == Color.BLACK) {
            balanceTreeAfterDelete(deleteNode);
        }
    }

    private void balanceTreeAfterDelete(Node linkedNode) {
        while (linkedNode != root && linkedNode.getColor() == Color.BLACK) {
            Node workNode;
            if (linkedNode == linkedNode.getParent().getChild(TypeChild.LEFT)) {
                workNode = linkedNode.getParent().getChild(TypeChild.RIGHT);
                if (workNode.getColor() == Color.RED) {
                    linkedNode.getParent().setColor(Color.RED);
                    workNode.setColor(Color.BLACK);
                    rotateToLeft(linkedNode.getParent());
                    workNode = linkedNode.getParent().getChild(TypeChild.RIGHT);
                }
                if (workNode.getChild(TypeChild.LEFT).getColor() == Color.BLACK &&
                        workNode.getChild(TypeChild.RIGHT).getColor() == Color.BLACK) {
                    workNode.setColor(Color.RED);
                    linkedNode = linkedNode.getParent();
                } else {
                    if (workNode.getChild(TypeChild.RIGHT).getColor() == Color.BLACK) {
                        workNode.getChild(TypeChild.LEFT).setColor(Color.BLACK);
                        workNode.setColor(Color.RED);
                        rotateToRight(workNode);
                        workNode = linkedNode.getParent().getChild(TypeChild.RIGHT);
                    }
                    linkedNode.getParent().setColor(Color.BLACK);
                    workNode.setColor(linkedNode.getParent().getColor());
                    workNode.getChild(TypeChild.RIGHT).setColor(Color.BLACK);
                    rotateToLeft(linkedNode.getParent());
                    linkedNode = root;
                }
            } else {
                workNode = linkedNode.getParent().getChild(TypeChild.LEFT);
                if (workNode.getColor() == Color.RED) {
                    linkedNode.getParent().setColor(Color.RED);
                    workNode.setColor(Color.BLACK);
                    rotateToRight(linkedNode.getParent());
                    workNode = linkedNode.getParent().getChild(TypeChild.LEFT);
                }
                if (workNode.getChild(TypeChild.RIGHT).getColor() == Color.BLACK &&
                        workNode.getChild(TypeChild.LEFT).getColor() == Color.BLACK) {
                    workNode.setColor(Color.RED);
                    linkedNode = linkedNode.getParent();
                } else {
                    if (workNode.getChild(TypeChild.LEFT).getColor() == Color.BLACK) {
                        workNode.getChild(TypeChild.RIGHT).setColor(Color.BLACK);
                        workNode.setColor(Color.RED);
                        rotateToLeft(workNode);
                        workNode = linkedNode.getParent().getChild(TypeChild.LEFT);
                    }
                    workNode.setColor(linkedNode.getParent().getColor());
                    linkedNode.getParent().setColor(Color.BLACK);
                    workNode.getChild(TypeChild.LEFT).setColor(Color.BLACK);
                    rotateToRight(linkedNode.getParent());
                    linkedNode = root;
                }
            }
        }
        linkedNode.setColor(Color.BLACK);
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
