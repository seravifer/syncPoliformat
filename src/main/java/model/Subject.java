package model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import utils.Utils;

import java.io.IOException;

public class Subject {

    private String name;
    private String id;
    //private Tree<String> files;

    public Subject(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    /**
     * Descarga y pasea la asignatura a una estructura definida ¿Tree?
     *
     */
    public void parseSubject() throws IOException {
        JsonArray items = Json.parse(Utils.getJson("content/site/" + id + ".json"))
                .asObject().get("site_collection").asArray();

        for (JsonValue item : items) {
            String titleItem = item.asObject().getString("title", null);
            String urlItem = item.asObject().getString("url", null);
            String typeItem = item.asObject().getString("type", null);
        }
    }

    /**
     * Descarga la asignatura ya parseada
     */
    public void downloadSubject() {

    }
}
