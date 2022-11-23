package com.latenightchauffeurs.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

public class PaymentHistoryAdapter extends RecyclerView.Adapter
{
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public PaymentHistoryAdapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode)
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

                if (detailMap.containsKey("payment_date") && !detailMap.get("payment_date").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).date.setText(detailMap.get("payment_date").toString());
                }

                if (detailMap.containsKey("transaction_id") && !detailMap.get("transaction_id").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).payId.setText(detailMap.get("transaction_id").toString());
                }

                if (detailMap.containsKey("amount") && !detailMap.get("amount").toString().equalsIgnoreCase(""))
                {
                    ((InboxViewHolder) holder).amount.setText(detailMap.get("amount").toString());
                }

                ((InboxViewHolder) holder).root.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Utils.global.mapData = (HashMap<String,Object>) v.getTag();

                        if (Utils.global.mapData.size()>0)
                        {
                            /*Navigation.nid=0;
                            Intent i=new Intent(mContext,Test.class);
                            i.putExtra("map",(Serializable) Utils.global.mapData);
                            mContext.startActivity(i);*/
                           /* Navigation.Instance.getFragment(0);
                            Intent broadcast = new Intent();
                            broadcast.setAction("OPEN_NEW_ACTIVITY");
                            broadcast.putExtra("status", "1");
                            broadcast.putExtra("location",Utils.global.mapData.get("location").toString() );
                            broadcast.putExtra("latitude",Utils.global.mapData.get("latitude").toString());
                            broadcast.putExtra("longitude",Utils.global.mapData.get("longitude").toString());
                            mcontext.sendBroadcast(broadcast);*/
                            /*SavePref pref1 = new SavePref();
                            pref1.SavePref(mcontext);
                            pref1.setClocation(Utils.global.mapData.get("location").toString());
                            pref1.setlocationlat(Utils.global.mapData.get("latitude").toString());
                            pref1.setlocationlng(Utils.global.mapData.get("longitude").toString());
                            ServiceInfo.Instance.DataFromoneTotwo();*/

                            //Utils.userLocationRequest(mcontext,Utils.global.mapData.get("id").toString(),"2");

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

        public TextView date,payId,amount;
        public CardView root;
        public InboxViewHolder(View v)
        {
            super(v);
            try
            {
                date=(TextView) v.findViewById(R.id.date);
                payId=(TextView) v.findViewById(R.id.pid);
                amount=(TextView) v.findViewById(R.id.amount);
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