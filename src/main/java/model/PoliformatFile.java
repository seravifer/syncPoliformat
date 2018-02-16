package model;

import com.squareup.moshi.Json;

import java.net.URL;
import java.nio.file.Path;
import java.util.Date;

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
    private Date endDate;
    @Json(name = "fromDate")
    private Date fromDate;
    @Json(name = "modifiedDate")
    private String modifiedDate;
    @Json(name = "numChildren")
    private Integer numChildren;
    @Json(name = "quota")
    private String quota;
    @Json(name = "size")
    private Integer size;
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
}

