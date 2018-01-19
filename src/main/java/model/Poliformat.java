package model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class Poliformat {

    private ArrayList<Subject> subjects;

    public Poliformat() {
        subjects = new ArrayList<>();
    }

    public  ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void syncSubjects() throws IOException {
        JsonArray items = Json.parse(Utils.getJson("site.json")).asObject().get("site_collection").asArray();

        for (JsonValue item : items) {
            String nameSubject = item.asObject().getString("htmlShortDescription", null);
            String idSubject = item.asObject().getString("id", null);
            subjects.add(new Subject(nameSubject, idSubject));
        }
    }
}
