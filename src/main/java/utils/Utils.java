package utils;

import model.SubjectInfo;
import model.json.ObjectParsers;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class Utils {

    private Utils() {}

    public static Path getLastSubjectUpdateJsonPath() {
        return Paths.get(Utils.appDirectory(), "lastSubjectUpdate.json");
    }

    public static String getJson(String url) throws IOException {
        URL link = new URL("https://poliformat.upv.es/direct/" + url);
        HttpsURLConnection conn = (HttpsURLConnection) link.openConnection();

        return inputStreamToString(conn.getInputStream());
    }

    public static String readFile(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return new String(bytes, "UTF-8");
    }

    private static String inputStreamToString(InputStream inputStream) {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString("UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static String now() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Integer random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static String getCurso() {
        Calendar time = Calendar.getInstance();

        int year = time.get(Calendar.YEAR);
        int month = time.get(Calendar.MONTH);

        if (month < 9) return Integer.toString(year - 1);
        else return Integer.toString(year);
    }

    public static void downloadFile(URL url, String path) throws IOException {
        String name = url.toString().substring(url.toString().lastIndexOf("/"));
        int pos = name.lastIndexOf(".");

        if (pos > 0) {
            String extension = name.substring(pos);
            if (!path.contains(extension)) path += extension;
        }

        Path to = Paths.get(path);
        try (InputStream from = url.openStream()) {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        }
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

    public static void updateSubject(SubjectInfo subjectInfo) {
        File file = getLastSubjectUpdateJsonPath().toFile();
        try {
            Map<String, String> jsonSubjects = ObjectParsers.LAST_SUBJECT_UPDATE_ADAPTER.fromJson(Utils.readFile(file));
            jsonSubjects.put(subjectInfo.getId(), subjectInfo.getLastUpdate());

            FileOutputStream out = new FileOutputStream(file,false);
            out.write(ObjectParsers.LAST_SUBJECT_UPDATE_ADAPTER.toJson(jsonSubjects).getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRemote(String id) throws IOException {
        URL url = new URL("https://poliformat.upv.es/direct/content/site/" + id + ".json");
        Path to = Paths.get(appDirectory(), id + ".json");
        try (InputStream from = url.openStream()) {
            Files.copy(from, to);
        }
    }

    public static String loadLocal(String id) throws IOException {
        return new String(Files.readAllBytes(Paths.get(appDirectory(), id + ".json")));
    }

}
