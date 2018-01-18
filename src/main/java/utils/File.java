package utils;

import java.net.MalformedURLException;
import java.net.URL;

public class File {
    private String type;
    private String name;
    private URL url;

    public File(String type, String name, String url) throws MalformedURLException {
        this.type = type;
        this.name = name;
        this.url = new URL(url);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }
}
