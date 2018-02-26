package model;

import com.squareup.moshi.Json;

public class PoliformatSiteEntity extends PoliformatEntity {

    @Json(name = "site_collection")
    private SubjectInfo[] siteCollection;

    public SubjectInfo[] getSiteCollection() {
        return siteCollection;
    }

}
