package com.latenightchauffeurs.model;

import java.io.Serializable;

public class ItemCardList implements Serializable {

    String gsacard = "";
    String profileid = "";
    String acctid = "";
    String auoptout = "";
    String postal = "";
    String expiry = "";
    String accttype = "";
    String token = "";

    boolean isUpDown = false;

    public boolean isUpDown() {
        return isUpDown;
    }

    public void setUpDown(boolean upDown) {
        isUpDown = upDown;
    }

    public String getGsacard() {
        return gsacard;
    }

    public void setGsacard(String gsacard) {
        this.gsacard = gsacard;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getAcctid() {
        return acctid;
    }

    public void setAcctid(String acctid) {
        this.acctid = acctid;
    }

    public String getAuoptout() {
        return auoptout;
    }

    public void setAuoptout(String auoptout) {
        this.auoptout = auoptout;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getAccttype() {
        return accttype;
    }

    public void setAccttype(String accttype) {
        this.accttype = accttype;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
