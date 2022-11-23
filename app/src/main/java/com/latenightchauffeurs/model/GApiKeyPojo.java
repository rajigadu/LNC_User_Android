package com.latenightchauffeurs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GApiKeyPojo {

    @SerializedName("data")
    @Expose
    private DataGoogleKeyPojo data;

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private String status;

    public DataGoogleKeyPojo getData() {
        return data;
    }

    public void setData(DataGoogleKeyPojo data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
