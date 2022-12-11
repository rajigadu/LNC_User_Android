package com.latenightchauffeurs.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.AddStops;
import com.latenightchauffeurs.model.modelItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class StopsAddressAdapter extends RecyclerView.Adapter {
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;

    public StopsAddressAdapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof InboxViewHolder) {
            modelItem singleTripData = (modelItem) adapterList.get(position);

            final HashMap<String, Object> detailMap = singleTripData.getMapMain();
            Utils.e("paymentslistadapter 107", "" + detailMap.toString());

            try {
                if (adapterMode == ConstVariable.StopLocations) {
                    ((InboxViewHolder) holder).delete.setVisibility(View.GONE);
                } else {
                    ((InboxViewHolder) holder).delete.setVisibility(View.VISIBLE);
                }

                ((InboxViewHolder) holder).delete.setTag(position);

                if (detailMap.containsKey("location") && !detailMap.get("location").toString().equalsIgnoreCase("")) {
                    ((InboxViewHolder) holder).location.setText(detailMap.get("location").toString());
                }

                ((InboxViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int removeIndex = (int) v.getTag();
                        adapterList.remove(removeIndex);
                        AddStops.stopsList.remove(removeIndex);
                        notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                Utils.e(Tag + "180", "Exception======================Exception======================Exception");
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

        public TextView location;
        public ImageView delete;
        public CardView root;

        public InboxViewHolder(View v) {
            super(v);
            try {
                location = (TextView) v.findViewById(R.id.location);
                delete = (ImageView) v.findViewById(R.id.delete);
                root = (CardView) v.findViewById(R.id.rowitem_root);
            } catch (Exception e) {
                Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }
}