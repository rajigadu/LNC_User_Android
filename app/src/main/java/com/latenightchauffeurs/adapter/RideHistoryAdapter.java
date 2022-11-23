package com.latenightchauffeurs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.AddTip;
import com.latenightchauffeurs.activity.Feedback;
import com.latenightchauffeurs.activity.Rating;
import com.latenightchauffeurs.activity.StopLocationsList;
import com.latenightchauffeurs.activity.ViewAvailableRide;
import com.latenightchauffeurs.fragment.RideHistory;
import com.latenightchauffeurs.model.modelItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class RideHistoryAdapter extends RecyclerView.Adapter {
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Activity mcontext;

    public RideHistoryAdapter(Activity context, List<modelItem> students, RecyclerView recyclerView,
                              int layout, int mode) {
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
            // Utils.e("paymentslistadapter 107", "" + detailMap);

            try {
                ((InboxViewHolder) holder).root.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).psummary.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).stops.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).btnFeedBack.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).btnAddTip.setTag(singleTripData.getMapMain());



//                if (detailMap.containsKey("promo_amt") && !detailMap.get("promo_amt").toString()
//                        .equalsIgnoreCase("")) {
//                    ((InboxViewHolder) holder).textViewPromo.setText("$"+detailMap.get("promo_amt").toString() + "");
//                }else{
//                    ((InboxViewHolder) holder).textViewPromo.setText("$0");
//                }


                if (detailMap.containsKey("promo_amt") && !detailMap.get("promo_amt").toString()
                        .equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).textViewPromo.setText("$"+detailMap.get("promo_amt").toString() + "");
                }else{
                    ((InboxViewHolder) holder).textViewPromo.setText("$0");
                }



                if (detailMap.containsKey("booking_type") && !detailMap.get("booking_type").toString()
                        .equalsIgnoreCase("")) {
                    if (detailMap.get("booking_type").toString().equalsIgnoreCase("1")) {
                        if (detailMap.containsKey("otherdate") && !detailMap.get("time").toString()
                                .equalsIgnoreCase("")) {
                            String ss = "", ee = "";

                            ss = "Date  :" + detailMap.get("otherdate").toString();
                            ee = "Time :" + detailMap.get("time").toString();

                            String s = ss
                                    + System.getProperty("line.separator")
                                    + ee
                                    + System.getProperty("line.separator");

                            ((InboxViewHolder) holder).date.setText(s);
                        }
                    } else {
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

                if (detailMap.containsKey("distance") && !detailMap.get("distance").toString()
                        .equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).distance.setText(String.format("%.2f",
                            Double.valueOf(detailMap.get("distance").toString())) + " mi");
                }

                if (detailMap.containsKey("transaction_id") && !detailMap.get("transaction_id").toString()
                        .equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).transactionId.setText(detailMap.get("transaction_id").toString());
                }

                if (detailMap.containsKey("status")) {
                    if (detailMap.get("status").toString().equalsIgnoreCase("2")) {
                        ((InboxViewHolder) holder).amount.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).waiting_charges.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).tipAmount.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).total_cost.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).cancel_amount.setVisibility(View.GONE);

                        ((InboxViewHolder) holder).title_amount.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).title_echarges.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).title_tip.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).title_totalcost.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).title_cancelamount.setVisibility(View.GONE);

                        ((InboxViewHolder) holder).title_promo_tile.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).textViewPromo.setVisibility(View.VISIBLE);


                        if (detailMap.containsKey("ride_amt") && !detailMap.get("ride_amt").toString()
                                .equalsIgnoreCase("")) {

                            double ride_amt = Double.parseDouble(detailMap.get("ride_amt").toString());
                            double extra_charge = Double.parseDouble(detailMap.get("extra_charge").toString());



                            ((InboxViewHolder) holder).amount.setText("$"+(ride_amt+extra_charge));
                        }

                        if (detailMap.containsKey("unplaned_waiting_amt") &&
                                !detailMap.get("unplaned_waiting_amt").toString().equalsIgnoreCase("") &&
                                !detailMap.get("unplaned_waiting_amt").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).waiting_charges.setText("$"+detailMap.get("unplaned_waiting_amt")
                                    .toString() + "");
                        }


                        if (detailMap.containsKey("feedback_status") &&
                                !detailMap.get("feedback_status").toString().equalsIgnoreCase("")
                            && !detailMap.get("feedback_status").toString().equalsIgnoreCase("0")
                                && !detailMap.get("feedback_status").toString().equalsIgnoreCase("null")) {
                           // ((InboxViewHolder) holder).tipAmount.setText(detailMap.get("tip_ammount").toString() + " $");
                            ((InboxViewHolder) holder).btnFeedBack.setVisibility(View.GONE);
                        } else {
                           // ((InboxViewHolder) holder).tipAmount.setText("0" + " $");
                            /*if (detailMap.get("tip_ammount").toString().equalsIgnoreCase("")
                                    && detailMap.get("tip_ammount").toString().equalsIgnoreCase("null") ||
                                    detailMap.get("tip_ammount") == null)*/
                            //((InboxViewHolder) holder).btnFeedBack.setVisibility(View.VISIBLE);
                            ((InboxViewHolder) holder).btnFeedBack.setVisibility(View.VISIBLE);
                        }



                        if (detailMap.containsKey("tip_ammount") &&
                                !detailMap.get("tip_ammount").toString().equalsIgnoreCase("")
                                && !detailMap.get("tip_ammount").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).tipAmount.setText("$"+detailMap.get("tip_ammount").toString() + "");
                            ((InboxViewHolder) holder).btnAddTip.setVisibility(View.GONE);
                        } else {
                            ((InboxViewHolder) holder).tipAmount.setText("$"+"0" + "");
                            /*if (detailMap.get("tip_ammount").toString().equalsIgnoreCase("")
                                    && detailMap.get("tip_ammount").toString().equalsIgnoreCase("null") ||
                                    detailMap.get("tip_ammount") == null)*/
                                //((InboxViewHolder) holder).btnFeedBack.setVisibility(View.VISIBLE);
                             ((InboxViewHolder) holder).btnAddTip.setVisibility(View.VISIBLE);
                        }

                        if (detailMap.containsKey("amount") && !detailMap.get("amount").toString()
                                .equalsIgnoreCase("")) {
                            Double t_cost = 0.0;

                            if (detailMap.containsKey("tip_ammount") && !detailMap.get("tip_ammount")
                                    .toString().equalsIgnoreCase("")
                                    && !detailMap.get("tip_ammount").toString().equalsIgnoreCase("null")) {
                                t_cost = Double.valueOf(detailMap.get("amount").toString()) +
                                        Double.valueOf(detailMap.get("tip_ammount").toString());
                            } else {
                                t_cost = Double.valueOf(detailMap.get("amount").toString());
                            }

                            ((InboxViewHolder) holder).total_cost.setText("$"+String.valueOf(t_cost) + "");
                        }
                    } else if (detailMap.get("status").toString().equalsIgnoreCase("4")) {
                        ((InboxViewHolder) holder).amount.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).waiting_charges.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).tipAmount.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).total_cost.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).cancel_amount.setVisibility(View.VISIBLE);

                        ((InboxViewHolder) holder).title_amount.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).title_echarges.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).title_tip.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).title_totalcost.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).title_cancelamount.setVisibility(View.VISIBLE);


                        ((InboxViewHolder) holder).title_promo_tile.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).textViewPromo.setVisibility(View.GONE);

                        if (detailMap.containsKey("ride_amt") && !detailMap.get("ride_amt").toString()
                                .equalsIgnoreCase("")) {
                            ((InboxViewHolder) holder).cancel_amount.setText("$"+detailMap.get("ride_amt").toString() + "");
                        }
                    }
                }

                if (detailMap.containsKey("pickup_address") &&
                        !detailMap.get("pickup_address").toString().equalsIgnoreCase("")) {
                    String s = "", e = "";

                    if (detailMap.containsKey("pickup_address") &&
                            !detailMap.get("pickup_address").toString().equalsIgnoreCase(""))
                        s = getColoredSpanned(detailMap.get("pickup_address").toString(), "#800000");

                    if (detailMap.containsKey("drop_address") &&
                            !detailMap.get("drop_address").toString().equalsIgnoreCase(""))
                        e = getColoredSpanned(detailMap.get("drop_address").toString(), "#F69625");

                   String PickUp = getColoredSpanned("PickUp: " , "#000000");
                    String DropOff = getColoredSpanned("DropOff: " , "#000000");

                    ((InboxViewHolder) holder).ride.setText(Html.fromHtml(PickUp+" "+s + "<br />  to  <br />" +DropOff+ " " + e ));
                }

                ((InboxViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            HashMap<String, Object> dmap = new HashMap<>();
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
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
                    }
                });

                ((InboxViewHolder) holder).psummary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            RideHistory.Instance.paymentSummaryRequest(Utils.global.mapData);
                        }
                    }
                });

                ((InboxViewHolder) holder).stops.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            HashMap<String, Object> dmap = new HashMap<>();
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }

                            //Utils.toastTxt("Still haven't implemented",mContext);
                            Intent i = new Intent(mcontext, StopLocationsList.class);
                            dmap.put("uType", "2");
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivity(i);
                        }
                    }
                });

                ((InboxViewHolder) holder).btnFeedBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            HashMap<String, Object> dmap = new HashMap<>();
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }

                            dmap.put("sType", "1");

                            //Utils.toastTxt("Still haven't implemented",mContext);
                            Intent i = new Intent(mcontext, Feedback.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivityForResult(i, 12);
                        }
                    }
                });



                ((InboxViewHolder) holder).btnAddTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.global.mapData = (HashMap<String, Object>) v.getTag();

                        if (Utils.global.mapData.size() > 0) {
                            HashMap<String, Object> dmap = new HashMap<>();
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null")) {
                                    dmap.put(key, value);
                                }
                            }

                            dmap.put("sType", "2");

                            //Utils.toastTxt("Still haven't implemented",mContext);
                            Intent i = new Intent(mcontext, AddTip.class);
                            i.putExtra("map", (Serializable) dmap);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mcontext.startActivityForResult(i, 12);
                        }
                    }
                });







            } catch (Exception e) {
                e.printStackTrace();
            }

            ((InboxViewHolder) holder).hotel = singleTripData;
        } else {

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
        public TextView date, ride, distance, amount, transactionId, waiting_charges, total_cost, cancel_amount, tipAmount;
        TextView title_amount, title_echarges, title_totalcost, title_cancelamount, title_tip, title_promo_tile, textViewPromo;
        public Button psummary, stops, btnFeedBack, btnAddTip;
        public RelativeLayout root;

        public InboxViewHolder(View v) {
            super(v);
            try {
                date = (TextView) v.findViewById(R.id.date);
                stops = (Button) v.findViewById(R.id.stops);
                ride = (TextView) v.findViewById(R.id.ride);
                amount = (TextView) v.findViewById(R.id.amount);
                title_amount = (TextView) v.findViewById(R.id.title_amount);
                title_echarges = (TextView) v.findViewById(R.id.title_wcharges);
                title_totalcost = (TextView) v.findViewById(R.id.title_totalcost);
                title_cancelamount = (TextView) v.findViewById(R.id.title_cancelamount);
                title_tip = (TextView) v.findViewById(R.id.title_tip);
                waiting_charges = (TextView) v.findViewById(R.id.waiting_charges);
                tipAmount = (TextView) v.findViewById(R.id.tip);
                total_cost = (TextView) v.findViewById(R.id.totalcost);
                cancel_amount = (TextView) v.findViewById(R.id.cancelamount);
                transactionId = (TextView) v.findViewById(R.id.trans);
                psummary = (Button) v.findViewById(R.id.psummary);
                distance = (TextView) v.findViewById(R.id.distance);
                root = (RelativeLayout) v.findViewById(R.id.rowitem_root);
                btnFeedBack = v.findViewById(R.id.btnFeedBack);
                btnAddTip = v.findViewById(R.id.btnAddTip);
                textViewPromo = v.findViewById(R.id.title_promo);
                title_promo_tile = v.findViewById(R.id.title_promo_tile);


            } catch (Exception e) {
                // Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }

    private static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}