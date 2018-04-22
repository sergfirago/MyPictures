
package com.firago.serg.mypictures.data.net.yandex.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListPictures {

    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("_embedded")
    @Expose
    private Embedded embedded;
    @SerializedName("LIST_TAG")
    @Expose
    private String name;
    @SerializedName("exif")
    @Expose
    private Exif_ exif;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("resource_id")
    @Expose
    private String resourceId;
    @SerializedName("public_url")
    @Expose
    private String publicUrl;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("views_count")
    @Expose
    private Integer viewsCount;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("comment_ids")
    @Expose
    private CommentIds_ commentIds;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("revision")
    @Expose
    private Long revision;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exif_ getExif() {
        return exif;
    }

    public void setExif(Exif_ exif) {
        this.exif = exif;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public CommentIds_ getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(CommentIds_ commentIds) {
        this.commentIds = commentIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

}
