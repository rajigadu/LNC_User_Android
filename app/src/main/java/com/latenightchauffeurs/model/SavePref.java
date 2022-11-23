package com.latenightchauffeurs.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class SavePref {
    private static final String TAG = "SavePref";
    Context con;

    SharedPreferences preferences = null;
    Editor editor = null;

    public void clear() {
//		 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//	    editor = preferences.edit();
        editor.clear();
        editor.commit();

        Log.d(TAG, "preferences cleared");
    }

    public void SavePref(Context c) {
        con = c;

        preferences = PreferenceManager.getDefaultSharedPreferences(con);
        editor = preferences.edit();
    }

    public String getcount() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("count", "");

        //Log.d(TAG, "setFName received "+name);

        return name;
    }

    public void setcount(String string) {
        // TODO Auto-generated method stub
        editor.putString("count", string);
        editor.commit();
    }


    public String getstatus() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("remember", "");

        //Log.d(TAG, "setFName received "+name);

        return name;
    }

    public void setstatus(String string) {
        // TODO Auto-generated method stub
        editor.putString("remember", string);
        editor.commit();
    }

    public void setsocialmail(String string) {
        // TODO Auto-generated method stub
        editor.putString("socialmail", string);
        editor.commit();
    }

    public String getsocilamail() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("socialmail", "");

        //Log.d(TAG, "setFName received "+name);

        return name;
    }

    public void setisnewmsg(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("isnewmsg", string);
        editor.commit();
        //  System.out.println("val save email ");

        //Log.d(TAG, "getUserId received");
    }

    public String getisnewmsg() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isnewmsg", "");

        //Log.d(TAG, "getUserId received "+name);

        return name;
    }


    public void setisfnewmsg(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("isfnewmsg", string);
        editor.commit();
        //  System.out.println("val save email ");

        //Log.d(TAG, "getUserId received");
    }

    public String getisfnewmsg() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isfnewmsg", "");

        //Log.d(TAG, "getUserId received "+name);

        return name;
    }


    public String getaddress() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("address", "");

        //Log.d(TAG, "setFName received "+name);

        return name;
    }

    public void setaddress(String string) {
        // TODO Auto-generated method stub
        editor.putString("address", string);
        editor.commit();
    }


    public String getmap() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("map", "");

        //Log.d(TAG, "setFName received "+name);

        return name;
    }

    public void setmap(String string) {
        // TODO Auto-generated method stub
        editor.putString("map", string);
        editor.commit();
    }

    public void setbaddress(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("baddress", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setId received");
    }

    public String getbaddress() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("baddress", "");

        Log.d(TAG, "setId received " + name);

        return name;
    }


    public void setName(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setName", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setName received");
    }


    public String getName() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setName", "");

        Log.d(TAG, "setName received " + name);

        return name;
    }

    public void setEmail(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setEmail", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setEmail received");
    }


    public String getEmail() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setEmail", "");

        Log.d(TAG, "setEmail received " + name);

        return name;
    }


    public void setcart_buy_id(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("cartid", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setEmail received");
    }


    public String getcart_buy_id() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("cartid", "");

        Log.d(TAG, "setEmail received " + name);

        return name;
    }


    public void setmyaddress(String string) {
        editor.putString("setPwd", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }

    public String getmyaddress() {
        String name = preferences.getString("setPwd", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }

    public void setplot(String string) {
        editor.putString("plot", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }

    public String getplot() {
        String name = preferences.getString("plot", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }


    public String getstreet() {
        String name = preferences.getString("street", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }

    public void setstreet(String string) {
        editor.putString("street", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }


    public String getnotepoint() {
        String name = preferences.getString("landmark", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }

    public void setnotepoint(String string) {
        editor.putString("landmark", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }

    public String getbuilding() {
        String name = preferences.getString("city", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }

    public void setbuilding(String string) {
        editor.putString("city", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }

    public String getgender() {
        String name = preferences.getString("state", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }

    public void setgender(String string) {
        editor.putString("state", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }


    public String getpin() {
        String name = preferences.getString("pin", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }

    public void setpin(String string) {
        editor.putString("pin", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }


    public String getdob() {
        String name = preferences.getString("dob", "");
        Log.d(TAG, "setPwd received " + name);
        return name;
    }

    public void setdob(String string) {
        editor.putString("dob", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setPwd received");
    }

    public void setMobile(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setMobile", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setMobile received");
    }

    public String getMobile() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setMobile", "");

        Log.d(TAG, "setMobile received " + name);

        return name;
    }


    public void setImage(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setimage", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setMobile received");
    }

    public String getImage() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setimage", "");

        Log.d(TAG, "setMobile received " + name);

        return name;
    }

    public void setUserFName(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("FName", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setMobile received");
    }

    public String getUserFName() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("FName", "");

        Log.d(TAG, "setMobile received " + name);

        return name;
    }

    public void setUserLName(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("LName", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setMobile received");
    }

    public String getUserLName() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("LName", "");

        Log.d(TAG, "setMobile received " + name);

        return name;
    }


    public void setzip(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("zip", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setProfilePic received");
    }

    public String getzip() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("zip", "");

        Log.d(TAG, "setProfilePic received " + name);

        return name;
    }

    public void setUserId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("getUserId", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getUserId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("getUserId", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setDriverId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("DriverId", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getDriverId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("DriverId", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }


    public void setdToken(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("devicetoken", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getdToken() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("devicetoken", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }


    public void setClocation(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("Clocation", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getClocation() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("Clocation", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }


    public void setlocationlat(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("locationlat", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getlocationlat() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("locationlat", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }


    public void setlocationlng(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("locationlng", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getlocationlng() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("locationlng", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setdrivermap(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("drivermap", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getdrivermap() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("drivermap", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setRideId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("RideId", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getRideId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("RideId", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }


    public void setCardId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("CardId", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getCardId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("CardId", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }


    public void setAcctid(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setAcctid", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "setAcctid received");
    }

    public String getAcctid() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setAcctid", "");

        Log.d(TAG, "setAcctid received " + name);

        return name;
    }




    public void setCardNumber(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("CardNumber", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getCardNumber() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("CardNumber", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setCardExMonth(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("CardExMonth", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getCardExMonth() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("CardExMonth", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setCardExYear(String string) {
        // TODO Auto-generated method stub

        editor.putString("CardExYear", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getCardExYear() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("CardExYear", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setRatingStatus(String string) {
        // TODO Auto-generated method stub

        editor.putString("RatingStatus", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getRatingStatus() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("RatingStatus", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setdRatingmap(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("Ratingmap", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getRatingmap() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("Ratingmap", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setisLocationUpdate(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("isLocationUpdate", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getisLocationUpdate() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isLocationUpdate", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setautoRideId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("autoRideId", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getautoRideId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("autoRideId", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }





    public void setBadgeCount(int string) {
        // TODO Auto-generated method stub
        editor.putInt("getBadgeCount", string);
        editor.commit();
    }

    public int getBadgeCount() {
        int name = preferences.getInt("getBadgeCount", 0);
        return name;
    }

}
