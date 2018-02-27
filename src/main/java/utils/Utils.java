package utils;

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
import java.util.concurrent.ThreadLocalRandom;

public final class Utils {

    private static double version = 1.0;

    private Utils() {}

    public static String fileToString(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return new String(bytes, "UTF-8");
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

    public static boolean checkVersion() {
        try {
            URL url = new URL("http://sergiavila.com/version"); // Temporal
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            double newVersion = Double.valueOf(in.readLine());
            in.close();

            return version < newVersion;
        } catch (IOException e) {
            System.err.println("Error al comprobar la versión de la aplicación.");
        }

        return false;
    }

}
