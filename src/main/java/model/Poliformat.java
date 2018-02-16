package model;

import com.eclipsesource.json.*;
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

    public Poliformat() {
        subjects = new ArrayList<>();
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    private void initApp() throws IOException {
        File folder = new File(Utils.poliformatDirectory());
        File settings = new File(Utils.appDirectory() + "settings.json");
        File directory = new File(Utils.appDirectory());

        folder.mkdir();
        directory.mkdir();
        settings.createNewFile();

        JsonObject config = Json.object().add("subjects", Json.array());
        PrintWriter printer = new PrintWriter(settings, "UTF-8");
        config.writeTo(printer, PrettyPrint.PRETTY_PRINT);
        printer.close();
    }

    public void syncRemote() throws IOException {
        Document doc = Jsoup.connect("https://intranet.upv.es/pls/soalu/sic_asi.Lista_asig").get();

        Elements inputElements = doc.getElementsByClass("upv_enlace");
        ArrayList<Subject> tempSubjects = new ArrayList<>();

        String course = Utils.getCurso();
        for (Element inputElement : inputElements) {
            String oldName = inputElement.ownText();
            String name = oldName.substring(0, oldName.length() - 2);
            String id = inputElement.getElementsByTag("span").text().substring(1, 6);
            tempSubjects.add(new Subject(name, "GRA_" + id + "_" + course));
        }

        JsonArray items = Json.parse(Utils.getJson("site.json")).asObject().get("site_collection").asArray();

        for (JsonValue item : items) {
            String nameSubject = item.asObject().getString("htmlShortDescription", null);
            String idSubject = item.asObject().getString("id", null);
            for (Subject itemSubject : tempSubjects) {
                if (itemSubject.getId().equals(idSubject)) {
                    itemSubject.setShortName(nameSubject);
                    subjects.add(itemSubject); break;
                }
            }
        }
    }

    public void syncLocal() throws IOException {
        File file = new File(Utils.appDirectory() + "settings.json");

        if (!file.exists()) initApp();

        FileReader reader = new FileReader(file);
        JsonObject settings = Json.parse(reader).asObject();
        reader.close();
        JsonArray jsonSubjects = settings.get("subjects").asArray();

        Boolean found = false;
        for (Subject itemSubject : subjects) {
            for (JsonValue item : jsonSubjects) {
                if (item.asObject().getString("id", "").equals(itemSubject.getId())) {
                    itemSubject.setLastUpdate(item.asObject().getString("lastUpdate", ""));
                    found = true;
                }
            }
            if (!found) {
                jsonSubjects.add(Json.object().add("id", itemSubject.getId()).add("lastUpdate", ""));
            } else {
                found = false;
            }
        }

        settings.set("subjects", jsonSubjects);
        PrintWriter printer = new PrintWriter(file, "UTF-8");
        settings.writeTo(printer);
        printer.close();
    }
}
