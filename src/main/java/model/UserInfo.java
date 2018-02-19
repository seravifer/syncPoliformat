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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
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

    public String getEntityReference() {
        return entityReference;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public URL getEntityURL() {
        return entityURL;
    }

    public void setEntityURL(URL entityURL) {
        this.entityURL = entityURL;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityTitle() {
        return entityTitle;
    }

    public void setEntityTitle(String entityTitle) {
        this.entityTitle = entityTitle;
    }

}