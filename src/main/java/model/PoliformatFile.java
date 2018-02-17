package model;

import com.squareup.moshi.Json;
import network.adapter.Clean;

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

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Path getContainer() {
        return container;
    }

    public void setContainer(Path container) {
        this.container = container;
    }

    public String getCopyrightAlert() {
        return copyrightAlert;
    }

    public void setCopyrightAlert(String copyrightAlert) {
        this.copyrightAlert = copyrightAlert;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(Integer numChildren) {
        this.numChildren = numChildren;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public String getEntityURL() {
        return entityURL;
    }

    public void setEntityURL(String entityURL) {
        this.entityURL = entityURL;
    }

    public String getEntityTitle() {
        return entityTitle;
    }

    public void setEntityTitle(String entityTitle) {
        this.entityTitle = entityTitle;
    }
}

