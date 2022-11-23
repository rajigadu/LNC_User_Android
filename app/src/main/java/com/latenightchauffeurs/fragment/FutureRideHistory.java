package com.latenightchauffeurs.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.FutureRideHistoryAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class FutureRideHistory extends Fragment {
    static RecyclerView rv_loc;

    public static Context mcontext;
    public static FutureRideHistoryAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();
    public static FutureRideHistory Instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            loadRequestsList(mcontext,stopsList,"");
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
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_FUTURE_RIDE_HISTORY);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.FutureRideHistory);
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
                //Utils.e(TAG+"122","Exception======================Exception======================Exception");
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
        requestsAdapter = new FutureRideHistoryAdapter(mcontext, requestsList, rv_loc, R.layout.history_ride_rowitem, ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    public void showPaymentSummaryDilog(final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog9, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView tid = (TextView) dialog.findViewById(R.id.tid);
        final TextView baseprice = (TextView) dialog.findViewById(R.id.baseprice);
        final TextView tip = (TextView) dialog.findViewById(R.id.tip);
        final TextView famount = (TextView) dialog.findViewById(R.id.fprice);
        final Button ok = (Button) dialog.findViewById(R.id.ok);

        title.setText(R.string.app_name);

        Integer finalAmount = 0;

        if (data != null && data.size() > 0) {
            if (data.containsKey("transaction_id") && !data.get("transaction_id").toString().equalsIgnoreCase("") && !data.get("transaction_id").toString().equalsIgnoreCase("null")) {
                tid.setText(data.get("transaction_id").toString());
            }

            if (data.containsKey("amount") && !data.get("amount").toString().equalsIgnoreCase("") && !data.get("amount").toString().equalsIgnoreCase("null")) {
                baseprice.setText(data.get("amount").toString() + " $");
                finalAmount = finalAmount + Integer.parseInt(data.get("amount").toString());
            }

            if (data.containsKey("tip_amount") && !data.get("tip_amount").toString().equalsIgnoreCase("null") && !data.get("tip_amount").toString().equalsIgnoreCase("")) {
                tip.setText(data.get("tip_amount").toString() + " $");
                finalAmount = finalAmount + Integer.parseInt(data.get("tip_amount").toString());
            }
            if (finalAmount != 0) {
                famount.setText(String.valueOf(finalAmount) + " $");
            }
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
        map.put("ride_id", smap.get("id"));
        OnlineRequest.paymentSummaryRequest(mcontext, map);
    }
}