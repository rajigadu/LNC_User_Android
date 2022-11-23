package com.latenightchauffeurs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2/18/2017.
 */

public class FutureChatAdapter extends RecyclerView.Adapter {
    private List<modelItem> adapterList;
    String Tag = "GroupAdapter";
    private int itemLayout;
    public static int adapterMode;
    public Context mcontext;
    String userId, did;

    public FutureChatAdapter(Context context, List<modelItem> students, RecyclerView recyclerView, int layout, int mode) {
        adapterList = students;
        itemLayout = layout;
        adapterMode = mode;
        mcontext = context;

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        userId = pref1.getUserId();
        did = pref1.getDriverId();
    }

    @Override
    public int getItemViewType(int position) {
        modelItem singleTripData = adapterList.get(position);
        HashMap<String, Object> detailMap = singleTripData.getMapMain();
        boolean isMyId = (detailMap.containsKey("sender") && detailMap.get("sender").toString().equalsIgnoreCase(userId));
        return isMyId ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        int viewRes = viewType == 0 ? R.layout.chat_rowitem_right_own : R.layout.chat_rowitem_left_other;
        View v = LayoutInflater.from(parent.getContext()).inflate(viewRes, parent, false);
        vh = new InboxViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatAdapter.InboxViewHolder) {
            modelItem singleTripData = adapterList.get(position);
            HashMap<String, Object> detailMap = singleTripData.getMapMain();

            try {

                Utils.setImagePiccaso(mcontext, Settings.URLIMAGEBASE + detailMap.get("profileImageSender").toString(),
                        ((ChatAdapter.InboxViewHolder) holder).right_img);
                ((ChatAdapter.InboxViewHolder) holder).right_msg.setText(detailMap.get("mesage").toString());

                ((ChatAdapter.InboxViewHolder) holder).right_dt.setText(detailMap.get("date").toString());

                //Utils.setImagePiccaso(mcontext, Settings.URLIMAGEBASE + detailMap.get("profileImageSender").toString(), ((InboxViewHolder) holder).left_img);
                //((InboxViewHolder) holder).rlLeftChat.setTag(singleTripData.getMapMain());
                // ((InboxViewHolder) holder).rlRightChat.setTag(singleTripData.getMapMain());


                /*if (detailMap.containsKey("sender") && !detailMap.get("sender").toString().equalsIgnoreCase("")) {
                    if (detailMap.containsKey("sender") && detailMap.get("sender").toString().equalsIgnoreCase(did)) {
                        ((InboxViewHolder) holder).rlLeftChat.setVisibility(View.VISIBLE);
                        ((InboxViewHolder) holder).rlRightChat.setVisibility(View.GONE);

                        if (detailMap.containsKey("profileImageSender") && detailMap.get("profileImageSender") != null) {
                            Utils.setImagePiccaso(mcontext, Settings.URLIMAGEBASE + detailMap.get("profileImageSender").toString(), ((InboxViewHolder) holder).left_img);
                        }

                        if (detailMap.containsKey("mesage") && !detailMap.get("mesage").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).left_msg.setText(detailMap.get("mesage").toString());
                        }

                        if (detailMap.containsKey("date") && !detailMap.get("date").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).left_dt.setText(detailMap.get("date").toString());
                        }
                    } else if (detailMap.containsKey("sender") && detailMap.get("sender").toString().equalsIgnoreCase(id)) {
                        ((InboxViewHolder) holder).rlLeftChat.setVisibility(View.GONE);
                        ((InboxViewHolder) holder).rlRightChat.setVisibility(View.VISIBLE);

                        if (detailMap.containsKey("profileImageSender") && detailMap.get("profileImageSender") != null) {
                            Utils.setImagePiccaso(mcontext, Settings.URLIMAGEBASE + detailMap.get("profileImageSender").toString(), ((InboxViewHolder) holder).right_img);
                        }
                        if (detailMap.containsKey("mesage") && !detailMap.get("mesage").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).right_msg.setText(detailMap.get("mesage").toString());
                        }

                        if (detailMap.containsKey("date") && !detailMap.get("date").toString().equalsIgnoreCase("null")) {
                            ((InboxViewHolder) holder).right_dt.setText(detailMap.get("date").toString());
                        }
                    }
                }*/
            } catch (Exception e) {
                Utils.e(Tag + "180", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }

            ((ChatAdapter.InboxViewHolder) holder).hotel = singleTripData;
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

        public TextView left_dt, right_dt, left_msg, right_msg;
        public LinearLayout root;
        public ImageView left_img, right_img;
        public RelativeLayout left, right;

        public InboxViewHolder(View v) {
            super(v);
            try {
                // left_img = (ImageView) v.findViewById(R.id.img_left);
                right_img = (ImageView) v.findViewById(R.id.img_right);
                // left_msg = (TextView) v.findViewById(R.id.msg_left);
                right_msg = (TextView) v.findViewById(R.id.msg_right);
                right_dt = (TextView) v.findViewById(R.id.right_dt);
                // left_dt = (TextView) v.findViewById(R.id.left_dt);
                // left = (RelativeLayout) v.findViewById(R.id.rl_left);
                right = (RelativeLayout) v.findViewById(R.id.rl_right);
            } catch (Exception e) {
                Utils.e("ProfileEventRecycle 212", "Exception======================Exception======================Exception");
                e.printStackTrace();
            }
        }
    }
}