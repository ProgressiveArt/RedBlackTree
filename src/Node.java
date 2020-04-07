import java.util.HashMap;
import java.util.Map;

public class Node {
    private Node parent;
    private final Map<TypeChild, Node> childs;
    private final int value;

    public Node(int value) {
        this.value = value;
        childs = new HashMap<>();
        for (TypeChild typeChild : TypeChild.values()) {
            childs.put(typeChild, null);
        }
    }

    public Node getParent() {
        return parent;
    }

    public Node getChild(TypeChild typeChild) {
        return childs.get(typeChild);
    }

    public void setChild(Node node) {
        TypeChild typeChild = node.value > value ? TypeChild.RIGHT : TypeChild.LEFT;
        childs.put(typeChild, node);
        node.parent = this;
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
        return parent.value > value ? TypeChild.RIGHT : TypeChild.LEFT;
    }

    public void setParent(Node node) {
        parent = node;
        if (parent != null) {
            parent.childs.put(type(), this);
        }
    }
}
