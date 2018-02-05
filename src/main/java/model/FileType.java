package model;

import java.net.MalformedURLException;
import java.net.URL;

public class FileType {

    private String title;
    private String type;
    private String route;
    private URL url;

    public FileType(String title, String type, String route, String url) throws MalformedURLException {
        this.title = title.replaceAll("[\\\\/:*?\"<>|]", "").replaceAll(" +$", "");
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
