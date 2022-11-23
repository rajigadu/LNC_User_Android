package com.latenightchauffeurs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StopsList implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private ArrayList<DataStops> dataStops = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<DataStops> getDataStops() {
        return dataStops;
    }

    public void setDataStops(ArrayList<DataStops> dataStops) {
        this.dataStops = dataStops;
    }
}
