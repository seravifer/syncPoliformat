package model;

import model.json.ObjectParsers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Poliformat {

    private Map<String, SubjectInfo> subjects;

    public Poliformat() {
        subjects = new HashMap<>();
    }

    public Map<String, SubjectInfo> getSubjects() {
        return subjects;
    }

    private void initApp() throws IOException {
        File folder = new File(Utils.poliformatDirectory());
        File subjectsUpdate = Utils.getLastSubjectUpdateJsonPath().toFile();
        File directory = new File(Utils.appDirectory());

        folder.mkdir();
        directory.mkdir();
        if (!subjectsUpdate.exists()) {
            FileOutputStream out = new FileOutputStream(subjectsUpdate);
            out.write("{}".getBytes("UTF-8"));
            out.flush();
            out.close();
        }
    }

    public void syncRemote() {
        try {
            String json = Utils.getJson("site.json");
            PoliformatSiteEntity subjectsEntity = ObjectParsers.POLIFORMAT_ENTITY_SUBJECT_ADAPTER.fromJson(json);
            subjects = Arrays.asList(subjectsEntity.getSiteCollection()).stream()
                    .filter(SubjectInfo::isRealSubject)
                    .collect(Collectors.toMap(SubjectInfo::getId, info -> info));
            fetchRealSubjectNames();
        } catch (IOException e) {
            throw new RuntimeException("Error en la descarga del indice de asignaturas", e);
        }
    }

    private void fetchRealSubjectNames() throws IOException {
        Document doc = Jsoup.connect("https://intranet.upv.es/pls/soalu/sic_asi.Lista_asig").get();

        Elements inputElements = doc.getElementsByClass("upv_enlace");

        String course = Utils.getCurso();
        for (Element inputElement : inputElements) {
            String name = inputElement.ownText().trim();
            String completeId = inputElement.getElementsByTag("span").text();
            int firstComaIndex = completeId.indexOf(',');
            String id = completeId.substring(1, firstComaIndex);
            subjects.get("GRA_" + id + "_" + course).setName(name);
        }
    }

    public void syncLocal() throws IOException {
        File file = Utils.getLastSubjectUpdateJsonPath().toFile();

        if (!file.exists()) initApp();

        Map<String, String> jsonSubjects = ObjectParsers.LAST_SUBJECT_UPDATE_ADAPTER.fromJson(Utils.readFile(file));
        for (SubjectInfo itemSubject: subjects.values()) {
            String lastUpdated = jsonSubjects.get(itemSubject.getId());
            if (lastUpdated == null) {
                jsonSubjects.put(itemSubject.getId(), "");
            } else {
                itemSubject.setLastUpdate(lastUpdated);
            }
        }

        FileOutputStream out = new FileOutputStream(file, false);
        out.write(ObjectParsers.LAST_SUBJECT_UPDATE_ADAPTER.toJson(jsonSubjects).getBytes("UTF-8"));
        out.close();
    }
}
