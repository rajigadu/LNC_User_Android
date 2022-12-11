package com.latenightchauffeurs.Utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.latenightchauffeurs.BuildConfig;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.activity.Navigation;
import com.latenightchauffeurs.model.SavePref;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
/**
 * Created by narip on 2/4/2017.
 */
public class Utils {
    private static final String TAG = "Utils";
    public static String Tag = "Utils_Log";
    public static final String EDIT_RIDE_INFO = "edit_ride_info";
    public static Context context = null;
    public static Global global;
    public static PopupWindow pwindo;
    static LayoutInflater inflater;
    static ProgressBar progress;
    public static ProgressDialog progressDialog;
    static Boolean showPopoup = true;
    // Milliseconds
    static LocationManager locationManager;

    // IMage Loader end
    private static AlertDialog alert;
    private static SharedPreferences.Editor editor;
    private static Pattern pattern;
    public static SharedPreferences prefs;
    private static Matcher matcher;

    public Utils(Context context) {
        if (context == null)
            context = Global.getAppContext();

        if (context != null) {
            if (global == null)
                global = new Global();

            Utils.context = context;

            if (context instanceof Activity)
                global.setCurrentActivity((Activity) context);
        }
    }

    public static void userLocationRequest(Context mcontext, String locid, String type) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("location_id", locid);
        Utils.global.mapMain.put("type", type);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_USER_LOCATION_INFO_UPDATION);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.UserLocationinfoUpdate);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void getFeedbackRequest(Context mcontext, String msg, String rating) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String did = pref1.getDriverId();

        double r = Double.parseDouble(rating);
        int rr = (int) r;

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("driverid", did);
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("msg", msg);
        Utils.global.mapMain.put("rating", rr);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_FEED_BACK);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.UserRating);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void setImagePiccaso(Context context, String url, ImageView imagevv) {
        //Utils.e(Tag+"118", "PHOTO===="+url);

        Picasso.with(context).load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(imagevv);

        /*Picasso.with(context).load(url).resize(600, 600)
                .centerInside()
                .onlyScaleDown()
                .into(new Target()
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap,Picasso.LoadedFrom from)
                    {
                        imagevv.setImageBitmap(bitmap);
                        Log.e("TAG"+"130", "SUCCESS");
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable)
                    {
                        Log.e("TAG4"+"137", "FAILED");
                        imagevv.setImageResource(R.drawable.placeholder);
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable)
                    {
                        Log.e("TAG4"+"148", "Prepare Load");
                    }
                });*/
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void showOkDialog(String dlgText, Context parent) {
        if (context == null && Utils.context != null) {
            context = Utils.context;
        }

        // final Dialog dialog = new Dialog(parent);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //  dialog.setContentView(R.layout.alert_dialog);

        LayoutInflater inflater = LayoutInflater.from(Utils.context);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog1, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(Utils.context).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        // rootlo.getLayoutParams().width = getHeightWidth("width") - getHeightWidth("width")/4;
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final Button buttonok = (Button) dialog.findViewById(R.id.buttonOk);
        // final ImageView iconimage = (ImageView) dialog .findViewById(R.id.imageView);
        // iconimage.setVisibility(View.VISIBLE);
        textView.setText(dlgText);
        title.setText(R.string.app_name);
//        Utils.setFontStyle(context, buttonok);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {

                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    dialog.cancel();
                }
            }
        });
        dialog.show();

        dialogLayout.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });
    }

    public static void setLogOut(Context context) {
        if (context == null && Utils.context != null) {
            context = Utils.context;
        }

        /* prefs = context.getSharedPreferences("com.haystack",0);
         editor = prefs.edit();
         editor.clear();
         editor.commit();*/
        Utils.toastTxt("You Logout Successfully", Utils.context);

        Settings.NETWORK_STATUS = "";
    }

    public static final void toastTxt(String str, Context context) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null && Utils.context != null) {
            context = Utils.context;
        }
        NetworkInfo activeNetworkInfo = null;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                Settings.NETWORK_STATUS = "";
                Settings.NETWORK_TYPE = "";
            } else {
                if (activeNetworkInfo.getType() == 1) {
                    Settings.NETWORK_TYPE = "WiFi";
                }
                if (activeNetworkInfo.getType() == 0)
                    Settings.NETWORK_TYPE = "3G";
                if (activeNetworkInfo.getType() == 0
                        && activeNetworkInfo.getType() == 1)
                    Settings.NETWORK_TYPE = "WiFi";
                if (activeNetworkInfo.getType() == 6) {
                    Settings.NETWORK_TYPE = "WiMax";
                }
            }
        }
        return activeNetworkInfo != null;
    }

    public static void showInternetErrorMessage(Context context) {
        if (context == null && Utils.context != null) {
            context = Utils.context;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final Button buttonok = (Button) dialog.findViewById(R.id.buttonOk);
        // final ImageView iconimage = (ImageView) dialog.findViewById(R.id.imageView);
        // iconimage.setVisibility(View.VISIBLE);
        textView.setText(R.string.connection_subtext);
        // title.setText(R.string.connection_errortext);
//        Utils.setFontStyle(context, buttonok);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {

                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    public static final void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void uploadImage(Context mcontext, Uri fileUri, HashMap<String, Object> param, final int mode) {
        Utils.e("JsonPost54123", "send events are >>>" + param);
        // create upload service client
        // initiatePopupWindow(mContext, "uploading Image...");
        initiatePopupWindow(context, "uploading Image...");

        UploadImage service = ServiceGenerator.createService(UploadImage.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        MultipartBody.Part body = null;

        if (fileUri != null) {
            File file = new File(fileUri.getPath());

            // create RequestBody instance from file
            RequestBody reqFile = RequestBody.create(MediaType.parse("profilepic/*"), file);

            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("profilepic", "profilepic.png", reqFile);
        }

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), param.get(ConstVariable.USERID).toString());

        RequestBody fname = null, lname = null, mobile = null;
        Call<ResponseBody> call = null;

        if (mode == ConstVariable.EditProfile) {
            fname = RequestBody.create(MediaType.parse("multipart/form-data"), param.get("fname").toString());
            lname = RequestBody.create(MediaType.parse("multipart/form-data"), param.get("lname").toString());
            mobile = RequestBody.create(MediaType.parse("multipart/form-data"), param.get(ConstVariable.MOBILE).toString());

            call = service.upload(param.get(ConstVariable.URL).toString(), id, fname, lname, mobile, body);
        } else {
            call = service.uploadres(param.get(ConstVariable.URL).toString(), body);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (Utils.progressDialog != null) {
                        Utils.progressDialog.dismiss();
                        Utils.progressDialog = null;
                    }
                    String resp = "";
                    Log.e("Rrespppppp123456--->", response.body().toString());
                    if (response.body() != null) {
                        resp = response.body().string();
                        System.out.println("Rrespppppp--->" + resp);
                        Log.e("response4227", "response------------------>" + resp);

                        if (mode == ConstVariable.Login) {
                            String result = JsonHelper.getResults(resp, context, mode);

                            if (result.equalsIgnoreCase(ConstVariable.SUCCESS)) {
                                Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), Utils.context);
                            } else {
                                Utils.showOkDialog(result.toString(), context);
                            }
                        }

                        if (mode == ConstVariable.EditProfile) {
                            String result = JsonHelper.getResults(resp, context, mode);

                            if (result.equalsIgnoreCase(ConstVariable.SUCCESS)) {
                                new Utils(context);
                                Utils.toastTxt("Profile updated  successfully.", context);
                                //com.munched.activity.EditProfile.Instance.closeactivity();

                                SavePref pref1 = new SavePref();
                                pref1.SavePref(context);
                                pref1.setImage(Utils.global.mapMain.get("profile_pic").toString());
                                Log.e("Rrespppppp9999999--->", Utils.global.mapMain.get("profile_pic").toString());
                                pref1.setUserFName(Utils.global.mapMain.get("first_name").toString());
                                pref1.setUserLName(Utils.global.mapMain.get("last_name").toString());
                                pref1.setMobile(Utils.global.mapMain.get(ConstVariable.MOBILE).toString());
                                Navigation.nid = 4;
                                Utils.startActivity(context, Navigation.class);
                            } else {
                                new Utils(context);
                                Utils.toastTxt(result.toString(), context);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                //Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public static void initiatePopupWindow(Context mcontext, String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(mcontext);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait request is in progress...");
                progressDialog.show();
            }
        } catch (Exception e) {
            //Utils.e("Utils 3548","Exception=============Exception==================Exception");
            e.printStackTrace();
        } finally {
            //Utils.e("2initiatePopupWindow","2513test------" + Global.mCurrentActivity);
        }
    }

    public static void initiateProgressWindow(Context mcontext, String text) {
        try {
            if (pwindo == null) {
                showPopoup = true;
                //Utils.e("Utils 3492", "initiatePopupWindow" + Global.mCurrentActivity);
                inflater = (LayoutInflater) ((Activity) mcontext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("WrongViewCast") final View container = inflater.inflate(R.layout.action_item_progress, null);

                pwindo = new PopupWindow(container, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);

                progress = (ProgressBar) container.findViewById(R.id.downLoad_progressbar);
                TextView downLoad_name = (TextView) container.findViewById(R.id.downLoad_name);
                downLoad_name.setText(text);

                pwindo.setBackgroundDrawable(new BitmapDrawable());

                container.findViewById(R.id.main).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pwindo != null) {
                            pwindo.showAtLocation(container.findViewById(R.id.main), Gravity.CENTER | Gravity.BOTTOM, 0, 100);
                        }
                    }
                }, 100L);
            }
        } catch (Exception e) {
            //Utils.e("Utils 3548","Exception=============Exception==================Exception");
            e.printStackTrace();
        } finally {
            //Utils.e("2initiatePopupWindow","2513test------" + Global.mCurrentActivity);
        }
    }


    public static void startActivity(Context context, Class<?> activity) {
        if (context == null)
            context = Utils.context;

        Intent intent = new Intent(context, activity);
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((Activity) context).startActivityForResult(intent, 0);
        } catch (Exception ex) {
            //Utils.e(Tag + "1896", "Exception===========Exception===========Exception");
            ex.printStackTrace();
        }
        intent = null;
    }

    public static HashMap<String, Object> GetJsonDataIntoMap(Context context, JSONArray array, String mode) {
        // SharedPreferences prefs =
//        Utils.e("Utils 709", "GetEventOfflineList ok");
        HashMap<String, Object> item = new HashMap<String, Object>();
        // List<HashMap<String, Object>> collection = new ArrayList<HashMap<String, Object>>();
//        if (context instanceof MainActivityAdmin) {
        try {
//                Utils.e("Utils 712", "GetEventOfflineList ok");

            HashMap<String, Object> itemSub = null;
//                Utils.e("Utils 715", "GetEventOfflineList ok"+array);
            JSONObject ary = null;
            JSONObject arySub = null;
//                Utils.e("Utils760", ""+array);
            for (int i = 0; i < array.length(); i++) {
//                    Utils.e("Utils 717", "GetEventOfflineList ok " + i);
//                String obj = (String) array.get(i);
                ary = array.optJSONObject(i);

//                    Utils.e("Utils 719", "JSONObject ok " + ary);
                if (ary != null) {
//                        Utils.e("Utils 720", "JSONObject ok " + ary);
                    Iterator<String> it = ary.keys();
//                        Utils.e("Utils 723", "Iterator ok " + it);
                    while (it.hasNext()) {
                        String key = it.next();

                        //for sub array
                        if (key.equalsIgnoreCase(ConstVariable.SUBARRAY)) {
//                            Utils.e("Utils 733", "JSONObject ok "+ConstVariable.EVENTIMAGE);
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY);
//                            Utils.e("Utils 733", "arraySub ok "+arraySub);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.e("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.e("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.e("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.e("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.e("Utils780",key+" : "+ary.get(key));
                                                itemSub.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.e("Utils780","key : "+key);
                                                itemSub.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                item.put(ConstVariable.SUBARRAY, tempArrayMapImage);
//                                    Utils.e("Utils 795", "arySubPer ok " + item);
                            } else {
                                item.put(ConstVariable.SUBARRAY, null);
                            }
                        } else if (key.equalsIgnoreCase(ConstVariable.SUBARRAY1)) {
//                            Utils.e("Utils 733", "JSONObject ok "+ConstVariable.EVENTIMAGE);
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY1);
//                            Utils.e("Utils 733", "arraySub ok "+arraySub);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.e("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.e("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.e("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.e("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.e("Utils780",key+" : "+ary.get(key));
                                                itemSub.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.e("Utils780","key : "+key);
                                                itemSub.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                item.put(ConstVariable.SUBARRAY1, tempArrayMapImage);
//                                    Utils.e("Utils 795", "arySubPer ok " + item);
                            } else {
                                item.put(ConstVariable.SUBARRAY1, null);
                            }
                        } else {
                            if (ary.get(key) != null) {
//                                    Utils.e("Utils780",key+" : "+ary.get(key));
                                item.put(key, ary.get(key));
                            } else {
//                                    Utils.e("Utils780","key : "+key);
                                item.put(key, "");
                            }
                        }

                    }
//                    collection.add(item);

                }
            }
        } catch (JSONException e) {
            //Utils.e("Error 4094", "while parsing===================JSONException==============JSONException");
            e.printStackTrace();
        }
//        }
        //Utils.e("Utils 751", "collection ok " + item);
        return item;
    }

    public static List GetJsonDataIntoList(Context context, JSONArray array, String mode) {
        // SharedPreferences prefs =
//        Utils.e("Utils 709", "GetEventOfflineList ok");

        List<HashMap<String, Object>> collection = new ArrayList<HashMap<String, Object>>();
//        if (context instanceof MainActivityAdmin) {
        try {
//                Utils.e("Utils 712", "GetEventOfflineList ok");
            HashMap<String, Object> item = null;
            HashMap<String, Object> itemSub = null;
            //Utils.e("Utils 715", "GetEventOfflineList ok"+array);
            JSONObject ary = null;
            JSONObject arySub = null;
//                Utils.e("Utils760", ""+array);
            for (int i = 0; i < array.length(); i++) {
//                    Utils.e("Utils 717", "GetEventOfflineList ok " + i);
//                String obj = (String) array.get(i);
                ary = array.optJSONObject(i);
//                    Utils.e("Utils 719", "JSONObject ok " + ary);
                if (ary != null) {
//                        Utils.e("Utils 720", "JSONObject ok " + ary);
                    Iterator<String> it = ary.keys();
                    // Utils.e("Utils 3695", "Iterator ok " + it);
                    item = new HashMap<String, Object>();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (key.equalsIgnoreCase(ConstVariable.SUBARRAY2)) {
//                            Utils.e("Utils 733", "JSONObject ok "+ConstVariable.EVENTIMAGE);
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY2);
//                            Utils.e("Utils 733", "arraySub ok "+arraySub);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.e("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.e("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.e("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.e("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.e("Utils780",key+" : "+ary.get(key));
                                                itemSub.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.e("Utils780","key : "+key);
                                                itemSub.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                item.put(ConstVariable.SUBARRAY2, tempArrayMapImage);
//                                    Utils.e("Utils 795", "arySubPer ok " + item);
                            } else {
                                item.put(ConstVariable.SUBARRAY2, null);
                            }
                        } else if (key.equalsIgnoreCase(ConstVariable.SUBARRAY)) {
//                            Utils.e("Utils 733", "JSONObject ok "+ConstVariable.EVENTIMAGE);
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY);
//                            Utils.e("Utils 733", "arraySub ok "+arraySub);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {

//                                Utils.e("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.e("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.e("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.e("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.e("Utils780",key+" : "+ary.get(key));
                                                itemSub.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.e("Utils780","key : "+key);
                                                itemSub.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                item.put(ConstVariable.SUBARRAY, tempArrayMapImage);
//                                    Utils.e("Utils 795", "arySubPer ok " + item);
                            } else {
                                item.put(ConstVariable.SUBARRAY, null);
                            }
                        } else if (key.equalsIgnoreCase(ConstVariable.SUBARRAY1)) {
//                            Utils.e("Utils 733", "JSONObject ok "+ConstVariable.EVENTIMAGE);
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY1);
//                            Utils.e("Utils 733", "arraySub ok "+arraySub);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.e("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.e("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.e("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.e("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.e("Utils780",key+" : "+ary.get(key));
                                                itemSub.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.e("Utils780","key : "+key);
                                                itemSub.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                item.put(ConstVariable.SUBARRAY1, tempArrayMapImage);
//                                    Utils.e("Utils 795", "arySubPer ok " + item);
                            } else {
                                item.put(ConstVariable.SUBARRAY1, null);
                            }
                        } else {
                            if (ary.get(key) != null) {
//                                    Utils.e("Utils780",key+" : "+ary.get(key));
                                item.put(key, ary.get(key));
                            } else {
//                                    Utils.e("Utils780","key : "+key);
                                item.put(key, "");
                            }
                        }
                    }
                    collection.add(item);
//                        Utils.e("Utils 751", "collection ok " + collection);
                }
            }
        } catch (JSONException e) {
            // Utils.e("Error 3860", "while parsing=====JSONException================JSONException");
            e.printStackTrace();
        }
//        }
        return collection;
    }

    public static void startPowerSaverIntent(final Context context) {
        SharedPreferences settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        boolean skipMessage = settings.getBoolean("skipProtectedAppCheck", false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            boolean foundCorrectIntent = false;
            for (final Intent intent : POWERMANAGER_INTENTS) {
                if (isCallable(context, intent)) {
                    foundCorrectIntent = true;
                    final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                    dontShowAgain.setText("Do not show again");
                    dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            editor.putBoolean("skipProtectedAppCheck", isChecked);
                            editor.apply();
                        }
                    });

                    new AlertDialog.Builder(context)
                            .setTitle(Build.MANUFACTURER + " Protected Apps")
                            .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", context.getString(R.string.app_name)))
                            .setView(dontShowAgain)
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                    break;
                }
            }
            if (!foundCorrectIntent) {
                editor.putBoolean("skipProtectedAppCheck", true);
                editor.apply();
            }
        }
    }

    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")).setData(Uri.fromParts("package", MyApplication.getContext().getPackageName(), null)),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"))
                    .setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart")),
            new Intent().setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.SHOW_APPSEC")).addCategory(Intent.CATEGORY_DEFAULT).putExtra("packageName", BuildConfig.APPLICATION_ID)
    );

    public static void ifHuaweiAlert(final Context context) {
        final SharedPreferences settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(context, intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                dontShowAgain.setText("Do not show again");
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Huawei Protected Apps")
                        .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", context.getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton("Protected Apps", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps(context);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }


    private static void huaweiProtectedApps(Context context) {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial(context);
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private static String getUserSerial(Context context) {
        //noinspection ResourceType
        @SuppressLint("WrongConstant") Object userManager = context.getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (Exception ignored) {
        }
        return "";
    }





    public static String isEmptyNull(String address) {
        if(address != null && !address.isEmpty() && !address.equalsIgnoreCase("null")) {
            return address;
        }else{
            return "";
        }
    }








    @SuppressLint("LongLogTag")
    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address = new ArrayList<>();
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Log.e(TAG, "address111 "+address);

            if(address.size() > 0){
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getMessage111 "+ex.getMessage());
        }
        return p1;
    }










    @SuppressLint("LongLogTag")
    public static LatLng getLocationFromAddress2(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address = new ArrayList<>();
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Log.e(TAG, "address111 "+address);

            if(address.size() > 0){
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getMessage111 "+ex.getMessage());
        }
        return p1;
    }






    public static HashMap<String, Object> getImageLoaderRoundStrech(Activity context) {
        HashMap<String, Object> loaderHashMap = new HashMap<>();

        Resources mResources = context.getResources();
        Bitmap mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.appicon);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap
        );
//        roundedBitmapDrawable.setCornerRadius(20.0f);
        roundedBitmapDrawable.setCircular(true);
        roundedBitmapDrawable.setAntiAlias(true);

        ImageLoader imageLoader = null;
        DisplayImageOptions options = null;

        try{
            imageLoader = ImageLoader.getInstance();

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(roundedBitmapDrawable)
                    .showImageForEmptyUri(roundedBitmapDrawable)
                    .showImageOnFail(roundedBitmapDrawable)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(20))
//                    .displayer(new CircleBitmapDisplayer(context.getResources().getColor(R.color.colorPrimary), 1))

                    .build();
        }catch(Exception e){
            Log.d(TAG, "myError11: "+e.getMessage());
        }

        loaderHashMap.put("imageLoader", imageLoader);
        loaderHashMap.put("options", options);

        return loaderHashMap;
    }






    public static HashMap<String, Object> getImageLoaderUser(Activity context) {
        HashMap<String, Object> loaderHashMap = new HashMap<>();

        Resources mResources = context.getResources();
        Bitmap mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.appicon);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap
        );

        roundedBitmapDrawable.setCircular(true);
//        roundedBitmapDrawable.setCornerRadius(20.0f);
//        roundedBitmapDrawable.setAntiAlias(true);

        ImageLoader imageLoader = null;
        DisplayImageOptions options = null;

        try{
            imageLoader = ImageLoader.getInstance();

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(roundedBitmapDrawable)
                    .showImageForEmptyUri(roundedBitmapDrawable)
                    .showImageOnFail(roundedBitmapDrawable)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
//                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(20))
                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
                    .build();
        }catch(Exception e){
            Log.d(TAG, "myError11: "+e.getMessage());
        }

        loaderHashMap.put("imageLoader", imageLoader);
        loaderHashMap.put("options", options);

        return loaderHashMap;
    }





//    public static HashMap<String, Object> getImageLoaderCornerRound(Context context) {
//        HashMap<String, Object> loaderHashMap = new HashMap<>();
//
//        Resources mResources = context.getResources();
//        Bitmap mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.device201910);
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//                mResources,
//                mBitmap
//        );
//        roundedBitmapDrawable.setCornerRadius(20.0f);
//        roundedBitmapDrawable.setAntiAlias(true);
//
//        com.nostra13.universalimageloader.core.ImageLoader imageLoader = null;
//        DisplayImageOptions options = null;
//
//        try{
//            imageLoader = ImageLoader.getInstance();
//            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(roundedBitmapDrawable)
//                    .showImageForEmptyUri(roundedBitmapDrawable)
//                    .showImageOnFail(roundedBitmapDrawable)
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .considerExifParams(true)
//                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(20))
//                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
//                    .build();
//        }catch(Exception e){
//            Log.d(TAG, "myError11: "+e.getMessage());
//        }
//
//        loaderHashMap.put("imageLoader", imageLoader);
//        loaderHashMap.put("options", options);
//
//        return loaderHashMap;
//    }

}

