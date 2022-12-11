package com.latenightchauffeurs.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.RideHistoryAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class RideHistory extends Fragment {
    static RecyclerView rv_loc;

    public static Activity mcontext;
    public static RideHistoryAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    public static RideHistory Instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_ridehistory, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        rv_loc = (RecyclerView) v.findViewById(R.id.rv_loc);

        mcontext = getActivity();
        Instance = this;

        requestsListMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);

        MyrequestsListRequest();

        /*for (int i=0;i<5;i++)
        {
            map=new HashMap<>();
            map.put("date","date "+String.valueOf(i));
            map.put("time","time "+String.valueOf(i));
            map.put("stops",String.valueOf(i));
            map.put("pick","pickup address "+String.valueOf(i));
            map.put("drop","drop off address "+String.valueOf(i));
            map.put("dist",String.valueOf(i*10));
            stopsList.add(map);
        }
        if (stopsList!=null&&stopsList.size()>0)
            loadRequestsList(mContext,stopsList,"");
*/
        super.onViewCreated(v, savedInstanceState);
    }

    public static void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_RIDE_HISTORY);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.RideHistory);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.e(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.e(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<modelItem>();

                //Utils.e(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if (!requestsList.contains(mp)) {
                        requestsList.add(new modelItem(mp));
                    }
                }
                //Utils.e(TAG+"118", "browseMembersList"+eventList.size());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Utils.e(TAG+"127", "browseMembersList"+eventList.size());
                //Utils.e(TAG+"128", "ok");
                if (!mode.equalsIgnoreCase("update")) {
                    setAdapterFriendsRequestList(context);
                } else {
                    requestsAdapter.notifyItemInserted(requestsList.size());
                    requestsAdapter.notifyDataSetChanged();
                }
            }
            requestsListMan.setAutoMeasureEnabled(true);
        } else {
            rv_loc.setVisibility(View.GONE);
            //noData.setVisibility(View.VISIBLE);
        }
    }

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rv_loc != null) {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        //Utils.e(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new RideHistoryAdapter(mcontext, requestsList, rv_loc, R.layout.history_ride_rowitem,
                ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    public void showPaymentSummaryDilog(final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog99, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        TextView title = dialog.findViewById(R.id.title);
        TextView tid = dialog.findViewById(R.id.tid);
        TextView baseprice = dialog.findViewById(R.id.baseprice);
        TextView tip = dialog.findViewById(R.id.tip);
        TextView famount = dialog.findViewById(R.id.fprice);
        TextView tvWaitingAmt = dialog.findViewById(R.id.tv_waitin_chg);
        Button ok = dialog.findViewById(R.id.ok);
        TextView unplan_wait_amt = dialog.findViewById(R.id.unplan_wait_amt);
        TextView unplan_stops_count = dialog.findViewById(R.id.unplan_stops_count);
        TextView plan_stops_count = dialog.findViewById(R.id.plan_stops_count);
        TextView admin_fee = dialog.findViewById(R.id.admin_fee_id);
        TextView tv_PromoAmount = dialog.findViewById(R.id.tv_PromoAmount);

        TextView fatitle = dialog.findViewById(R.id.fatitle);



        title.setText(R.string.app_name);




        Integer finalAmount = 2, finaCostAmt = 0;

        if (data != null && data.size() > 0) {
            if (data.containsKey("transaction_id") && !data.get("transaction_id").toString()
                    .equalsIgnoreCase("") && !data.get("transaction_id").toString()
                    .equalsIgnoreCase("null")) {
                tid.setText(data.get("transaction_id").toString());
            }//waiting_amt



//            if (data.containsKey("base_price") && !data.get("base_price").toString().equalsIgnoreCase
//                    ("") && !data.get("base_price").toString().equalsIgnoreCase("null")) {
//                finalAmount = finalAmount + Integer.parseInt(data.get("base_price").toString());
//                baseprice.setText(finalAmount + " $");
//            }


            if (data.containsKey("promo_amt") && !data.get("promo_amt").toString().equalsIgnoreCase
                    ("") && !data.get("promo_amt").toString().equalsIgnoreCase("null")) {
                // finalAmount = finalAmount + Integer.parseInt(data.get("base_price").toString());
                //baseprice.setText(finalAmount + " $");
                tv_PromoAmount.setText("$"+data.get("promo_amt").toString() + "");

            }else{
                tv_PromoAmount.setText("$0");

            }



            if (data.containsKey("base_price") && !data.get("base_price").toString().equalsIgnoreCase
                    ("") && !data.get("base_price").toString().equalsIgnoreCase("null")) {
               // finalAmount = finalAmount + Integer.parseInt(data.get("base_price").toString());
                //baseprice.setText(finalAmount + " $");
               // baseprice.setText("$"+data.get("base_price").toString() + "");

            }else{
                //baseprice.setText("$0");
            }



            if (data.containsKey("unplaned_waiting_amt") && !data.get("unplaned_waiting_amt").toString().equalsIgnoreCase
                    ("") && !data.get("unplaned_waiting_amt").toString().equalsIgnoreCase("null")) {
                unplan_wait_amt.setText("$"+data.get("unplaned_waiting_amt").toString() + "");

            }else{
                unplan_wait_amt.setText("$0");
            }

            if (data.containsKey("unplaned_stop_amt") && !data.get("unplaned_stop_amt").toString().equalsIgnoreCase
                    ("") && !data.get("unplaned_stop_amt").toString().equalsIgnoreCase("null")) {
                unplan_stops_count.setText("$"+data.get("unplaned_stop_amt").toString() + "");

            }else{
                unplan_stops_count.setText("$0");
            }


            if (data.containsKey("planed_stop_amt") && !data.get("planed_stop_amt").toString().equalsIgnoreCase
                    ("") && !data.get("planed_stop_amt").toString().equalsIgnoreCase("null")) {
                plan_stops_count.setText("$"+data.get("planed_stop_amt").toString() + "");

            }else{
                plan_stops_count.setText("$0");
            }


            if (data.containsKey("extra_charge") && !data.get("extra_charge").toString().equalsIgnoreCase
                    ("") && !data.get("extra_charge").toString().equalsIgnoreCase("null")) {
                admin_fee.setText("$"+data.get("extra_charge").toString() + "");

            }else{
                admin_fee.setText("$0");
            }



            if (data.containsKey("tip_amount") && !data.get("tip_amount").toString().equalsIgnoreCase
                    ("") && !data.get("tip_amount").toString().equalsIgnoreCase("null")) {
                tip.setText("$"+data.get("tip_amount").toString() + "");

            }else{
                tip.setText("$0");
            }


            double basePriseA = 0;
            double extra_charge = 0;

            try{
                basePriseA = Double.parseDouble(""+data.get("base_price").toString());

            }catch (Exception e){
            }


            try{
                extra_charge = Double.parseDouble(""+data.get("extra_charge").toString());
            }catch (Exception e){
            }


            int aa = (int) (basePriseA+extra_charge);

            baseprice.setText("$"+(aa));


//
//            if (data.containsKey("unplaned_waiting_amt") && !data.get("unplaned_waiting_amt").toString()
//                    .equalsIgnoreCase("") && !data.get("unplaned_waiting_amt").toString()
//                    .equalsIgnoreCase("null")) {
//                tvWaitingAmt.setText(data.get("unplaned_waiting_amt").toString() + " $");
//            }



//            if (data.containsKey("tip_amount") && !data.get("tip_amount").toString()
//                    .equalsIgnoreCase("null") &&
//                    !data.get("tip_amount").toString().equalsIgnoreCase("")) {
//                tip.setText(data.get("tip_amount").toString() + " $");
//                finaCostAmt = Integer.parseInt(data.get("tip_amount").toString());
//                //finalAmount = finalAmount + Integer.parseInt(data.get("tip_amount").toString());
//            } else {
//                tip.setText("0 $");
//            }

//            if (data.containsKey("total_fare") && !data.get("total_fare").toString()
//                    .equalsIgnoreCase("") && !data.get("total_fare").toString()
//                    .equalsIgnoreCase("null")) {
//                //famount.setText(String.valueOf(finalAmount) + " $");
//
//                famount.setText("$"+String.format("%d", Integer.valueOf(data.get("total_fare").toString()) + finaCostAmt) + "");
//



//
//            if (data.containsKey("total_fare") && !data.get("total_fare").toString().equalsIgnoreCase
//                    ("") && !data.get("total_fare").toString().equalsIgnoreCase("null")) {
//                famount.setText("$"+data.get("total_fare").toString() + "");
//
//            }else{
//                famount.setText("$0");
//            }



            if (data.containsKey("total_fare") && !data.get("total_fare").toString()
                    .equalsIgnoreCase("") && !data.get("total_fare").toString()
                    .equalsIgnoreCase("null")) {
                //famount.setText(String.valueOf(finalAmount) + " $");
//                famount.setText("$"+("%d"(data.get("total_fare").toString() ) + "");


              //  famount.setText("$"+String.format("%d", data.get("total_fare").toString()  + ""));

               //   famount.setText("$"+String.format("%d", Integer.valueOf(data.get("total_fare").toString()) + finaCostAmt) + "");
            }
        }


        LinearLayout linearLayout11 = (LinearLayout)dialog.findViewById(R.id.rl11);
        LinearLayout linearLayout22 = (LinearLayout)dialog.findViewById(R.id.rl22);
        LinearLayout linearLayout33 = (LinearLayout)dialog.findViewById(R.id.rl33);
        LinearLayout linearLayout44 = (LinearLayout)dialog.findViewById(R.id.rl44);
        LinearLayout linearLayout55 = (LinearLayout)dialog.findViewById(R.id.rl55);
        LinearLayout linearLayout66 = (LinearLayout)dialog.findViewById(R.id.rl66);
        LinearLayout linearLayout77 = (LinearLayout)dialog.findViewById(R.id.rl77);

        if(data.get("status").toString().equalsIgnoreCase("2")){
            fatitle.setText("Total Fare : ");
            linearLayout11.setVisibility(View.VISIBLE);
            linearLayout22.setVisibility(View.VISIBLE);
            linearLayout33.setVisibility(View.VISIBLE);
            linearLayout44.setVisibility(View.VISIBLE);
            linearLayout55.setVisibility(View.VISIBLE);
            linearLayout66.setVisibility(View.VISIBLE);
            linearLayout77.setVisibility(View.VISIBLE);
            famount.setText("$"+data.get("total_fare").toString());
        }else if(data.get("status").toString().equalsIgnoreCase("4")){
            fatitle.setText("Ride Cancellation Amount : ");
            linearLayout11.setVisibility(View.GONE);
            linearLayout22.setVisibility(View.GONE);
            linearLayout33.setVisibility(View.GONE);
            linearLayout44.setVisibility(View.GONE);
            linearLayout55.setVisibility(View.GONE);
            linearLayout66.setVisibility(View.GONE);
            linearLayout77.setVisibility(View.GONE);
            famount.setText("$"+data.get("ride_amt").toString());
        }



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    dialog.cancel();
                } catch (Exception e) {
                    // TODO: handle exception
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

    public void paymentSummaryRequest(HashMap<String, Object> smap) {
        map = new HashMap<>();
        map.put("ride_id", smap.get("rideid"));
        OnlineRequest.paymentSummaryRequest(mcontext, map);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(mcontext , "sdcsf" +requestCode, Toast.LENGTH_SHORT).show();

        if(requestCode == 12){
            MyrequestsListRequest();
        }


    }
}