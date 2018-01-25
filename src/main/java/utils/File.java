package utils;

import java.net.MalformedURLException;
import java.net.URL;

public class File {
    private String title;
    private String type;
    private String route;
    private URL url;

    public File(String title, String type, String route, String url) throws MalformedURLException {
        this.title = title.replaceAll("[\\\\/:*?\"<>|]", ""); // Clean invalid characters
        this.type = type;
        this.route = route;
        this.url = new URL(url);
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getRoute() {
        return route;
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return title;
    }
}
