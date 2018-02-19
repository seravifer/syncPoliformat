package model;

import com.squareup.moshi.Json;
import model.json.adapter.Clean;

import java.net.URL;
import java.nio.file.Path;

public class PoliformatFile {

    @Json(name = "author")
    private String author;
    @Json(name = "authorId")
    private String authorId;
    @Json(name = "container")
    private Path container;
    @Json(name = "copyrightAlert")
    private String copyrightAlert;
    @Json(name = "description")
    private String description;
    @Json(name = "endDate")
    private String endDate;
    @Json(name = "fromDate")
    private String fromDate;
    @Json(name = "modifiedDate")
    private String modifiedDate;
    @Json(name = "numChildren")
    private Integer numChildren;
    @Json(name = "quota")
    private String quota;
    @Json(name = "size")
    private Integer size;
    @Clean
    @Json(name = "title")
    private String title;
    @Json(name = "type")
    private String type;
    @Json(name = "url")
    private URL url;
    @Json(name = "usage")
    private String usage;
    @Json(name = "hidden")
    private Boolean hidden;
    @Json(name = "visible")
    private Boolean visible;
    @Json(name = "entityReference")
    private String entityReference;
    @Json(name = "entityURL")
    private String entityURL;
    @Json(name = "entityTitle")
    private String entityTitle;

    public String getParentUrl() {
        String urlString = url.toString();
        int index;

        if (urlString.endsWith("/")) {
            index = urlString.substring(0, urlString.length() - 1).lastIndexOf('/');
        } else {
            index = urlString.lastIndexOf('/');
        }

        return url.toString().substring(0, index + 1);
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Path getContainer() {
        return container;
    }

    public String getCopyrightAlert() {
        return copyrightAlert;
    }

    public String getDescription() {
        return description;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public Integer getNumChildren() {
        return numChildren;
    }

    public String getQuota() {
        return quota;
    }

    public Integer getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public URL getUrl() {
        return url;
    }

    public String getUsage() {
        return usage;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public Boolean getVisible() {
        return visible;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public String getEntityURL() {
        return entityURL;
    }

    public String getEntityTitle() {
        return entityTitle;
    }

}

