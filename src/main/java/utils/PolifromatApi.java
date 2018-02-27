package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class PolifromatApi {

    private PolifromatApi() {}

    public static String getSubject(String id) throws IOException {
        return getJson("content/site/" + id + ".json");
    }

    public static String getSubjects() throws IOException {
        return getJson("site.json");
    }

    public static String getUserInfo() throws IOException {
        return getJson("user/current.json");
    }

    private static String getJson(String url) throws IOException {
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
}
