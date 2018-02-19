package utils;

import model.SubjectInfo;
import model.json.ObjectParsers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public final class Settings {

    private Settings() {}

    public static Path getSubjectsPath() {
        return Paths.get(appDirectory(), "subjects.json");
    }

    public static void initFolders() throws IOException {
        File folder = new File(poliformatDirectory());
        File directory = new File(appDirectory());
        File subjects = getSubjectsPath().toFile();

        folder.mkdir();
        directory.mkdir();

        FileOutputStream out = new FileOutputStream(subjects);
        out.write("{}".getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    public static String appDirectory() {
        String pathDirectory;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            pathDirectory = System.getenv("APPDATA");
        } else {
            pathDirectory = Paths.get(System.getProperty("user.home"), ".local", "share").toString();
        }

        return Paths.get(pathDirectory, "syncPoliformat").toString();
    }

    public static String poliformatDirectory() {
        return Paths.get(System.getProperty("user.home"), "Poliformat").toString();
    }

    public static String loadLocal(String id) throws IOException {
        return new String(Files.readAllBytes(Paths.get(appDirectory(), id + ".json")));
    }

    public static void saveRemote(String id) throws IOException {
        URL url = new URL("https://poliformat.upv.es/direct/content/site/" + id + ".json");
        Path to = Paths.get(appDirectory(), id + ".json");
        try (InputStream from = url.openStream()) {
            Files.copy(from, to);
        }
    }

    public static void updateSubject(SubjectInfo subjectInfo) {
        File file = getSubjectsPath().toFile();
        try {
            Map<String, String> jsonSubjects = ObjectParsers.LAST_SUBJECT_UPDATE_ADAPTER.fromJson(Utils.readFile(file));
            jsonSubjects.put(subjectInfo.getId(), subjectInfo.getLastUpdate());

            FileOutputStream out = new FileOutputStream(file, false);
            out.write(ObjectParsers.LAST_SUBJECT_UPDATE_ADAPTER.toJson(jsonSubjects).getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
