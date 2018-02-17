package utils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import model.Subject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    private static String separator = System.getProperty("file.separator");

    public static InputStreamReader getJsonStream(String url) throws IOException {
        URL link = new URL("https://poliformat.upv.es/direct/" + url);
        HttpsURLConnection conn = (HttpsURLConnection) link.openConnection();

        return new InputStreamReader(conn.getInputStream());
    }

    public static String getJson(String url) throws IOException {
        URL link = new URL("https://poliformat.upv.es/direct/" + url);
        HttpsURLConnection conn = (HttpsURLConnection) link.openConnection();

        return inputStreamToString(conn.getInputStream());
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

        Path finalPath = Paths.get(path);
        try (InputStream in = url.openStream()) {
            Files.copy(in, finalPath);
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

    public static void updateSubject(Subject subject) {
        try {
            File file = new File(appDirectory() + "settings.json");
            FileReader reader = new FileReader(file);
            JsonObject settings = Json.parse(reader).asObject();
            reader.close();
            JsonArray jsonSubjects = settings.get("subjects").asArray();

            for (JsonValue item : jsonSubjects) {
                if (item.asObject().getString("id", null).equals(subject.getId())) {
                    item.asObject().set("lastUpdate", subject.getLastUpdate());
                    break;
                }
            }

            settings.set("subjects", jsonSubjects);
            PrintWriter printer = new PrintWriter(file, "UTF-8");
            settings.writeTo(printer);
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
     *
     * @param id
     * @throws IOException
     */
    public static void saveRemote(String id) throws IOException {
        URL url = new URL("https://poliformat.upv.es/direct/content/site/" + id + ".json");
        Path path = Paths.get(appDirectory(), id + ".json");
        try (InputStream in = url.openStream()) {
            Files.copy(in, path);
        }
    }

    public static JsonObject loadLocal(String id) throws IOException {
        File file = new File(appDirectory() + id + ".json");
        FileReader reader = new FileReader(file);
        JsonObject subjectLocal = Json.parse(reader).asObject();
        reader.close();
        return subjectLocal;
    }
}
