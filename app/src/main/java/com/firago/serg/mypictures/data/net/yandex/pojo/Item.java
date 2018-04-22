
package com.firago.serg.mypictures.data.net.yandex.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("views_count")
    @Expose
    private Integer viewsCount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("exif")
    @Expose
    private Exif exif;
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
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("comment_ids")
    @Expose
    private CommentIds commentIds;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("revision")
    @Expose
    private Long revision;
    @SerializedName("antivirus_status")
    @Expose
    private String antivirusStatus;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("sha256")
    @Expose
    private String sha256;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("md5")
    @Expose
    private String md5;
    @SerializedName("mime_type")
    @Expose
    private String mimeType;
    @SerializedName("size")
    @Expose
    private Integer size;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exif getExif() {
        return exif;
    }

    public void setExif(Exif exif) {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public CommentIds getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(CommentIds commentIds) {
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

    public String getAntivirusStatus() {
        return antivirusStatus;
    }

    public void setAntivirusStatus(String antivirusStatus) {
        this.antivirusStatus = antivirusStatus;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
