package utils;

import java.util.LinkedList;
import java.util.List;

public class Tree<T> {

    private List<Tree<T>> children;
    private T data;

    public Tree (T data) {
        this.data = data;
        this.children = new LinkedList<>();
    }

    public boolean addChild(Tree<T> child) {
        return children.add(child);
    }

    public boolean removeChild(Tree<T> child) {
        return children.remove(child);
    }

    public T getData() {
        return data;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + data.toString());

        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }

        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ?"    " : "│   "), true);
        }
    }
}
