package com.latenightchauffeurs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.fragment.DropAddressList;
import com.latenightchauffeurs.model.modelItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class DropAddressAdapter2 extends RecyclerView.Adapter
{
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public DropAddressAdapter2(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode)
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,int position)
    {
        if (holder instanceof InboxViewHolder)
        {
            modelItem singleTripData = (modelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            Utils.e("paymentslistadapter 107", "" + detailMap);

            try
            {
                ((InboxViewHolder) holder).delete.setVisibility(View.VISIBLE);
                ((InboxViewHolder) holder).edit.setVisibility(View.VISIBLE);

                ((InboxViewHolder) holder).root.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).delete.setTag(singleTripData.getMapMain());
                ((InboxViewHolder) holder).edit.setTag(singleTripData.getMapMain());

                if(detailMap.containsKey("street_address") && !detailMap.get("street_address").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).street.setText(detailMap.get("street_address").toString());
                }

                if (detailMap.containsKey("address_type") && !detailMap.get("address_type").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).type.setText(detailMap.get("address_type").toString());
                }

                if (detailMap.containsKey("city") && !detailMap.get("city").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).city.setText(detailMap.get("city").toString());
                }

                if (detailMap.containsKey("state") && !detailMap.get("state").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).state.setText(detailMap.get("state").toString());
                }

                if (detailMap.containsKey("zipcode") && !detailMap.get("zipcode").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).zip.setText(detailMap.get("zipcode").toString());
                }

                if (detailMap.containsKey("notes") && !detailMap.get("notes").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).address_more.setText(detailMap.get("notes").toString());
                }

                ((InboxViewHolder) holder).root.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        if (Utils.global.mapData.size()>0)
                        {
                            HashMap<String,Object> dmap=new HashMap<>();

                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet())
                            {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null"))
                                {
                                    dmap.put(key,value);
                                }
                            }
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("map",(Serializable)dmap);
                            ((Activity)mcontext).setResult(Activity.RESULT_OK,returnIntent);
                            ((Activity)mcontext).finish();
                        }
                    }
                });

                ((InboxViewHolder) holder).delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        if (Utils.global.mapData.size()>0)
                        {
                            DropAddressList.Instance.showMessage("Do you want to delete!",Utils.global.mapData,1);
                        }
                    }
                });

                ((InboxViewHolder) holder).edit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        HashMap<String,Object> dmap=new HashMap<>();

                        if (Utils.global.mapData.size()>0)
                        {
                            for (Map.Entry<String, Object> entry : Utils.global.mapData.entrySet())
                            {
                                String key = entry.getKey();
                                Object value = entry.getValue();

                                if (!value.toString().equalsIgnoreCase("null"))
                                {
                                    dmap.put(key,value);
                                }
                            }
                            DropAddressList.Instance.showMessage("Do you want to edit!",dmap,2);
                        }
                    }
                });


           }
            catch (Exception e)
            {
                Utils.e(Tag+"180","Exception======================Exception======================Exception");
                e.printStackTrace();
            }
            ((InboxViewHolder) holder).hotel= singleTripData;
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

    public static class InboxViewHolder extends RecyclerView.ViewHolder
    {
        //Notification
        public modelItem hotel;
        public View gView;
        //***

        public TextView street,type,city,state,zip,address_more;
        public CardView root;
        public ImageView delete,edit;
        public InboxViewHolder(View v)
        {
            super(v);
            try
            {
                street=(TextView) v.findViewById(R.id.street);
                type=(TextView) v.findViewById(R.id.type);
                city=(TextView) v.findViewById(R.id.city);
                state=(TextView) v.findViewById(R.id.state);
                delete=(ImageView) v.findViewById(R.id.delete);
                edit=(ImageView) v.findViewById(R.id.edit);
                zip=(TextView) v.findViewById(R.id.zip);
                address_more=(TextView) v.findViewById(R.id.street_2);
                root=(CardView) v.findViewById(R.id.rowitem_root);
            }
            catch (Exception e)
            {
                Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }
}