package com.latenightchauffeurs.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.modelItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class ServiceInfoAdapter extends RecyclerView.Adapter
{
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public ServiceInfoAdapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode)
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
           // Utils.e("paymentslistadapter 107", "" + detailMap);

            try
            {
                ((InboxViewHolder) holder).root.setTag(singleTripData.getMapMain());

                if (detailMap.containsKey("location") && !detailMap.get("location").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).name.setText(detailMap.get("location").toString());
                }

                if (detailMap.containsKey("typ") && !detailMap.get("typ").toString().equalsIgnoreCase(""))
                {
                    if (detailMap.containsKey("typ") && detailMap.get("typ").toString().equalsIgnoreCase("Available"))
                    {
                        //((InboxViewHolder) holder).status.setText(detailMap.get("location").toString());
                    }
                    else
                    {
                        ((InboxViewHolder) holder).status.setText(detailMap.get("typ").toString());
                    }
                }

                ((InboxViewHolder) holder).root.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        if (Utils.global.mapData.size()>0)
                        {

                            if (Utils.global.mapData.containsKey("typ") && Utils.global.mapData.get("typ").toString().equalsIgnoreCase("Available"))
                            {
                                Utils.userLocationRequest(mcontext, Utils.global.mapData.get("id").toString(), "2");
                            }

                        }
                    }
                });
            }
            catch (Exception e)
            {
               // Utils.e(Tag+"180","Exception======================Exception======================Exception");
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

        public TextView name,status;
        public CardView root;
        public InboxViewHolder(View v)
        {
            super(v);
            try
            {
                name=(TextView) v.findViewById(R.id.name);
                status=(TextView) v.findViewById(R.id.status);
                root=(CardView) v.findViewById(R.id.rowitem_root);
            }
            catch (Exception e)
            {
                //Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }
}