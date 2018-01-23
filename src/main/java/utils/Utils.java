package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
}
