package com.latenightchauffeurs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.ViewAvailableRide;
import com.latenightchauffeurs.fragment.Cards;
import com.latenightchauffeurs.model.modelItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class CardsListAdapter extends RecyclerView.Adapter
{
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public CardsListAdapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode)
    {
        adapterList = 	students;
        itemLayout 	= 	layout;
        adapterMode	=	mode;
        mcontext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder vh;

            View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent,false);
            vh = new InboxViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof InboxViewHolder)
        {
            modelItem singleTripData = (modelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            //Utils.e("paymentslistadapter 107", "" + detailMap);

            try
            {
                ((InboxViewHolder) holder).delete.setTag(singleTripData.getMapMain());
                //((InboxViewHolder) holder).viewmap.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).delete.setVisibility(View.VISIBLE);

                if (detailMap.containsKey("token") && !detailMap.get("token").toString().equalsIgnoreCase(""))
                {
                    String number = detailMap.get("token").toString();
                    String mask = number.replaceAll("\\w(?=\\w{4})", "*");


                    ((InboxViewHolder) holder).cnumber.setText(""+mask);
                }


                if(detailMap.containsKey("expiry")){
                    if(detailMap.get("expiry").toString().length() >= 2){
                        String cc = detailMap.get("expiry").toString().substring(0, 2);
                        ((InboxViewHolder) holder).emonth.setText(cc);
                    }

                    if(detailMap.get("expiry").toString().length() >= 4){
                        String substring = detailMap.get("expiry").toString().substring(Math.max(detailMap.get("expiry").toString().length() - 2, 0));
                        ((InboxViewHolder) holder).eyear.setText(substring);
                    }
                }

//                if (detailMap.containsKey("exp_month") && !detailMap.get("exp_month").toString().equalsIgnoreCase(""))
//                {
//                    ((InboxViewHolder) holder).emonth.setText(detailMap.get("exp_month").toString());
//                }
//
//                if (detailMap.containsKey("exp_year") && !detailMap.get("exp_year").toString().equalsIgnoreCase(""))
//                {
//                    ((InboxViewHolder) holder).eyear.setText(detailMap.get("exp_year").toString());
//                }

                ((InboxViewHolder) holder).delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        if(Utils.global.mapData.size()>0)
                        {
                            Cards.Instance.showDeleteDilog("Do you want to delete.",Utils.global.mapData);

                        }
                    }
                });
            }
            catch (Exception e)
            {
               // Utils.e(Tag+"180","Exception=====Exception======Exception ::   "+e.getMessage());
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).hotel= singleTripData;
        }
    }

    @Override
    public int getItemCount()
    {
        return adapterList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder
    {
        //Notification
        public modelItem hotel;
        public View gView;
        //***

        public TextView cnumber,emonth,eyear;
        ImageView delete;
        public RelativeLayout root;
        public InboxViewHolder(View v)
        {
            super(v);
            try
            {
                cnumber=(TextView) v.findViewById(R.id.cnumber);
                emonth=(TextView) v.findViewById(R.id.emonth);
                eyear=(TextView) v.findViewById(R.id.eyear);
                delete=(ImageView) v.findViewById(R.id.delete);
                root=(RelativeLayout) v.findViewById(R.id.rowitem_root);
            }
            catch (Exception e)
            {
                //Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }

    private static String getColoredSpanned(String text, String color)
    {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}