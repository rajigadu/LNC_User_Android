package com.latenightchauffeurs.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;

import com.latenightchauffeurs.activity.ActivityChat;
import com.latenightchauffeurs.activity.ActivityEditBookingInfo;
import com.latenightchauffeurs.activity.AddAddress;
import com.latenightchauffeurs.activity.AddCard;
import com.latenightchauffeurs.activity.AddTip;
import com.latenightchauffeurs.activity.CardsList;
import com.latenightchauffeurs.activity.Feedback;
import com.latenightchauffeurs.activity.Forgot;
import com.latenightchauffeurs.activity.Navigation;
import com.latenightchauffeurs.activity.Rating;
import com.latenightchauffeurs.activity.SignUp;
import com.latenightchauffeurs.activity.SocialSignUp;
import com.latenightchauffeurs.activity.StopLocationsList;
import com.latenightchauffeurs.activity.UserLocation;
import com.latenightchauffeurs.activity.ViewRideDetails;
import com.latenightchauffeurs.fragment.BookReservation_new;
import com.latenightchauffeurs.fragment.Cards;
import com.latenightchauffeurs.fragment.DashBorad;
import com.latenightchauffeurs.fragment.DropAddressList;
import com.latenightchauffeurs.fragment.Home;
import com.latenightchauffeurs.model.SavePref;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.latenightchauffeurs.fragment.BookReservation_new.bookingAsap;

/**
 * Created by narip on 2/4/2017.
 */
public class JsonPost implements ConstVariable {
    private static final int CREATE_NEW_CARD = 34;
    public static String TAG = "JsonPost";
    static String events;
    private static String mainRespon = "";


    Context activity;



    public static int xxxx ;


//    String mainRespon = "";

    public synchronized static void getNetworkResponse(final Context context, final ProgressBar progressBar,
                                                       final HashMap<String, Object> nameValuePairs, final int mode) {
        Utils.e("JsonPost54", "send events are >>>" + nameValuePairs.get(URL).toString() + nameValuePairs);

        xxxx = mode;


        try {
            boolean isPop = true;

            if (mode == UserLocationUpdate)
                isPop = false;
            if (mode == DeviceTokenUpdate)
                isPop = false;
            if (mode == DriverLocationUpdate)
                isPop = false;
            if (mode == Chat)
                isPop = false;
            if (mode == RideAutoCancel)
                isPop = false;
            if (mode == ChatList)
                isPop = false;
            if (mode == AdsList)
                isPop = false;

            try {
                if (isPop)
                    Utils.initiatePopupWindow(context, "Please wait request is in progress...");
            } catch (Exception e) {
                // Utils.e(TAG+"129","Exception===============Exception=================Exception");
                e.printStackTrace();
            }

            //RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(nameValuePairs)).toString());

            ServiceApi service = ServiceGenerator.createService(ServiceApi.class);

            Call<ResponseBody> response = service.login(nameValuePairs.get(URL).toString(), nameValuePairs);

            nameValuePairs.remove(URL);
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> rawResponse) {
                    try {
                        //get your response....
                        if (Utils.progressDialog != null) {
                            Utils.progressDialog.dismiss();
                            Utils.progressDialog = null;
                        }
                        String result = rawResponse.body().string();
                        Log.e(TAG + "61", "RetroFit2.0 :RetroGetResult: " + result);


                        mainRespon = result;

                        String result1 = rawResponse.body().string();

                        switch (mode) {

                            case ApplyPromo:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.optString("status").equals("1")) {
                                        BookReservation_new.Instance.dismissDialog();
                                    } else {
                                        BookReservation_new.Instance.promoCode = "";
                                    }
                                    Utils.toastTxt(jsonObject.optString("msg"), context);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case Login:
                                // Log.e(TAG+"66", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"68", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    //Settings.ID=Utils.global.profiledata.get(ConstVariable.ID).toString();
                                    //Settings.LATITUDE=Utils.global.profiledata.get(ConstVariable.LATITUDE).toString();
                                    //Settings.LONGITUDE=Utils.global.profiledata.get(ConstVariable.LONGITUDE).toString();

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);

                                    //  pref1.setImage(Utils.global.profiledata.get("photo").toString());
                                    //  pref1.setaddress(Utils.global.profiledata.get("address").toString());
                                    //  pref1.setClocation(Utils.global.profiledata.get("location").toString());
                                    // pref1.setlocationlat(Utils.global.profiledata.get("locationlat").toString());
                                    //  pref1.setlocationlng(Utils.global.profiledata.get("locationlong").toString());

//                                    new Utils(context);
//                                    Utils.toastTxt("Login Successful", Utils.context);
//
//                                    Utils.startActivity(Utils.context, Navigation.class);
//                                    com.latenightchauffeurs.activity.Login.Instance.closeActivity();
//
//                                    com.latenightchauffeurs.activity.Login.Instance.registrationIDtoServer();


//                                   if(Utils.global.mapMain.get("card_status").toString().equalsIgnoreCase("2")){
                                       Log.e(TAG , "XXXXX "+Utils.global.mapData().toString());
                                    Log.e(TAG , "XXXXXVVVVV "+Utils.global.mapMain().toString());
//                                   }else{
//                                       Log.e(TAG , "XXXXX "+Utils.global.mapMain.get("card_status").toString());
//                                   }

                                    Log.e(TAG , "XXXXXVVVVVmainRespon "+mainRespon);


                                   // Log.e(TAG , "XXXXXVVVVVmainRespon "+key);



                                    try{
                                        JSONObject jsonObject = new JSONObject(mainRespon);
//                                        if(jsonObject.has("card_status")){
//                                            String card_status = jsonObject.getString("card_status");
//                                            if(card_status.equalsIgnoreCase("1")){
//                                                Log.e(TAG , "AAAAAAAAAA");
//
                                                if (Utils.global.profiledata.containsKey(ConstVariable.ID) && !Utils.global.profiledata.get(ConstVariable.ID).toString().equalsIgnoreCase(""))
                                                    pref1.setUserId(Utils.global.profiledata.get(ConstVariable.ID).toString());
                                                if (Utils.global.profiledata.containsKey("first_name") && !Utils.global.profiledata.get("first_name").toString().equalsIgnoreCase(""))
                                                    pref1.setUserFName(Utils.global.profiledata.get("first_name").toString());
                                                if (Utils.global.profiledata.containsKey("last_name") && !Utils.global.profiledata.get("last_name").toString().equalsIgnoreCase(""))
                                                    pref1.setUserLName(Utils.global.profiledata.get("last_name").toString());
                                                if (Utils.global.profiledata.containsKey("email_address") && !Utils.global.profiledata.get("email_address").toString().equalsIgnoreCase(""))
                                                    pref1.setEmail(Utils.global.profiledata.get("email_address").toString());
                                                if (Utils.global.profiledata.containsKey(ConstVariable.MOBILE) && !Utils.global.profiledata.get(ConstVariable.MOBILE).toString().equalsIgnoreCase(""))
                                                    pref1.setMobile(Utils.global.profiledata.get(ConstVariable.MOBILE).toString());
                                                if (Utils.global.profiledata.containsKey("profile_pic") && !Utils.global.profiledata.get("profile_pic").toString().equalsIgnoreCase(""))
                                                    pref1.setImage(Utils.global.profiledata.get("profile_pic").toString());


                                                Utils.toastTxt("Login Successful", Utils.context);
                                                new Utils(context);
                                                Utils.startActivity(Utils.context, Navigation.class);
                                                com.latenightchauffeurs.activity.Login.Instance.closeActivity();
                                                com.latenightchauffeurs.activity.Login.Instance.registrationIDtoServer();
//                                            }else{
//                                                Log.e(TAG , "BBBBBBBBBB");
//                                                 Intent intent = new Intent(com.latenightchauffeurs.activity.Login.Instance, AddCard.class);
//                                                intent.putExtra("key", Utils.global.profiledata.get(ConstVariable.ID).toString());
//                                                com.latenightchauffeurs.activity.Login.Instance.startActivityForResult(intent, CREATE_NEW_CARD);
//                                            }
//                                        }
                                    }catch (Exception e){

                                    }






                                } else {
                                    new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.showOkDialog(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case ForgetPassword:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "97","images urls stopsList==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    Forgot.Instance.closeactivity();
                                } else {
                                    new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case Registration:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "146","signup successfull====");
                                    Utils.toastTxt("You registered successfully.", Utils.context);
                                    Utils.startActivity(Utils.context, com.latenightchauffeurs.activity.Login.class);
                                } else {
                                    new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case ChangePassword:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //new Utils(context);
                                    // Utils.e(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.latenightchauffeurs.activity.ChangePassword.Instance.closeactivity();
                                    // Navigation.nid=5;
                                    //Utils.startActivity(Utils.context, com.latenightchauffeurs.activity.Navigation.class);
                                } else {
                                    new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case SocialStatus:
                                Log.e(TAG + "66", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                Log.e(TAG + "68", "response bool====" + result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    //Utils.startActivity(context,FBSignup.class);

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);

                                    if (Utils.global.profiledata.containsKey(ConstVariable.ID) && !Utils.global.profiledata.get(ConstVariable.ID).toString().equalsIgnoreCase(""))
                                        pref1.setUserId(Utils.global.profiledata.get(ConstVariable.ID).toString());

                                    if (Utils.global.profiledata.containsKey("email_address") && !Utils.global.profiledata.get("email_address").toString().equalsIgnoreCase(""))
                                        pref1.setEmail(Utils.global.profiledata.get("email_address").toString());

                                    if (Utils.global.profiledata.containsKey(ConstVariable.MOBILE) && !Utils.global.profiledata.get(ConstVariable.MOBILE).toString().equalsIgnoreCase(""))
                                        pref1.setMobile(Utils.global.profiledata.get(ConstVariable.MOBILE).toString());

                                    if (Utils.global.profiledata.containsKey("first_name") && !Utils.global.profiledata.get("first_name").toString().equalsIgnoreCase(""))
                                        pref1.setUserFName(Utils.global.profiledata.get("first_name").toString());

                                    if (Utils.global.profiledata.containsKey("last_name") && !Utils.global.profiledata.get("last_name").toString().equalsIgnoreCase(""))
                                        pref1.setUserLName(Utils.global.profiledata.get("last_name").toString());

                                    if (Utils.global.profiledata.containsKey("profile_pic") && !Utils.global.profiledata.get("profile_pic").toString().equalsIgnoreCase(""))
                                        pref1.setImage(Utils.global.profiledata.get("profile_pic").toString());

                                    Utils.toastTxt("You are login successfully", Utils.context);
                                    Utils.startActivity(context, Navigation.class);
                                } else {
                                    //Utils.e("" + "73", "login  mapdata===="+Utils.global.couponslist);
                                    // Utils.startActivity(context, com.latenightchauffeurs.activity.SocialSignUp.class);

                                    Intent i = new Intent(context, SocialSignUp.class);
                                    // i.putExtra("type",nameValuePairs.get("type").toString());
                                    context.startActivity(i);

                                    // Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case SocialSignUp:
                                // Log.e(TAG+"66", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"68", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    //Settings.ID=Utils.global.profiledata.get(ConstVariable.ID).toString();
                                    // Settings.LATITUDE=Utils.global.profiledata.get(ConstVariable.LATITUDE).toString();
                                    //Settings.LONGITUDE=Utils.global.profiledata.get(ConstVariable.LONGITUDE).toString();

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    if (Utils.global.profiledata.containsKey(ConstVariable.ID) && !Utils.global.profiledata.get(ConstVariable.ID).toString().equalsIgnoreCase(""))
                                        pref1.setUserId(Utils.global.profiledata.get(ConstVariable.ID).toString());

                                    if (Utils.global.profiledata.containsKey("email_address") && !Utils.global.profiledata.get("email_address").toString().equalsIgnoreCase(""))
                                        pref1.setEmail(Utils.global.profiledata.get("email_address").toString());

                                    if (Utils.global.profiledata.containsKey(ConstVariable.MOBILE) && !Utils.global.profiledata.get(ConstVariable.MOBILE).toString().equalsIgnoreCase(""))
                                        pref1.setMobile(Utils.global.profiledata.get(ConstVariable.MOBILE).toString());

                                    if (Utils.global.profiledata.containsKey("first_name") && !Utils.global.profiledata.get("first_name").toString().equalsIgnoreCase(""))
                                        pref1.setUserFName(Utils.global.profiledata.get("first_name").toString());

                                    if (Utils.global.profiledata.containsKey("last_name") && !Utils.global.profiledata.get("last_name").toString().equalsIgnoreCase(""))
                                        pref1.setUserLName(Utils.global.profiledata.get("last_name").toString());

                                    if (Utils.global.profiledata.containsKey("profile_pic") && !Utils.global.profiledata.get("profile_pic").toString().equalsIgnoreCase(""))
                                        pref1.setImage(Utils.global.profiledata.get("profile_pic").toString());

                                    Utils.toastTxt("You are SignUp successfully", Utils.context);
                                    com.latenightchauffeurs.activity.Login.Instance.registrationIDtoServer();
                                    Utils.startActivity(context, Navigation.class);
                                } else {
                                    new Utils(context);
                                    Log.e("119", "response" + result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case AddDropAddress:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //new Utils(context);
                                    // Utils.e(TAG + "97","images urls stopsList==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");

                                    if (nameValuePairs.containsKey("type") && nameValuePairs.get("type").toString().equalsIgnoreCase("1")) {
                                        AddAddress.Instance.closeActivity();
                                        com.latenightchauffeurs.activity.DropAddressList.Instance.dropAddressRequest();
                                    } else if (nameValuePairs.containsKey("type") && nameValuePairs.get("type").toString().equalsIgnoreCase("2")) {
                                        Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                        Navigation.nid = 4;
                                        Utils.startActivity(context, Navigation.class);
                                    }
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case DropOffAddressList:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.e("" + "73", "login  mapdata===="+  Utils.global.dropAddresslist);

                                    if (nameValuePairs.containsKey("type") && nameValuePairs.get("type").toString().equalsIgnoreCase("1")) {
                                        // BookReservation.Instance.updateDropAddressList(Utils.global.dropAddresslist);
                                        com.latenightchauffeurs.activity.DropAddressList.loadRequestsList(context, Utils.global.dropAddresslist, "mode");
                                    } else if (nameValuePairs.containsKey("type") && nameValuePairs.get("type").toString().equalsIgnoreCase("2")) {
                                        List<HashMap<String, Object>> list1 = new ArrayList<>();
                                        HashMap<String, Object> dmap;

                                        for (int i = 0; i < Utils.global.dropAddresslist.size(); i++) {
                                            dmap = new HashMap<>();

                                            for (Map.Entry<String, Object> entry : Utils.global.dropAddresslist.get(i).entrySet()) {
                                                String key = entry.getKey();
                                                Object value = entry.getValue();

                                                if (!value.toString().equalsIgnoreCase("null")) {
                                                    dmap.put(key, value);
                                                }
                                            }
                                            list1.add(dmap);
                                        }
                                        DropAddressList.loadRequestsList(context, list1, "mode");
                                    }
                                } else {
                                    Utils.global.dropAddresslist.clear();
                                    DropAddressList.loadRequestsList(context, Utils.global.dropAddresslist, "mode");
                                    new Utils(context);
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case BookReseration:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "97","images urls stopsList==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    // Forgot.Instance.closeactivity();
                                    if (Utils.global.mapMain.get(ConstVariable.MESSAGE).toString().equalsIgnoreCase("Looking for driver...")) {
                                        BookReservation_new.getBookingResponce();
                                        Home.Instance.updateRideStatus(Utils.global.mapMain);
                                    } else {

                                        bookingAsap = true;
                                        Log.e(TAG , "111111111111");
                                        BookReservation_new.Instance.showBookingSuccessDialog
                                                (Utils.global.mapMain.get(ConstVariable.MESSAGE).toString());
                                    }

                                } else {
                                    new Utils(context);
                                    bookingAsap = false;
                                    // Log.e("119","response"+result.toString());
                                    //Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);

                                    Log.e(TAG , "showBookingSuccessDialog");
                                    Log.e(TAG , "2222222222222222");
                                    BookReservation_new.Instance.showBookingSuccessDialog
                                            (Utils.global.mapMain.get(ConstVariable.MESSAGE).toString());
                                }
                                break;

                            case UpdateBookingReservation:
                                //result = JsonHelper.getResults(result.toString(), context, mode);
                                try {
                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    Utils.toastTxt(jsonObject.optString(ConstVariable.MESSAGE), context);
                                    ((Activity) context).finish();
                                    DashBorad.IS_UPDATED_RIDE = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case ContactUs:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "97","images urls stopsList==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    // Forgot.Instance.closeactivity();
                                } else {
                                    new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case CurrentRides:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    HashMap<String, Object> dmap = new HashMap<>();
                                    for (Map.Entry<String, Object> entry : Utils.global.crideslist.get(0).entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();

                                        if (!value.toString().equalsIgnoreCase("null")) {
                                            dmap.put(key, value);
                                        }
                                    }

                                    Utils.e("" + "73", "login  mapdata====" + Utils.global.crideslist);
                                    DashBorad.Instance.loadRequestsList(context, Utils.global.crideslist, "mode");
                                }else {
                                    new Utils(context);
                                    Utils.toastTxt(Utils.global.mapMain.get("status").toString(), context);
                                }
                                break;
                            case FutureRides:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    HashMap<String, Object> dmap = new HashMap<>();
                                    for (Map.Entry<String, Object> entry : Utils.global.frideslist.get(0).entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();

                                        if (!value.toString().equalsIgnoreCase("null")) {
                                            dmap.put(key, value);
                                        }
                                    }

                                  /*if(Utils.global.frideslist.get(1)!=null)
                                    {
                                        if(Utils.global.frideslist.get(1).containsKey("estimate_price"))
                                        {
                                            if(!Utils.global.frideslist.get(1).get("estimate_price").toString().equalsIgnoreCase(""))
                                            {
                                                dmap.put("estimate_price",Utils.global.frideslist.get(1).get("estimate_price").toString());
                                                Utils.global.frideslist.remove(1);
                                            }
                                        }
                                    }

                                    if (Utils.global.frideslist.get(1)!=null)
                                    {
                                        if(Utils.global.frideslist.get(1).containsKey("driver_rating"))
                                        {
                                            if(!Utils.global.frideslist.get(1).get("driver_rating").toString().equalsIgnoreCase(""))
                                            {
                                                dmap.put("driver_rating",Utils.global.frideslist.get(1).get("driver_rating").toString());
                                                Utils.global.frideslist.remove(1);
                                            }
                                        }
                                    }*/

                                    Utils.e("" + "73", "login  mapdata====" + Utils.global.frideslist);
                                    com.latenightchauffeurs.fragment.FutureRides.Instance.loadRequestsList(context, Utils.global.frideslist, "mode");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case CancelRide:
                                Log.e(TAG + "245", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                Log.e(TAG + "247", "response bool====" + result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    com.latenightchauffeurs.activity.CancelRide.Instance.closeActivity();
                                    Navigation.nid = 0;
                                    Utils.startActivity(context, Navigation.class);

                                    /*Intent broadcast = new Intent();
                                    broadcast.putExtra("isid", "12");
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("status", "12");
                                    broadcast.putExtra("data","");*/
                                } else {
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case CancelAmount:
                                /*Log.e(TAG + "245", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);*/

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    //if (jsonObject.optString("status").equals("1")) {
                                        com.latenightchauffeurs.activity.CancelFutureRide.Instance.showMessage(context,
                                                jsonObject.optString("status"), jsonObject.optString("amount"));
                                    /*}
                                    else {
                                        //BookReservation_new.Instance.promoCode = "";
                                    }*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.e(TAG + "247", "response bool====" + result.toString());
                                /*if (result.equalsIgnoreCase(SUCCESS)) {
                                    HashMap<String, Object> dmap = new HashMap<>();
                                    for (Map.Entry<String, Object> entry : Utils.global.cancelAmountlist.get(0).entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();

                                        if (!value.toString().equalsIgnoreCase("null")) {
                                            dmap.put(key, value);
                                        }
                                    }
                                    Utils.e("" + "73", "login  mapdata====" + Utils.global.cancelAmountlist);

                                    if (Utils.global.cancelAmountlist.size() > 0)

                                } else {
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }*/
                                break;
                            case CancelFutureRide:
                                Log.e(TAG + "245", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                Log.e(TAG + "247", "response bool====" + result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    Navigation.nid = 1;
                                    Utils.startActivity(context, Navigation.class);

                                    /*Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "12");
                                    broadcast.putExtra("status", "12");
                                    broadcast.putExtra("data","");*/
                                } else {
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case DriverLocationUpdate:
                                Log.e(TAG + "245", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                Log.e(TAG + "247", "response bool====" + result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //  Utils.e(TAG + "250","browse members mapdata==== " + Utils.global.browseMembersList);
                                    Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "7");
                                    broadcast.putExtra("status", "7");
                                    broadcast.putExtra("data", "");

                                    if (Utils.global.mapMain.containsKey("latitude") && !Utils.global.mapMain.get("latitude").toString().equalsIgnoreCase(""))
                                        broadcast.putExtra("lat", Utils.global.mapMain.get("latitude").toString());
                                    if (Utils.global.mapMain.containsKey("longitude") && !Utils.global.mapMain.get("longitude").toString().equalsIgnoreCase(""))
                                        broadcast.putExtra("lon", Utils.global.mapMain.get("longitude").toString());
                                    if (Utils.global.mapMain.containsKey("eta_time") && !Utils.global.mapMain.get("eta_time").toString().equalsIgnoreCase("null"))
                                        broadcast.putExtra("eta_time", Utils.global.mapMain.get("eta_time").toString());
                                    if (Utils.global.mapMain.containsKey("eta_distance") && !Utils.global.mapMain.get("eta_distance").toString().equalsIgnoreCase("null"))
                                        broadcast.putExtra("eta_distance", Utils.global.mapMain.get("eta_distance").toString());
                                    if (Utils.global.mapMain.containsKey("start_ride_status") && !Utils.global.mapMain.get("start_ride_status").toString().equalsIgnoreCase(""))
                                        broadcast.putExtra("start_ride_status", Utils.global.mapMain.get("start_ride_status").toString());

                                    context.sendBroadcast(broadcast);
                                } else {
                                    // Log.e("119","response"+result.toString());
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case ActivePartner:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.e("" + "73", "login  mapdata====" + Utils.global.activepartnerlist);
                                    //DashBorad.Instance.loadRequestsList(context,Utils.global.activepartnerlist,"mode");

                                    Home.Instance.showPartnerDetailsDilog(Utils.global.activepartnerlist.get(0));
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case CCRide:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    HashMap<String, Object> dmap = new HashMap<>();
                                    for (Map.Entry<String, Object> entry : Utils.global.crideslist.get(0).entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();

                                        if (!value.toString().equalsIgnoreCase("null")) {
                                            dmap.put(key, value);
                                        }
                                    }

                                    if (Utils.global.crideslist.get(1) != null) {
                                        if (Utils.global.crideslist.get(1).containsKey("estimate_price")) {
                                            if (!Utils.global.crideslist.get(1).get("estimate_price").toString().
                                                    equalsIgnoreCase("")) {
                                                dmap.put("estimate_price", Utils.global.crideslist.get(1).
                                                        get("estimate_price").toString());
                                                Utils.global.crideslist.remove(1);
                                            }
                                        }
                                    }

                                    if (Utils.global.crideslist.get(1) != null) {
                                        if (Utils.global.crideslist.get(1).containsKey("driver_rating")) {
                                            if (!Utils.global.crideslist.get(1).get("driver_rating").toString().
                                                    equalsIgnoreCase("")) {
                                                dmap.put("driver_rating", Utils.global.crideslist.get(1).
                                                        get("driver_rating").toString());
                                                Utils.global.crideslist.remove(1);
                                            }
                                        }
                                    }

                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "5");
                                    broadcast.putExtra("status", "5");
                                    broadcast.putExtra("data", (Serializable)dmap);
                                    context.sendBroadcast(broadcast);*/
                                    Home.Instance.getCurrentRideDetails(dmap, "1");
                                } else {
                                    //new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                   /* Intent broadcast = new Intent();
                                    broadcast.setAction("OPEN_NEW_ACTIVITY");
                                    broadcast.putExtra("isid", "6");
                                    broadcast.putExtra("status", "6");
                                    broadcast.putExtra("data", "");
                                    context.sendBroadcast(broadcast);*/
                                    Home.Instance.getCurrentRideDetails(Utils.global.mapMain, "2");
                                }
                                break;
                            case RideHistory:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.ridelist);
                                    com.latenightchauffeurs.fragment.RideHistory.loadRequestsList(context, Utils.global.ridelist, "mode");
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Utils.global.mapMain.get("status").toString(), context);
                                }
                                break;
                            case FutureRideHistory:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.ridelist);
                                    com.latenightchauffeurs.fragment.FutureRideHistory.loadRequestsList(context, Utils.global.fridehistorylist, "mode");
                                } else {
                                    // new Utils(context);
                                    //Utils.toastTxt( Utils.global.mapMain.get("status").toString(),context);
                                }
                                break;
                            case ServiceInfo:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.slocationslist);

                                    if (nameValuePairs.get("type").toString().equalsIgnoreCase("1")) {
                                        SignUp.populatecities(Utils.global.slocationslist);
                                    } else if (nameValuePairs.get("type").toString().equalsIgnoreCase("2")) {
                                        com.latenightchauffeurs.fragment.ServiceInfo.loadRequestsList(context, Utils.global.slocationslist, "mode");
                                    } else if (nameValuePairs.get("type").toString().equalsIgnoreCase("3")) {
                                        UserLocation.loadRequestsList(context, Utils.global.slocationslist, "mode");
                                    } else if (nameValuePairs.get("type").toString().equalsIgnoreCase("4")) {
                                        com.latenightchauffeurs.activity.SocialSignUp.populatecities(Utils.global.slocationslist);
                                    }
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case UserLocationinfoUpdate:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.userlocationlist);

                                    if (nameValuePairs.get("type").toString().equalsIgnoreCase("1")) {
                                        SavePref pref1 = new SavePref();
                                        pref1.SavePref(context);
                                        pref1.setClocation(Utils.global.userlocationlist.get(1).get("location").toString());
                                        pref1.setlocationlat(Utils.global.userlocationlist.get(1).get("locationlat").toString());
                                        pref1.setlocationlng(Utils.global.userlocationlist.get(1).get("locationlong").toString());
                                        Utils.startActivity(context, Navigation.class);

                                        //SignUp.populatecities(Utils.global.slocationslist);
                                    } else if (nameValuePairs.get("type").toString().equalsIgnoreCase("2")) {
                                        SavePref pref1 = new SavePref();
                                        pref1.SavePref(context);
                                        pref1.setClocation(Utils.global.userlocationlist.get(1).get("location").toString());
                                        pref1.setlocationlat(Utils.global.userlocationlist.get(1).get("locationlat").toString());
                                        pref1.setlocationlng(Utils.global.userlocationlist.get(1).get("locationlong").toString());
                                        com.latenightchauffeurs.fragment.ServiceInfo.Instance.DataFromoneTotwo();
                                        com.latenightchauffeurs.fragment.ServiceInfo.loadRequestsList(context, Utils.global.userlocationlist, "mode");
                                    }
                                } else {
                                    new Utils(context);
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case AddCreditCard:
                                // Log.e(TAG+"144", "response" + result.toString());

//                                Log.e(TAG , "result11" +
//                                        " "+result1);


                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());




                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //new Utils(context);
                                    // Utils.e(TAG + "97","images urls stopsList==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");




                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    Cards.Instance.CreditCardListRequest();

                                    // Forgot.Instance.closeactivity();
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case AddCreditCard1:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //new Utils(context);
                                    // Utils.e(TAG + "97","images urls stopsList==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");

                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                    CardsList.MyrequestsListRequest();

                                    // Forgot.Instance.closeactivity();
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case CreditCardsList:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.ridelist);
                                    Cards.Instance.loadRequestsList(context, Utils.global.cardslist, "mode");
                                } else {
                                    Utils.global.cardslist.clear();
                                    Cards.Instance.loadRequestsList(context, Utils.global.cardslist, "mode");

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setCardId("");
                                    pref1.setCardNumber("");
                                    pref1.setCardExMonth("");
                                    pref1.setCardExYear("");

                                    // new Utils(context);
                                    // Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case CreditCardsList1:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.ridelist);
                                    CardsList.loadRequestsList(context, Utils.global.cardslist, "mode");
                                } else {
                                    Utils.global.cardslist.clear();
                                    CardsList.loadRequestsList(context, Utils.global.cardslist, "mode");

                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setCardId("");
                                    pref1.setCardNumber("");
                                    pref1.setCardExMonth("");
                                    pref1.setCardExYear("");

                                    // new Utils(context);
                                    // Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;
                            case DeleteCard:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //new Utils(context);
                                    // Utils.e(TAG + "97","images urls stopsList==== " + Utils.global.getImageUrlsList());
                                    // DashBoard.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    // Members.loadDataHotelsList(Utils.context,Utils.global.getImageUrlsList(),"local");
                                    Utils.toastTxt(Utils.global.mapMain.get("msg").toString(), context);
                                    Cards.Instance.CreditCardListRequest();
                                    // Forgot.Instance.closeactivity();
                                } else {

                                    Utils.toastTxt(Utils.global.mapMain.get("msg").toString(), context);
                                    Cards.Instance.CreditCardListRequest();

                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    // Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;


                            case DeleteCard2:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {

                                    Utils.toastTxt(Utils.global.mapMain.get("msg").toString(), context);
                                    BookReservation_new.Instance.cardList();
                                } else {

                                    Utils.toastTxt(Utils.global.mapMain.get("msg").toString(), context);
                                    BookReservation_new.Instance.cardList();

                                }
                                break;


                            case EstimatedPrice:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.ridelist);

                                    if (Utils.global.estimatedpricelist.size() > 0)
                                        BookReservation_new.Instance.showEstimatedPriceDialog(Utils.global.estimatedpricelist.get(0));
                                } else {
                                    // new Utils(context);
                                    // Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                }
                                break;

                            case UpdatedEstimatedPrice:
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    // Utils.e("" + "73", "login  mapdata===="+  Utils.global.ridelist);

                                    if (Utils.global.updatedEstimatedpricelist.size() > 0)
                                        ActivityEditBookingInfo.appCompatActivity.showUpdatedPriceDialog(Utils.global.updatedEstimatedpricelist.get(0));
                                }
                                break;

                            case ChatList:
                                // Log.e(TAG+"106", "response" + result.toString());
                                if (!result.toString().equalsIgnoreCase("") && !result.toString().equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result.toString(), context, mode);
                                else
                                    result = "faillure";
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.e("" + "73", "login  mapdata====" + Utils.global.chatlist);
                                    ActivityChat.instance.loadRequestsList(context, Utils.global.chatlist, "mode");
                                } else {
                                    Utils.global.chatlist.clear();
                                    ActivityChat.instance.loadRequestsList(context, Utils.global.chatlist, "mode");
                                }
                                break;
                            case Chat:
                                // Log.e(TAG+"106", "response" + result.toString());
                                if (!result.toString().equalsIgnoreCase("") && !result.toString().equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result.toString(), context, mode);
                                else
                                    result = "faillure";
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.e("" + "73", "login  mapdata===="+  Utils.global.chatlist);
                                    ActivityChat.instance.fetchMsgsList();
                                } else {
                                    Utils.global.chatlist.clear();
                                    //ActivityChat.instance.loadRequestsList(context, Utils.global.chatlist, "mode");
                                }
                                break;
                            case UserRating:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "146","signup successfull====");

                                    Log.e(TAG, "SSSSSSSSSS "+Utils.global.mapMain.toString());

                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), Utils.context);
                                    Rating.Instance.closeActivity();
                                    //Utils.startActivity(Utils.context, com.qwikride.activity.Login.class);
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Log.e(TAG, "AAAAAAAAAAaA "+Utils.global.mapMain.toString());

                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), context);
                                }
                                break;


                            case UserRating1:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), Utils.context);
                                    Feedback.Instance.closeActivity();
                                    //Utils.startActivity(Utils.context, com.qwikride.activity.Login.class);
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), context);
                                }
                                break;

                            case UserRating2:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), Utils.context);
                                    AddTip.Instance.closeActivity();
                                    //Utils.startActivity(Utils.context, com.qwikride.activity.Login.class);
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), context);
                                }
                                break;


                            case PaymentSummary:
                                // Log.e(TAG+"106", "response" + result.toString());
                                if (!result.toString().equalsIgnoreCase("") && !result.toString().equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result.toString(), context, mode);



                                else
                                    result = "faillure";
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    if (Utils.global.paymentlist.size() > 0)

//                                        "base_price": 70,
//                                            "total_fare": 162

                                        try{
                                            JSONObject jsonObject = new JSONObject(mainRespon);

                                            String status = jsonObject.getString("status");
                                            if(status.equalsIgnoreCase("1")){
                                                String base_price = jsonObject.getString("base_price");
                                                String total_fare = jsonObject.getString("total_fare");

                                                Utils.global.paymentlist.get(0).put("base_price" , ""+base_price);
                                                Utils.global.paymentlist.get(0).put("total_fare" , ""+total_fare);

                                                com.latenightchauffeurs.fragment.RideHistory.Instance.showPaymentSummaryDilog(Utils.global.paymentlist.get(0));


                                            }

                                        }catch (Exception e){

                                        }




                                } else {

                                }
                                break;
                            case AdsList:
                                Log.e(TAG + "245", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                Log.e(TAG + "247", "response bool====" + result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Navigation.loaddata(Utils.global.bannerslist);
                                    Navigation.assignServiceTime(Utils.global.extramap);
                                    //  Utils.e(TAG + "250","browse members mapdata==== " + Utils.global.browseMembersList);
                                } else {
                                    // Log.e("119","response"+result.toString());
                                    // Utils.showOkDialog( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    Navigation.loaddata(null);
                                    Navigation.assignServiceTime(null);
                                }
                                break;
                            case DeleteDropAddress:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), Utils.context);
                                    DropAddressList.Instance.MyrequestsListRequest();
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), context);
                                }
                                break;
                            case EditDropAddress:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    new Utils(context);
                                    // Utils.e(TAG + "146","signup successfull====");
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), Utils.context);
                                    DropAddressList.Instance.MyrequestsListRequest();
                                } else {
                                    // new Utils(context);
                                    // Log.e("119","response"+result.toString());
                                    Utils.toastTxt(Utils.global.mapMain.get("message").toString(), context);
                                }
                                break;
                            case StopLocations:
                                // Log.e(TAG+"106", "response" + result.toString());
                                if (!result.toString().equalsIgnoreCase("") && !result.toString().equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result.toString(), context, mode);
                                else
                                    result = "faillure";
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.e("" + "73", "login  mapdata====" + Utils.global.stopLocationslist);
                                    StopLocationsList.loadRequestsList(context, Utils.global.stopLocationslist, "mode");
                                } else {

                                }
                                break;
                            case PaymentHistory:
                                // Log.e(TAG+"106", "response" + result.toString());
                                if (!result.toString().equalsIgnoreCase("") && !result.toString().equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result.toString(), context, mode);
                                else
                                    result = "faillure";
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.e("" + "73", "login  mapdata====" + Utils.global.paymentlist);
                                    com.latenightchauffeurs.fragment.PaymentHistory.loadRequestsList(context, Utils.global.paymentlist, "mode");
                                } else {

                                }
                                break;
                            case RideAutoCancel:
                                // Log.e(TAG+"106", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setautoRideId("");
                                    //new Utils(context);
                                    //Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),Utils.context);
                                    Home.Instance.updateRideStatus(Utils.global.mapMain);
                                } else {
                                    SavePref pref1 = new SavePref();
                                    pref1.SavePref(context);
                                    pref1.setautoRideId("");
                                    new Utils(context);
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;
                            case FutureDriverDetails:
                                // Log.e(TAG+"106", "response" + result.toString());
                                if (!result.toString().equalsIgnoreCase("") && !result.toString().equalsIgnoreCase("null"))
                                    result = JsonHelper.getResults(result.toString(), context, mode);
                                else
                                    result = "faillure";
                                // Log.e(TAG+"193", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    Utils.e("" + "73", "login mapdata====" + Utils.global.fridedriverdetailslist);
                                    ViewRideDetails.Instance.getFutureRideDriverDetails(Utils.global.fridedriverdetailslist);
                                } else {
                                    ViewRideDetails.Instance.getFutureRideDriverDetails(null);
                                }
                                break;
                            case Logout:
                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    Navigation.Instance.afterLogoutStatus();
                                } else {
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;

                            case EDITRIDE:

                                Log.e(TAG+"144", "response" + result.toString());

                                Navigation.Instance.afterLogoutStatus();

//                                // Log.e(TAG+"144", "response" + result.toString());
                                result = JsonHelper.getResults(result.toString(), context, mode);
                                // Log.e(TAG+"146", "response bool===="+result.toString());
                                if (result.equalsIgnoreCase(SUCCESS)) {
                                    //Utils.toastTxt( Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(),context);
                                    //Navigation.Instance.afterLogoutStatus();

                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE+"fsdf").toString(), context);

                                } else {
                                    Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                }
                                break;



                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    if (Utils.progressDialog != null) {
                        Utils.progressDialog.dismiss();
                        Utils.progressDialog = null;
                    }
                    /*if(progressBar != null)
                    {
                        progressBar.setVisibility(View.GONE);
                    }*/
                    // other stuff...
                    Utils.e(TAG+"98","Exception==================Exception===================Exception"+throwable.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //Utils.e(TAG+"104","Exception==================Exception===================Exception");
        } finally {

        }
    }

}
