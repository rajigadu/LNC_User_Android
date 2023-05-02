package com.latenightchauffeurs.Utils;

import android.content.Context;
import android.text.format.DateUtils;

import com.latenightchauffeurs.model.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by narip on 2/4/2017.
 */
public class JsonHelper implements ConstVariable {
    public static final String Response_Code = "status";
    public static final String Response_Text = "ResponseText";
    public static final String DATA = "data";
    public static final String ERROR_MSG = "errmsg";
    static String Tag = "jsonHelper";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static String dateFinal;
    public static int flags = DateUtils.FORMAT_ABBREV_RELATIVE;

    public static String getResults(String result, Context context, final int mode) {
        String response = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;

        if (result == null)
            return null;
        if (context == null)
            context = Global.getAppContext();
        try {
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SavePref preferences = new SavePref();
            preferences.SavePref(context);
            if (jsonObject.has("dbh_ride_cost_per_hour")) {
                preferences.setDhRideCostPerHour(jsonObject.getString("dbh_ride_cost_per_hour"));
            }

           /* int key = 0;
             key = jsonObject.getInt(Response_Code);
            key=1;

            if (Utils.global == null)
                Utils.global = new Global();

            Utils.e(Tag+"55", key+""+mode);*/

            int key = 0;
            try {
                key = jsonObject.getInt(Response_Code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Utils.global == null)
                Utils.global = new Global();
            // Utils.e(Tag+"48", key+""+mode);

            switch (key) {
                case 0:
                    //response = jsonObject.optString(Response_Text);
                    response = FAILURE;

                    jsonArray = jsonObject.optJSONArray(DATA);
                    Utils.global.mapMain = Utils.GetJsonDataIntoMap(context, jsonArray, "");

                    break;
                case 1:
                    response = ConstVariable.SUCCESS;
                    jsonArray = jsonObject.optJSONArray(DATA);

                    Utils.global.mapMain.clear();
                    Utils.global.mapMain = Utils.GetJsonDataIntoMap(context, jsonArray, "");

                    if (mode == ConstVariable.DriverLocationUpdate) {
                        Utils.global.mapMain.put("eta_time", jsonObject.get("eta_time").toString());
                        Utils.global.mapMain.put("eta_distance", jsonObject.get("eta_distance").toString());
                        Utils.global.mapMain.put("start_ride_status", jsonObject.get("start_ride_status").toString());
                    }

                    if (mode == ConstVariable.AdsList) {
                        Utils.global.extramap.put("time_left", jsonObject.get("time_left").toString());
                    }

                    if (mode == ConstVariable.Login) {
                        // Utils.e(Tag + "69", "login  array==== " + jsonArray);

                        Utils.global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");

//                        Utils.global.profiledata.put("card_status" , "jkk");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));
//                         Utils.e(Tag + "73", "loginXXXXXXXXXX  mapdata===="+  Utils.global.);
                    }

                    if (mode == ConstVariable.SocialSignUp) {
                        // Utils.e(Tag + "69", "login  array==== " + jsonArray);

                        Utils.global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        // Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.SocialStatus) {
                        // Utils.e(Tag + "69", "login  array==== " + jsonArray);

                        Utils.global.profiledata = Utils.GetJsonDataIntoMap(context, jsonArray, "");
                        //Utils.global.addAllImageUrlsList((ArrayList<HashMap<String,Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.mapMain);
                    }
                    if (mode == ConstVariable.DropOffAddressList) {
                        Utils.global.dropAddresslist.clear();
                        Utils.global.dropAddresslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "73", "login  mapdata====" + Utils.global.dropAddresslist);
                    }
                    if (mode == ConstVariable.CurrentRides) {
                        Utils.global.crideslist.clear();
                        Utils.global.crideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "73", "login  mapdata====" + Utils.global.crideslist);
                    }
                    if (mode == ConstVariable.ActivePartner) {
                        Utils.global.activepartnerlist.clear();
                        Utils.global.activepartnerlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "73", "login  mapdata====" + Utils.global.activepartnerlist);
                    }
                    if (mode == ConstVariable.CCRide) {
                        Utils.global.crideslist.clear();
                        Utils.global.crideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.cridelist);
                    }
                    if (mode == ConstVariable.RideHistory) {
                        Utils.global.ridelist.clear();
                        Utils.global.ridelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.ridelist);
                    }
                    if (mode == ConstVariable.ServiceInfo) {
                        Utils.global.slocationslist.clear();
                        Utils.global.slocationslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        // Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.slocationslist);
                    }
                    if (mode == ConstVariable.UserLocationinfoUpdate) {
                        Utils.global.userlocationlist.clear();
                        Utils.global.userlocationlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.userlocationlist);
                    }
                    if (mode == ConstVariable.CreditCardsList || mode == ConstVariable.CreditCardsList1) {
                        Utils.global.cardslist.clear();
                        Utils.global.cardslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.userlocationlist);
                    }
                    if (mode == ConstVariable.ActiveCardInfo) {
                        Utils.global.activecardinfo.clear();
                        Utils.global.activecardinfo.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.userlocationlist);
                    }
                    if (mode == ConstVariable.EstimatedPrice) {
                        Utils.global.estimatedpricelist.clear();
                        Utils.global.estimatedpricelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.userlocationlist);
                    }
                    if (mode == ConstVariable.UpdatedEstimatedPrice) {
                        Utils.global.updatedEstimatedpricelist.clear();
                        Utils.global.updatedEstimatedpricelist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.userlocationlist);
                    }

                    if (mode == ConstVariable.ChatList) {
                        Utils.global.chatlist.clear();
                        Utils.global.chatlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.userlocationlist);
                    }
                    if (mode == ConstVariable.PaymentSummary) {
                        Utils.global.paymentlist.clear();
                        Utils.global.paymentlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        //Utils.e(Tag + "73", "login  mapdata===="+  Utils.global.userlocationlist);
                    }
                    if (mode == ConstVariable.AdsList) {
                        Utils.global.bannerslist.clear();
                        Utils.global.bannerslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "148", "array data2 ==== " + Utils.global.bannerslist);
                    }
                    if (mode == ConstVariable.StopLocations) {
                        Utils.global.stopLocationslist.clear();
                        Utils.global.stopLocationslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "148", "array data2 ==== " + Utils.global.stopLocationslist);
                    }
                    if (mode == ConstVariable.PaymentHistory) {
                        Utils.global.paymentlist.clear();
                        Utils.global.paymentlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "148", "array data2 ==== " + Utils.global.paymentlist);
                    }
                    if (mode == ConstVariable.FutureRides) {
                        Utils.global.frideslist.clear();
                        Utils.global.frideslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "148", "array data2 ==== " + Utils.global.frideslist);
                    }
                    if (mode == ConstVariable.FutureDriverDetails) {
                        Utils.global.fridedriverdetailslist.clear();
                        Utils.global.fridedriverdetailslist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));
                        Utils.global.fridedriverdetailslist.get(0).put("rating", jsonObject.getString("driver_rating"));

                        Utils.e(Tag + "148", "array data2 ==== " + Utils.global.fridedriverdetailslist);
                    }
                    if (mode == ConstVariable.FutureRideHistory) {
                        Utils.global.fridehistorylist.clear();
                        Utils.global.fridehistorylist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));
                        Utils.global.fridehistorylist.get(0).put("rating", jsonObject.getString("driver_rating"));

                        Utils.e(Tag + "148", "array data2 ==== " + Utils.global.fridehistorylist);
                    }
                    if (mode == ConstVariable.CancelAmount) {
                        Utils.global.cancelAmountlist.clear();
                        Utils.global.cancelAmountlist.addAll((ArrayList<HashMap<String, Object>>) (Utils.GetJsonDataIntoList(context, jsonArray, "")));

                        Utils.e(Tag + "148", "array data2 ==== " + Utils.global.cancelAmountlist);
                    }

//                    if (mode == ConstVariable.DeleteCard) {
//
//                        Utils.e(Tag + "148", "array data2mapMain111 ==== " + jsonArray);
//
//                        Utils.global.mapMain.clear();
//                        Utils.global.mapMain = Utils.GetJsonDataIntoMap(context, jsonArray, "");
//
//
//                        Utils.e(Tag + "148", "array data2mapMain ==== " + Utils.global.mapMain);
//                    }


            }
        } catch (Exception e) {
            // TODO: handle exception
            Utils.e(Tag + "79", "Exception=================Exception====================Exception" + e.toString());
            e.printStackTrace();
        } finally {
            jsonArray = null;
            jsonObject = null;
        }
        return response;
    }
}
