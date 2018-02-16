package model;

import com.squareup.moshi.Json;
import utils.FileType;
import utils.Tree;

public class PoliformatEntity {
    @Json(name = "entityPrefix")
    private String entityPrefix;
    @Json(name = "contentCollection")
    private PoliformatFile[] contentCollection;

    public Tree<FileType> toFileTree() {
        for (PoliformatFile item : contentCollection) {

        }
        return null;
    }

    public String getEntityPrefix() {
        return entityPrefix;
    }

    public void setEntityPrefix(String entityPrefix) {
        this.entityPrefix = entityPrefix;
    }

    public PoliformatFile[] getContentCollection() {
        return contentCollection;
    }

    public void setContentCollection(PoliformatFile[] contentCollection) {
        this.contentCollection = contentCollection;
    }
}
