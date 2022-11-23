package com.latenightchauffeurs.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.latenightchauffeurs.model.SavePref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by narayana on 3/27/2018.
 */

public class OnlineRequest {
    public static void loginRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("emailid", map.get("emailid").toString());
        Utils.global.mapMain.put(ConstVariable.PASSWORD, map.get("password").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_LOGIN);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.Login);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void signupRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.FULLNAME, map.get("fname").toString());
        Utils.global.mapMain.put(ConstVariable.LASTNAME, map.get("lname").toString());
        Utils.global.mapMain.put(ConstVariable.EMAIL, map.get("email").toString());
        Utils.global.mapMain.put(ConstVariable.ADDRESS, map.get("address").toString());
        Utils.global.mapMain.put(ConstVariable.MOBILE, map.get("mobile").toString());
        Utils.global.mapMain.put(ConstVariable.PASSWORD, map.get("password").toString());
        // Utils.global.mapMain.put("card_token",map.get("card_token").toString());

        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_REGISTRATION);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.Registration);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void changePawwordRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.USERID, id);
        Utils.global.mapMain.put("oldpassword", map.get("old_password").toString());
        Utils.global.mapMain.put("newpassword", map.get("new_password").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_CHANGE_PASSWORD);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.ChangePassword);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void contactusRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.ID, id);
        Utils.global.mapMain.put("fullname", map.get("fullname").toString());
        Utils.global.mapMain.put(ConstVariable.EMAIL, map.get(ConstVariable.EMAIL).toString());
        Utils.global.mapMain.put("message", map.get("message").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_CONTACT_US);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.ContactUs);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }


    }

    public static void editProfileRequest(Context mcontext, Uri uri, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.USERID, id);
        Utils.global.mapMain.put("fname", map.get("fname").toString());
        Utils.global.mapMain.put("mobile", map.get("mobile").toString());
        Utils.global.mapMain.put("lname", map.get("lname").toString());
        Utils.global.mapMain.put("profilepic", map.get("profilepic"));
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_EDIT_PROFILE);

        if (Utils.isNetworkAvailable(mcontext)) {
            Utils.uploadImage(mcontext, uri, Utils.global.mapMain, ConstVariable.EditProfile);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void forgotRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.ID, id);
        Utils.global.mapMain.put("email", map.get("email").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_FORGET_PASSWORD);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.ForgetPassword);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void socialSignupRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.FULLNAME, map.get(ConstVariable.FULLNAME).toString());
        Utils.global.mapMain.put("lname", map.get("lname").toString());
        Utils.global.mapMain.put(ConstVariable.EMAIL, map.get(ConstVariable.EMAIL).toString());
        Utils.global.mapMain.put(ConstVariable.MOBILE, map.get(ConstVariable.MOBILE).toString());
        Utils.global.mapMain.put("purl", map.get("purl").toString());
        // Utils.global.mapMain.put("location_id",map.get("location_id").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_SOCIAL_SIGN_UP);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.SocialSignUp);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void bookingRequest(Context mcontext, HashMap<String, Object> map) {
        Date todayDate = Calendar.getInstance().getTime();

        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        //SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm:ss aa");

        // String date=formatter.format(todayDate);
        // String time=formatter1.format(todayDate);

        new Utils(mcontext);
        Utils.global.mapMain();
        // Utils.global.mapMain.put("date",date);
        // Utils.global.mapMain.put("time",time);
        Utils.global.mapMain.put("json", map.get("json").toString());
        Utils.global.mapMain.put("jsonstops", map.get("jsonstops").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_BOOKING_REQUEST);

        Log.e("Tagggg", Utils.global.mapMain.toString());

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.BookReseration);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void applyPromo(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("promo", map.get("promo").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_APPLY_PROMO);

        Log.e("Tagggg", Utils.global.mapMain.toString());

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.ApplyPromo);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void updateBookingRequest(Context mcontext, HashMap<String, Object> map) {
        new Utils(mcontext);
        Utils.global.mapMain();

        Utils.global.mapMain.put("json", map.get("json").toString());
        Utils.global.mapMain.put("jsonstops", map.get("jsonstops").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_EDIT_RIDE_INFO);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.UpdateBookingReservation);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void addDropAddressRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String lid = pref1.getClocation();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("user_id", id);
        Utils.global.mapMain.put("street_address", map.get("street_address").toString());
        Utils.global.mapMain.put("address2", map.get("address2").toString());
        Utils.global.mapMain.put("city", map.get("city").toString());
        Utils.global.mapMain.put("state", map.get("state").toString());
        Utils.global.mapMain.put("zipcode", map.get("zipcode").toString());
        Utils.global.mapMain.put("latitude", map.get("latitude").toString());
        Utils.global.mapMain.put("longitude", map.get("longitude").toString());
        Utils.global.mapMain.put("address_type", map.get("address_type").toString());
        Utils.global.mapMain.put("notes", map.get("notes").toString());

        Utils.global.mapMain.put("type", map.get("type").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_ADD_DROP_ADDRESS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.AddDropAddress);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void userLocationUpdateRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.USERID, id);
        Utils.global.mapMain.put("laitude", map.get("laitude").toString());
        Utils.global.mapMain.put("longitude", map.get("longitude").toString());
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_USER_LOCATION_UPDATE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.UserLocationUpdate);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void deviceTokenRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("devicetoken", map.get("devicetoken").toString());
        Utils.global.mapMain.put("device_type", "android");
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_DEVICE_TOKEN_UPDATION);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DeviceTokenUpdate);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getDropOffAddressesRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("type", map.get("type").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_DROP_OFF_ADDRESS_LIST);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DropOffAddressList);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getStopLlocationsRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);

        if (map.containsKey("rideid"))
            Utils.global.mapMain.put("rideid", map.get("rideid").toString());

        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_STOP_LOCATIONS_LIST);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.StopLocations);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void deleteDropAddressRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("id", map.get("id").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_DELETE_DROP_ADDRESS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.DeleteDropAddress);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void editDropAddressRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("addressid", map.get("id").toString());
        Utils.global.mapMain.put("street_address", map.get("street_address").toString());
        Utils.global.mapMain.put("address2", map.get("address2").toString());
        Utils.global.mapMain.put("city", map.get("city").toString());
        Utils.global.mapMain.put("state", map.get("state").toString());
        Utils.global.mapMain.put("zipcode", map.get("zipcode").toString());
        Utils.global.mapMain.put("latitude", map.get("latitude").toString());
        Utils.global.mapMain.put("longitude", map.get("longitude").toString());
        Utils.global.mapMain.put("address_type", map.get("address_type").toString());
        Utils.global.mapMain.put("notes", map.get("notes").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_EDIT_DROP_ADDRESS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.EditDropAddress);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void cancelUserRidRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.USERID, id);
        Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());

        if (!map.get("reason").toString().equalsIgnoreCase(""))
            Utils.global.mapMain.put("reason", map.get("reason").toString());

        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CANCEL_RIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.CancelRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void cancelFutureRideAmountRequest(Context mcontext, HashMap<String, Object> map) {
        Date todayDate = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        //SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm:ss aa");

        String datetime = formatter.format(todayDate);
        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());
        Utils.global.mapMain.put("cancel_time", datetime);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CANCEL_RIDE_AMOUNT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.CancelAmount);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void cancelRideAmountRequest(Context mcontext, HashMap<String, Object> map) {
        Date todayDate = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        //SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm:ss aa");

        String datetime = formatter.format(todayDate);

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String rid = pref1.getRideId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("ride_id", rid);
        Utils.global.mapMain.put("cancel_time", datetime);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CANCEL_RIDE_AMOUNT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.CancelAmount);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void cancelFutureRidRequest(Context mcontext, HashMap<String, Object> map) {
        Date todayDate = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        //SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm:ss aa");

        String datetime = formatter.format(todayDate);
        // String time=formatter1.format(todayDate);


        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.USERID, id);
        Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());
        Utils.global.mapMain.put("cancel_time", datetime);

        if (!map.get("reason").toString().equalsIgnoreCase(""))
            Utils.global.mapMain.put("reason", map.get("reason").toString());

        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CANCEL_FUTURE_RIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.CancelFutureRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getDriverDetailsRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("user_id", id);
        Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());

      /*  Utils.global.mapMain.put("user_id","132");
        Utils.global.mapMain.put("ride_id","703");*/

        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_FUTURE_RIDE_DRIVER_DETAILS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.FutureDriverDetails);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void locationServiceInfoRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.USERID, id);
        Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());
        Utils.global.mapMain.put("reason", map.get("devicetoken").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_CANCEL_RIDE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.CancelRide);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getPartnerDetailsRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_ACTIVE_PARTNER_DETAILS);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.ActivePartner);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void estimatedPriceRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("pick_lat", map.get("pick_lat").toString());
        Utils.global.mapMain.put("pick_lng", map.get("pick_lng").toString());
        Utils.global.mapMain.put("dest_lat", map.get("dest_lat").toString());
        Utils.global.mapMain.put("dest_lng", map.get("dest_lng").toString());
        Utils.global.mapMain.put("promo", map.get("promo").toString());
        Utils.global.mapMain.put("count", map.get("count").toString());
        Utils.global.mapMain.put("date", map.get("date").toString());
        Utils.global.mapMain.put("time", map.get("time").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_ESTIMATED_PRICE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.EstimatedPrice);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void updateEditPriceRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("pick_lat", map.get("pick_lat").toString());
        Utils.global.mapMain.put("pick_lng", map.get("pick_lng").toString());
        Utils.global.mapMain.put("dest_lat", map.get("dest_lat").toString());
        Utils.global.mapMain.put("dest_lng", map.get("dest_lng").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_ESTIMATED_PRICE);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.UpdatedEstimatedPrice);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void paymentSummaryRequest(Context mcontext, HashMap<String, Object> map) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("ride_id", map.get("ride_id").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_PAYMENT_SUMMARY);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.PaymentSummary);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }
}
