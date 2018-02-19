package model;

import java.util.Date;
import java.util.List;
import com.squareup.moshi.Json;
import model.json.adapter.PosixDate;

public class SubjectInfo {

    @Json(name = "contactEmail")
    private String contactEmail;
    @Json(name = "contactName")
    private String contactName;
    @PosixDate
    @Json(name = "createdDate")
    private Date createdDate;
    @Json(name = "description")
    private String description;
    @Json(name = "htmlDescription")
    private String htmlDescription;
    @Json(name = "htmlShortDescription")
    private String shortName;
    @Json(name = "id")
    private String id;
    @PosixDate
    @Json(name = "lastModified")
    private Date lastModified;
    @Json(name = "maintainRole")
    private String maintainRole;
    @PosixDate
    @Json(name = "modifiedDate")
    private Date modifiedDate;
    @Json(name = "owner")
    private String owner;
    @Json(name = "providerGroupId")
    private String providerGroupId;
    @Json(name = "reference")
    private String reference;
    @Json(name = "shortDescription")
    private String shortDescription;
    @Json(name = "title")
    private String title;
    @Json(name = "type")
    private String type;
    @Json(name = "userRoles")
    private List<String> userRoles = null;
    @Json(name = "activeEdit")
    private Boolean activeEdit;
    @Json(name = "customPageOrdered")
    private Boolean customPageOrdered;
    @Json(name = "empty")
    private Boolean empty;
    @Json(name = "joinable")
    private Boolean joinable;
    @Json(name = "pubView")
    private Boolean pubView;
    @Json(name = "published")
    private Boolean published;
    @Json(name = "softlyDeleted")
    private Boolean softlyDeleted;
    @Json(name = "entityReference")
    private String entityReference;
    @Json(name = "entityURL")
    private String entityURL;
    @Json(name = "entityId")
    private String entityId;
    @Json(name = "entityTitle")
    private String entityTitle;

    private transient String lastUpdate = "";
    private transient String name = "non-fetched-name";
    private transient SubjectManager manager = new SubjectManager(this);

    public Boolean isRealSubject() {
        return maintainRole.equals("profesor") && type.equals("siteupv");
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Object getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtmlDescription() {
        return htmlDescription;
    }

    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public String getMaintainRole() {
        return maintainRole;
    }

    public void setMaintainRole(String maintainRole) {
        this.maintainRole = maintainRole;
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

    public String getProviderGroupId() {
        return providerGroupId;
    }

    public void setProviderGroupId(String providerGroupId) {
        this.providerGroupId = providerGroupId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public Boolean getActiveEdit() {
        return activeEdit;
    }

    public void setActiveEdit(Boolean activeEdit) {
        this.activeEdit = activeEdit;
    }

    public Boolean getCustomPageOrdered() {
        return customPageOrdered;
    }

    public void setCustomPageOrdered(Boolean customPageOrdered) {
        this.customPageOrdered = customPageOrdered;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getJoinable() {
        return joinable;
    }

    public void setJoinable(Boolean joinable) {
        this.joinable = joinable;
    }

    public Boolean getPubView() {
        return pubView;
    }

    public void setPubView(Boolean pubView) {
        this.pubView = pubView;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getSoftlyDeleted() {
        return softlyDeleted;
    }

    public void setSoftlyDeleted(Boolean softlyDeleted) {
        this.softlyDeleted = softlyDeleted;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SubjectManager getManager() {
        return manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
