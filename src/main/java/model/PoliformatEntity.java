package model;

import com.squareup.moshi.Json;
import utils.Tree;

import java.util.HashMap;
import java.util.Map;

public class PoliformatEntity {

    @Json(name = "entityPrefix")
    private String entityPrefix;
    @Json(name = "content_collection")
    private PoliformatFile[] contentCollection;

    public Tree<PoliformatFile> toFileTree() {
        Map<String, Tree<PoliformatFile>> aux = new HashMap<>();
        Tree<PoliformatFile> parent = new Tree<>(contentCollection[0]);
        Tree<PoliformatFile> root = parent;
        aux.put(parent.getData().getUrl().toString(), parent);

        for (int i = 1; i < contentCollection.length; i++) {
            Tree<PoliformatFile> current = new Tree<>(contentCollection[i]);
            parent = aux.get(current.getData().getParentUrl());
            parent.addChild(current);
            aux.put(current.getData().getUrl().toString(), current);
        }

        return root;
    }

    public String getEntityPrefix() {
        return entityPrefix;
    }

    public PoliformatFile[] getContentCollection() {
        return contentCollection;
    }

}
