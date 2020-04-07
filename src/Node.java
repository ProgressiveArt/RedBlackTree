
public class Node {
    private Node parent;
    private Color color;
    private final Map<TypeChild, Node> childs;
    private final int value;

    public Node(int value) {
        this.value = value;
        this.color = Color.RED;
        childs = new HashMap<>();
        for (TypeChild typeChild : TypeChild.values()) {
            childs.put(typeChild, null);
        }
    }

    public Color getParentColor() {
        if (parent == null) {
            return null;
        }
        return parent.color;
    }

    public Color getUncleColor() {
        Node uncle = getUncle();
        return uncle == null
                ? (getGrandParent() == null ? null : Color.BLACK)
                : uncle.color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public Node getParent() {
        return parent;
    }

    public Node getGrandParent() {
        if (parent == null) {
            return null;
        }
        return parent.parent;
    }

    public Node getUncle() {
        Node grandParent = getGrandParent();
        if (grandParent == null) {
            return null;
        }
        return grandParent.getChild(parent.type() == TypeChild.RIGHT
                ? TypeChild.LEFT
                : TypeChild.RIGHT);
    }

    public Node getChild(TypeChild typeChild) {
        return childs.get(typeChild);
    }