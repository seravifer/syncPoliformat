package utils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import java.nio.file.Paths;

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
            children.get(children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public List<Pair<T, String>> merge(Tree<T> localTree) {
        String rootPath = Settings.poliformatDirectory();
        List<Pair<T, String>> newFiles = new LinkedList<>();
        Deque<Tree<T>> dequeOld = new LinkedList<>();
        Deque<Pair<Tree<T>, String>> dequeNew = new LinkedList<>();

        dequeOld.add(this);
        dequeNew.add(new Pair(localTree, rootPath));

        Tree<T> ptrA = null;
        Pair<Tree<T>, String> ptrB = null;

        while (dequeOld.size() != 0) {
            ptrA = dequeOld.getFirst();
            ptrB = dequeNew.getFirst();

            if (ptrA.getData().equals(ptrB.getKey().getData())) {
                dequeOld.removeFirst();
                dequeNew.removeFirst();

                for (Tree<T> child : ptrA.getChildren()) {
                    dequeOld.addFirst(child);
                }

                for (Tree<T> child : ptrB.getKey().getChildren()) {
                    String path = Paths.get(ptrB.getValue(), child.getData().toString()).toString();
                    dequeNew.addFirst(new Pair(child, path));
                }

            } else {
                Pair<Tree<T>, String> file = dequeNew.removeFirst();
                newFiles.add(new Pair(file.getKey().getData(), file.getValue()));

                for (Tree<T> child : ptrB.getKey().getChildren()) {
                    String path = Paths.get(ptrB.getValue(), child.getData().toString()).toString();
                    dequeNew.addFirst(new Pair(child, path));
                }
            }
        }

        while (dequeNew.size() != 0) {
            ptrB = dequeNew.removeFirst();

            for (Tree<T> child : ptrB.getKey().getChildren()) {
                String path = Paths.get(ptrB.getValue(), child.getData().toString()).toString();
                dequeNew.addFirst(new Pair(child, path));
            }

            newFiles.add(new Pair(ptrB.getKey().getData(), ptrB.getValue()));
        }

        return newFiles;
    }

}
