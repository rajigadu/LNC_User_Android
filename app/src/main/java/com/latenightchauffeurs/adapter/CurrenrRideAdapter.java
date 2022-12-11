package com.latenightchauffeurs.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonHelper;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.ServiceApi;
import com.latenightchauffeurs.Utils.ServiceGenerator;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.ActivityEditBookingInfo;
import com.latenightchauffeurs.activity.StopLocationsList;
import com.latenightchauffeurs.activity.ViewAvailableRide;
import com.latenightchauffeurs.activity.ViewRideDetails;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class CurrenrRideAdapter extends RecyclerView.Adapter {

      ServiceApi service = ServiceGenerator.createService(ServiceApi.class);


    private static final String TAG = "CurrenrRideAdapter";
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public CurrenrRideAdapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode) {
        adapterList = students;
        itemLayout = layout;
        adapterMode = mode;
        mcontext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        vh = new InboxViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InboxViewHolder) {
            modelItem singleTripData = (modelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            //Utils.e("paymentslistadapter 107", "" + detailMap);

            try {
                ((InboxViewHolder) holder).root.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).stops.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).viewRide.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).btnEditRide.setTag(singleTripData.getMapMain());
                //((InboxViewHolder) holder).viewmap.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("booking_type") && !detailMap.get("booking_type").toString().equalsIgnoreCase("")) {
                    if (detailMap.get("booking_type").toString().equalsIgnoreCase("1")) {
                        ((InboxViewHolder) holder).status_ride.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).ridestatus_title.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).viewRide.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).btnEditRide.setVisibility(View.GONE);

                        if (detailMap.containsKey("otherdate") && !detailMap.get("otherdate").toString().equalsIgnoreCase("")) {
                            String ss = "", ee = "";

                            ss = "Date  :" + detailMap.get("otherdate").toString();
                            ee = "Time :" + detailMap.get("time").toString();

                            String s = ss
                                    + System.getProperty("line.separator")
                                    + ee
                                    + System.getProperty("line.separator");

                            ((InboxViewHolder) holder).date.setText(s);

                        }
                        //((InboxViewHolder) holder).date.setText(Html.fromHtml(detailMap.get("created_at").toString()));
                    } else {
                        ((InboxViewHolder) holder).status_ride.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).ridestatus_title.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).viewRide.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).btnEditRide.setVisibility(View.VISIBLE);

                        String ss = "", ee = "";

                        ss = "Date  :" + detailMap.get("otherdate").toString();
                        ee = "Time :" + detailMap.get("time").toString();

                        String s = ss
                                + System.getProperty("line.separator")
                                + ee
                                + System.getProperty("line.separator");

                        ((InboxViewHolder) holder).date.setText(s);
                    }
                }

                if (detailMap.containsKey("distance") && !detailMap.get("distance").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).distance.setText(String.format("%.2f", Double.valueOf(detailMap.get("distance").toString())) + " mi");
                }

                if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                    String s = "", ss = "", e = "", ee = "";
                    List<String> plist = new ArrayList<>();
                    List<String> elist = new ArrayList<>();

                    if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                        // plist.add(detailMap.get("pickup_street_address").toString());
                    }
                    if (detailMap.containsKey("pickup_address") && !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                        plist.add(detailMap.get("pickup_address").toString());
                    }

                    ss = TextUtils.join(",", plist);
                    s = getColoredSpanned(ss, "#800000");

                    if (detailMap.containsKey("drop_address") && !detailMap.get("drop_address").toString().equalsIgnoreCase("")) {
                        elist.add(detailMap.get("drop_address").toString());
                    }

                    ee = TextUtils.join(",", elist);
                    e = getColoredSpanned(ee, "#F69625");

                                     String PickUp = getColoredSpanned("PickUp: " , "#000000");
                    String DropOff = getColoredSpanned("DropOff: " , "#000000");

                    ((InboxViewHolder) holder).ride.setText(Html.fromHtml(PickUp+" "+s + "<br />  to  <br />" +DropOff+ " " + e ));
                }

                if (detailMap.containsKey("future_accept") && !detailMap.get("future_accept").toString().equalsIgnoreCase("")) {
                    if (detailMap.get("future_accept").toString().equalsIgnoreCase("0")) {
                        String c = "", cc = "Pending";
                        c = getColoredSpanned(cc, "#DF0614");
                        ((InboxViewHolder) holder).status_ride.setText(Html.fromHtml(c));
                        ((InboxViewHolder) holder).btnEditRide.setVisibility(View.VISIBLE);
                        //((InboxViewHolder) holder).viewRide.setVisibility(View.VISIBLE);
                    } else {
                        String c = "", cc = "Accepted";
                        c = getColoredSpanned(cc, "#417318");
                        ((InboxViewHolder) holder).status_ride.setText(Html.fromHtml(c));
                        ((InboxViewHolder) holder).btnEditRide.setVisibility(View.GONE);
                        // ((InboxViewHolder) holder).viewRide.setVisibility(View.GONE);

                        if (detailMap.get("status").toString().equalsIgnoreCase("1")) {
                            ((InboxViewHolder) holder).textViewAcc.setVisibility(View.VISIBLE);
                        }else{
                            ((InboxViewHolder) holder).textViewAcc.setVisibility(View.GONE);
                        }

                    }
                }


                ((InboxViewHolder) holder).btnEditRide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> dmap = new HashMap<>();
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }
                        }
//
                        if (Utils.global.mapData.size() > 0) {
//                            Intent i = new Intent(mcontext, ActivityEditBookingInfo.class);
//                            i.putExtra(Utils.EDIT_RIDE_INFO, (Serializable) dmap);
//                            mcontext.startActivity(i);

                            callFourHourApi(dmap);

//                            final ProgressDialog progressDialog = new ProgressDialog(mcontext);
//                            progressDialog.setMessage("Loading...");
//                            progressDialog.show();
                        }


                        Log.e(TAG, "dmapdmap "+dmap.toString());
                    }
                });

                ((InboxViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> dmap = new HashMap<>();
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }
                        }

                        if (Utils.global.mapData.size() > 0) {
                            //Utils.toastTxt("Still haven't implemented",mContext);
                            Intent i = new Intent(mcontext, ViewAvailableRide.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });

                ((InboxViewHolder) holder).stops.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> dmap = new HashMap<>();
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }
                        }

                        if (Utils.global.mapData.size() > 0) {
                            //Utils.toastTxt("Still haven't implemented",mContext);
                            Intent i = new Intent(mcontext, StopLocationsList.class);
                            dmap.put("uType", "1");
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });

                ((InboxViewHolder) holder).viewRide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> dmap = new HashMap<>();
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();


                        if (Utils.global.mapData.size() > 0) {
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }
                        }

                        if (Utils.global.mapData.size() > 0) {
                            //Utils.toastTxt("Still haven't implemented",mContext);
                            Intent i = new Intent(mcontext, ViewRideDetails.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });
                if (detailMap.containsKey("estimation_price")) {
                    ((InboxViewHolder) holder).tvRideCost.setText("$"+detailMap.get("estimation_price").toString() + "");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).hotel = singleTripData;
        }
    }

    private void callFourHourApi(final HashMap<String, Object> dmap) {


        final ProgressDialog progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("ride_id", dmap.get("id").toString());
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_EDIT_RIDES);


        if (Utils.isNetworkAvailable(mcontext)) {
            //getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.EDITRIDE);

            ServiceApi service = ServiceGenerator.createService(ServiceApi.class);

            Call<ResponseBody> response = service.login(com.latenightchauffeurs.Utils.Settings.URL_EDIT_RIDES, Utils.global.mapMain);

            response.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    progressDialog.dismiss();

                    try {

                        String result = response.body().string();
                        Log.e(TAG + "61cccc ", "RetroFit2.0 :RetroGetResult: " + result);


                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");

                        if(status.equals("0")){
                            String msg = jsonObject.getString("msg");

                            new AlertDialog.Builder(mcontext)
                                    .setTitle("Late Night Chauffeurs")
                                    .setMessage(""+msg)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();


                        }else{
                            Intent i = new Intent(mcontext, ActivityEditBookingInfo.class);
                            i.putExtra(Utils.EDIT_RIDE_INFO, (Serializable) dmap);
                            mcontext.startActivity(i);
                        }



                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });



        } else {
            Utils.showInternetErrorMessage(mcontext);
        }



    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        //Notification
        public modelItem hotel;
        public View gView;
        //***

        public TextView date, ride, distance, status_ride, ridestatus_title, tvRideCost, textViewAcc;
        public Button stops, viewRide, btnEditRide;
        public RelativeLayout root;

        public InboxViewHolder(View v) {
            super(v);
            try {
                date = v.findViewById(R.id.date);
                tvRideCost = v.findViewById(R.id.tv_ride_cost);
                stops = (Button) v.findViewById(R.id.stops);
                viewRide = (Button) v.findViewById(R.id.viewdetails);
                status_ride = (TextView) v.findViewById(R.id.ridestatus);
                ridestatus_title = (TextView) v.findViewById(R.id.ridestatus_title);
                ride = (TextView) v.findViewById(R.id.ride);
                distance = (TextView) v.findViewById(R.id.distance);
                root = (RelativeLayout) v.findViewById(R.id.rowitem_root);
                btnEditRide = v.findViewById(R.id.btn_edit_ride);
                textViewAcc = v.findViewById(R.id.ridestatus_acc);
            } catch (Exception e) {
                //Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }









    public void getNetworkResponse(final Context context, final ProgressBar progressBar,
                                                       final HashMap<String, Object> nameValuePairs, final int mode) {

       ServiceApi service = ServiceGenerator.createService(ServiceApi.class);

        Call<ResponseBody> response = service.login(com.latenightchauffeurs.Utils.Settings.URL_EDIT_RIDES, nameValuePairs);

        response.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //get your response....
                    if (Utils.progressDialog != null) {
                        Utils.progressDialog.dismiss();
                        Utils.progressDialog = null;
                    }
                    String result = response.body().string();
                    Log.e(TAG + "61cccc ", "RetroFit2.0 :RetroGetResult: " + result);


                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");

                    if(status.equals("0")){
                        String msg = jsonObject.getString("msg");

                    }else{

                    }



                }catch (Exception e){

                }
                }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


}