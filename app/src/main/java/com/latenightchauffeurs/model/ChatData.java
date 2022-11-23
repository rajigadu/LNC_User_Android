package com.latenightchauffeurs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatData {

    @SerializedName("profileImageReciever")
    @Expose
    private String profileImageReciever;
    @SerializedName("profileImageSender")
    @Expose
    private String profileImageSender;
    @SerializedName("mesage")
    @Expose
    private String mesage;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("reciever")
    @Expose
    private String reciever;


    public String getProfileImageReciever() {
        return profileImageReciever;
    }

    public void setProfileImageReciever(String profileImageReciever) {
        this.profileImageReciever = profileImageReciever;
    }

    public String getProfileImageSender() {
        return profileImageSender;
    }

    public void setProfileImageSender(String profileImageSender) {
        this.profileImageSender = profileImageSender;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

}
