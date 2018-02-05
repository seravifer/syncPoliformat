package utils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import model.Subject;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static InputStreamReader getJson(String url) throws IOException {
        URL link = new URL("https://poliformat.upv.es/direct/" + url);
        HttpsURLConnection conn = (HttpsURLConnection) link.openConnection();

        return new InputStreamReader(conn.getInputStream());
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

        InputStream input = url.openStream();
        FileOutputStream output = new FileOutputStream(new File(path));

        int length;
        byte[] buffer = new byte[1024];
        while ((length = input.read(buffer)) > -1) {
            output.write(buffer, 0, length);
        }

        output.close();
        input.close();
    }

    public static String appDirectory() {
        String pathFile;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            pathFile = System.getenv("APPDATA");
        } else {
            pathFile = System.getProperty("user.home"); // ¿En que carpeta se guarda en Linux/Mac?
        }

        return pathFile + System.getProperty("file.separator") + "syncPoliformaT" + System.getProperty("file.separator");
    }

    public static String poliformatDirectory() {
        return System.getProperty("user.home") +
                System.getProperty("file.separator") +
                "PoliformaT" +
                System.getProperty("file.separator");
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
}
