package com.latenightchauffeurs.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by narip on 2/4/2017.
 */
public class Global extends Application {
    public static Activity mCurrentActivity;
    public static HashMap<String, Object> mapMain = new HashMap<String, Object>();
    public static HashMap<String, Object> dmap = new HashMap<String, Object>();
    public static HashMap<String, Object> mapData = new HashMap<String, Object>();
    public static HashMap<String, Object> eventdata = new HashMap<String, Object>();
    public static HashMap<String, Object> tempData = new HashMap<String, Object>();
    public static HashMap<String, Object> profiledata = new HashMap<String, Object>();
    public static HashMap<String, Object> extramap = new HashMap<String, Object>();

    public static List<HashMap<String, Object>> dropAddresslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> crideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> frideslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> ridelist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> activepartnerlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> slocationslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> userlocationlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> cardslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> activecardinfo = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> estimatedpricelist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> updatedEstimatedpricelist = new ArrayList<>();
    public static List<HashMap<String, Object>> chatlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> paymentlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> bannerslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> stopLocationslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> fridedriverdetailslist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> fridehistorylist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> cancelAmountlist = new ArrayList<HashMap<String, Object>>();
    public static List<HashMap<String, Object>> updateRideInfo = new ArrayList<HashMap<String, Object>>();

    public static GoogleApiClient mGoogleApiClient, mGoogleApiClient_1, mGoogleApiClient_pickup, mGoogleApiClient_drop;
    public static GoogleApiClient mGoogleApiClient_address, mGoogleApiClient_stopaddress;
    private static Context context;

    public static Context getAppContext() {
        return Global.context;
    }

    public HashMap<String, Object> mapMain() {
        if (mapMain == null) {
            return mapMain = new HashMap<String, Object>();
        } else {
            mapMain.clear();
        }
        return mapMain;
    }

    public HashMap<String, Object> dmapMain() {
        if (dmap == null) {
            return dmap = new HashMap<String, Object>();
        } else {
            dmap.clear();
        }
        return dmap;
    }

    public HashMap<String, Object> eventdata() {
        if (eventdata == null) {
            return eventdata = new HashMap<String, Object>();
        } else {
            eventdata.clear();
        }
        return eventdata;
    }

    public HashMap<String, Object> profile() {
        if (profiledata == null) {
            return profiledata = new HashMap<String, Object>();
        } else {
            profiledata.clear();
        }
        return profiledata;
    }

    public HashMap<String, Object> mapData() {
        if (mapMain == null) {
            return mapData = new HashMap<String, Object>();
        } else {
            mapData.clear();
        }
        return mapData;
    }

    public HashMap<String, Object> tempData() {
        if (tempData == null) {
            return tempData = new HashMap<String, Object>();
        } else {
            tempData.clear();
        }
        return tempData;
    }

    public void setCurrentActivity(Activity CurrentActivity) {
        mCurrentActivity = CurrentActivity;
    }
}
