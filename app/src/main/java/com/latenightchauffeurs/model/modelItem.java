package com.latenightchauffeurs.model;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by AnaadIT on 3/11/2017.
 */

public class
modelItem implements Serializable {
    private HashMap<String, Object> mapMain = new HashMap<String, Object>();


    public modelItem(HashMap<String, Object> map) {
        try {
            this.mapMain = map;
            // this.subList = (List<HashMap<String, Object>>) map.get("name");
        } catch (Exception e) {
            Log.e("TripData 36", "Exception===========Exception==========Exception");
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getMapMain() {
        return mapMain;
    }

    public void setMapMain(HashMap<String, Object> mapMain) {
        this.mapMain = mapMain;
    }

}
