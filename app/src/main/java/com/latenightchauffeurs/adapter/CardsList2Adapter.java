package com.latenightchauffeurs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.modelItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class CardsList2Adapter extends RecyclerView.Adapter
{
    private static final String TAG = "CardsList2Adapter";
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public CardsList2Adapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode)
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
                ((InboxViewHolder) holder).root.setTag(singleTripData.getMapMain());
                //((InboxViewHolder) holder).viewmap.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).delete.setVisibility(View.GONE);

                if (detailMap.containsKey("token") && !detailMap.get("token").toString().equalsIgnoreCase(""))
                {
                    String mask = detailMap.get("token").toString().replaceAll("\\w(?=\\w{4})", "*");
                    ((InboxViewHolder) holder).cnumber.setText(""+mask);
                }



//                ((CardsListAdapter.InboxViewHolder) holder).emonth.setVisibility(View.VISIBLE);
//
//                Log.e(TAG, "AAAA "+detailMap.get("expiry").toString());


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

                ((InboxViewHolder) holder).root.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        HashMap<String, Object> dmap = new HashMap<>();
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        if(Utils.global.mapData.size()>0)
                        {
                            for(Map.Entry<String, Object> entry:Utils.global.mapData.entrySet())
                            {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if(!value.toString().equalsIgnoreCase("null"))
                                {
                                    dmap.put(key, value);
                                }
                            }
                        }

                        if (Utils.global.mapData.size()>0)
                        {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("map",(Serializable)dmap);
                            ((Activity)mcontext).setResult(Activity.RESULT_OK,returnIntent);
                            ((Activity)mcontext).finish();

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