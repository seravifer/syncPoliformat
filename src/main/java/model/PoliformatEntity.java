package model;

import com.squareup.moshi.Json;
import utils.Tree;

import java.util.HashMap;
import java.util.Map;

public abstract class PoliformatEntity {

    @Json(name = "entityPrefix")
    private String entityPrefix;

    public String getEntityPrefix() {
        return entityPrefix;
    }

    public void setEntityPrefix(String entityPrefix) {
        this.entityPrefix = entityPrefix;
    }
}
