package model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class Poliformat {

    private HashMap<String, String> subjects;

    public Poliformat() {
        subjects = new HashMap<>();
    }

    public void syncSubjects() throws IOException {
        // https://poliformat.upv.es/direct/site.json
        // https://github.com/ralfstx/minimal-json

        URL link = new URL("https://poliformat.upv.es/direct/site.json");
        HttpsURLConnection conn = (HttpsURLConnection) link.openConnection();

        InputStreamReader json = new InputStreamReader(conn.getInputStream());
        JsonArray items = Json.parse(json).asObject().get("site_collection").asArray();

        for (JsonValue item : items) {
            String nameSubject = item.asObject().getString("htmlShortDescription", null);
            String idSubject = item.asObject().getString("id", null);
            subjects.put(nameSubject, idSubject);
        }
    }

    public HashMap<String, String> getSubjects() {
        return subjects;
    }

    public void parseSubject(String idSubject) throws IOException {
        URL link = new URL("https://poliformat.upv.es/direct/content/site/" + idSubject + ".json");
        HttpsURLConnection conn = (HttpsURLConnection) link.openConnection();

        InputStreamReader json = new InputStreamReader(conn.getInputStream());
        JsonArray items = Json.parse(json).asObject().get("site_collection").asArray();

        for (JsonValue item : items) {
            String titleItem = item.asObject().getString("title", null);
            String urlItem = item.asObject().getString("url", null);
            String typeItem = item.asObject().getString("type", null);
        }
    }
}
