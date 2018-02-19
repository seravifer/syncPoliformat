package model;

import com.squareup.moshi.Json;
import utils.Tree;

import java.util.HashMap;
import java.util.Map;

public class PoliformatContentEntity extends PoliformatEntity {

    @Json(name = "content_collection")
    private PoliformatFile[] contentCollection;

    public PoliformatFile[] getCollection() {
        return contentCollection;
    }

    public Tree<PoliformatFile> toFileTree() {
        Map<String, Tree<PoliformatFile>> aux = new HashMap<>();
        Tree<PoliformatFile> parent = new Tree<>(getCollection()[0]);
        Tree<PoliformatFile> root = parent;
        aux.put(parent.getData().getUrl().toString(), parent);

        for (int i = 1; i < getCollection().length; i++) {
            Tree<PoliformatFile> current = new Tree<>(getCollection()[i]);
            parent = aux.get(current.getData().getParentUrl());
            parent.addChild(current);
            aux.put(current.getData().getUrl().toString(), current);
        }

        return root;
    }
}
