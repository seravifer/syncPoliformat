package model;

import com.squareup.moshi.Json;
import model.json.adapter.PosixDate;

import java.net.URL;
import java.util.Date;

public class UserInfo {

    @PosixDate
    @Json(name = "createdDate")
    private Date createdDate;
    @Json(name = "displayId")
    private String displayId;
    @Json(name = "displayName")
    private String displayName;
    @Json(name = "eid")
    private String eid;
    @Json(name = "email")
    private String email;
    @Json(name = "firstName")
    private String firstName;
    @Json(name = "id")
    private String id;
    @PosixDate
    @Json(name = "lastModified")
    private Date lastModified;
    @Json(name = "lastName")
    private String lastName;
    @PosixDate
    @Json(name = "modifiedDate")
    private Date modifiedDate;
    @Json(name = "owner")
    private String owner;
    @Json(name = "reference")
    private String reference;
    @Json(name = "sortName")
    private String sortName;
    @Json(name = "type")
    private String type;
    @Json(name = "url")
    private URL url;
    @Json(name = "entityReference")
    private String entityReference;
    @Json(name = "entityURL")
    private URL entityURL;
    @Json(name = "entityId")
    private String entityId;
    @Json(name = "entityTitle")
    private String entityTitle;

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getDisplayId() {
        return displayId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEid() {
        return eid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public String getOwner() {
        return owner;
    }

    public String getReference() {
        return reference;
    }

    public String getSortName() {
        return sortName;
    }

    public String getType() {
        return type;
    }

    public URL getUrl() {
        return url;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public URL getEntityURL() {
        return entityURL;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEntityTitle() {
        return entityTitle;
    }

}