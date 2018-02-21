package utils;

import model.PoliformatFile;

import java.util.LinkedList;
import java.util.List;
import java.util.Deque;

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

    public List<T> merge(Tree<T> localTree) {
        List<T> newFiles = new LinkedList<>();
        Deque<Tree<T>> dequeOld = new LinkedList<>();
        Deque<Tree<T>> dequeNew = new LinkedList<>();

        dequeOld.add(this);
        dequeNew.add(localTree);

        Tree<T> ptrA = null;
        Tree<T> ptrB = null;

        while (dequeOld.size() != 0) {
            ptrA = dequeOld.getFirst();
            ptrB = dequeNew.getFirst();

            if (ptrA.getData().equals(ptrB.getData())) {
                dequeOld.removeFirst();
                dequeNew.removeFirst();

                for (Tree<T> child : ptrA.getChildren()) {
                    dequeOld.addFirst(child);
                }

                for (Tree<T> child : ptrB.getChildren()) {
                    dequeNew.addFirst(child);
                }

            } else {
                Tree<T> file = dequeNew.removeFirst();
                newFiles.add(file.getData());

                for (Tree<T> child : ptrB.getChildren()) {
                    dequeNew.addFirst(child);
                }
            }
        }

        while (dequeNew.size() != 0) {
            ptrB = dequeNew.removeFirst();

            for (Tree<T> child : ptrB.getChildren()) {
                dequeNew.addFirst(child);
            }

            newFiles.add(ptrB.getData());
        }

        return newFiles;
    }

}
