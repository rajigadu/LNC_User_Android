package com.latenightchauffeurs.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.modelItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class BookingsAdapter extends RecyclerView.Adapter
{
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public BookingsAdapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode)
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
            vh = new AdapterViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof AdapterViewHolder)
        {
            modelItem singleTripData = (modelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
           // Utils.e("paymentslistadapter 107", "" + detailMap);

            try
            {
                ((AdapterViewHolder) holder).root.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("date") && !detailMap.get("date").toString().equalsIgnoreCase(""))
                {
                   /* String d="",t="";

                    if (detailMap.containsKey("date") && !detailMap.get("date").toString().equalsIgnoreCase(""))
                    d =  getColoredSpanned(detailMap.get("date").toString(), "#800000");

                    if (detailMap.containsKey("time") && !detailMap.get("time").toString().equalsIgnoreCase(""))
                        t =  getColoredSpanned(detailMap.get("time").toString(), "#800000");*/

                    ((AdapterViewHolder) holder).date.setText(Html.fromHtml(detailMap.get("date").toString()));

                }

                if (detailMap.containsKey("ridecount") && !detailMap.get("ridecount").toString().equalsIgnoreCase(""))
                {
                    ((AdapterViewHolder) holder).persons.setText(detailMap.get("ridecount").toString());
                }

                if (detailMap.containsKey("distance") && !detailMap.get("distance").toString().equalsIgnoreCase(""))
                {
                    ((AdapterViewHolder) holder).distance.setText(String.format("%.2f",Double.valueOf(detailMap.get("distance").toString()))+" mi");
                }

                if (detailMap.containsKey("slocation") && !detailMap.get("slocation").toString().equalsIgnoreCase(""))
                {
                    String s="",e="";

                    if (detailMap.containsKey("slocation") && !detailMap.get("slocation").toString().equalsIgnoreCase(""))
                        s =  getColoredSpanned(detailMap.get("slocation").toString(), "#800000");

                    if (detailMap.containsKey("mlocation") && !detailMap.get("mlocation").toString().equalsIgnoreCase(""))
                        e =  getColoredSpanned(detailMap.get("mlocation").toString(), "#F69625");

                    ((AdapterViewHolder) holder).ride.setText(Html.fromHtml(s+"<br />  to  <br />" +e));
                }

                ((AdapterViewHolder) holder).root.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        if (Utils.global.mapData.size()>0)
                        {
                            /*Intent i=new Intent(mContext,ViewRideDetails.class);
                            i.putExtra("map",(Serializable) Utils.global.mapData);
                            //i.putExtra("userid",Utils.global.mapData.get("userid").toString());
                            mContext.startActivity(i);*/
                        }
                    }
                });
            }
            catch (Exception e)
            {
               // Utils.e(Tag+"180","Exception======================Exception======================Exception");
                e.printStackTrace();
            }

            ((AdapterViewHolder) holder).hotel= singleTripData;
        }
        else
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return adapterList.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder
    {
        //Notification
        public modelItem hotel;
        public View gView;
        //***

        public TextView date,persons,ride,distance;
        public RelativeLayout root;
        public AdapterViewHolder(View v)
        {
            super(v);
            try
            {
                date=(TextView) v.findViewById(R.id.date);
                persons=(TextView) v.findViewById(R.id.persons);
                ride=(TextView) v.findViewById(R.id.ride);
                distance=(TextView) v.findViewById(R.id.distance);
                root=(RelativeLayout) v.findViewById(R.id.rowitem_root);
            }
            catch (Exception e)
            {
               // Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
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