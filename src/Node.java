import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Node {
    private final String guid;
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
        this.guid = UUID.randomUUID().toString();
    }

    public String getGuid() {
        return guid;
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

    public void setChild(Node node) {
        TypeChild typeChild = node.value > value ? TypeChild.RIGHT : TypeChild.LEFT;
        setChild(node, typeChild);
    }

    public void setChild(Node node, TypeChild typeChild) {
        childs.put(typeChild, node);
        if (node != null) {
            node.parent = this;
        }
    }

    public void setNil(TypeChild typeChild) {
        childs.put(typeChild, null);
    }

    public boolean childIsNil(TypeChild typeChild) {
        return getChild(typeChild) == null;
    }

    public TypeChild type() {
        if (parent == null) {
            return null;
        }
        return value > parent.value ? TypeChild.RIGHT : TypeChild.LEFT;
    }

    public void setParent(Node node) {
        parent = node;
        if (parent != null) {
            parent.childs.put(type(), this);
        }
    }
}
