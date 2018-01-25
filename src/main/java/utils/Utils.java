package utils;

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

    public static void downloadFile(String urlFile, String routeFile, String nameFile) throws IOException {
        URL url = new URL(urlFile);

        InputStream in = url.openStream();
        FileOutputStream fos = new FileOutputStream(new File(routeFile + nameFile));

        int length;
        byte[] buffer = new byte[2048];
        while ((length = in.read(buffer)) > -1) {
            fos.write(buffer, 0, length);
        }

        fos.close();
        in.close();
    }
}
