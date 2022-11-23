package com.latenightchauffeurs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatPojo {

    @SerializedName("data")
    @Expose
    private ArrayList<ChatData> data = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<ChatData> getDataList() {
        return data;
    }

    public void setDataList(ArrayList<ChatData> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
