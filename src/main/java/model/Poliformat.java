package model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Poliformat {

    private ArrayList<Subject> subjects;
    private String directory;

    public Poliformat() {
        subjects = new ArrayList<>();
    }

    public  ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void syncSubjects() throws IOException {
        Document doc = Jsoup.connect("https://intranet.upv.es/pls/soalu/sic_asi.Lista_asig").get();

        Elements inputElements = doc.getElementsByClass("upv_enlace");

        for (Element inputElement : inputElements) {
            String oldName = inputElement.ownText();
            String name = oldName.substring(0, oldName.length() - 2);
            String id = inputElement.getElementsByTag("span").text().substring(1, 6);
            subjects.add(new Subject(name, "GRA_" + id + "_" + Utils.getCurso()));
        }

        JsonArray items = Json.parse(Utils.getJson("site.json")).asObject().get("site_collection").asArray();

        for (JsonValue item : items) {
            String nameSubject = item.asObject().getString("htmlShortDescription", null);
            String idSubject = item.asObject().getString("id", null);
            //subjects.add(new Subject(nameSubject, idSubject));
            for (Subject itemSubject : subjects) {
                if (itemSubject.getId().equals(idSubject)) {
                    itemSubject.setShortName(nameSubject);
                }
            }
        }
    }

    private void settings() throws IOException {
        File dir = new File(Utils.appDirectory());
        File file = new File(Utils.appDirectory() + "settings.json");
        if (!file.exists()) {
            dir.mkdir();
            file.createNewFile();
            JsonObject config = Json.object().add("directory", "prueba");
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            config.writeTo(writer);
            writer.close();
        } else {
            FileReader reader = new FileReader(file);
            JsonObject settings = Json.parse(reader).asObject();
            directory = settings.get("directory").asString();
        }
    }
}
