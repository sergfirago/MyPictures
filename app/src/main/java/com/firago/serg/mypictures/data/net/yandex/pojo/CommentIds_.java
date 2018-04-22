
package com.firago.serg.mypictures.data.net.yandex.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentIds_ {

    @SerializedName("private_resource")
    @Expose
    private String privateResource;
    @SerializedName("public_resource")
    @Expose
    private String publicResource;

    public String getPrivateResource() {
        return privateResource;
    }

    public void setPrivateResource(String privateResource) {
        this.privateResource = privateResource;
    }

    public String getPublicResource() {
        return publicResource;
    }

    public void setPublicResource(String publicResource) {
        this.publicResource = publicResource;
    }

}
