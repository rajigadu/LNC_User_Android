package com.latenightchauffeurs.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.activity.AddAddress;
import com.latenightchauffeurs.adapter.DropAddressAdapter2;
import com.latenightchauffeurs.adapter.PlaceArrayAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DropAddressList extends Fragment {
    Button addDropButton;

    public static RecyclerView rv_loc;
    public static DropAddressList Instance;
    public static Context mcontext;
    public static DropAddressAdapter2 requestsAdapter;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_droplist, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(final View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        rv_loc = (RecyclerView) v.findViewById(R.id.rv_loc);
        addDropButton = (Button) v.findViewById(R.id.add_drop);

        Instance = this;
        mcontext = getContext();

        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));

        MyrequestsListRequest();

        addDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext, AddAddress.class);
                i.putExtra("type", "2");
                startActivity(i);
            }
        });

      /*  for (int i=0;i<5;i++)
        {
            map=new HashMap<>();
            map.put("fname","c"+String.valueOf(i));
            stopsList.add(map);
        }*/

       /* if (Utils.global.dropAddresslist!=null&&Utils.global.dropAddresslist.size()>0)
            loadRequestsList(mContext,Utils.global.dropAddresslist,"");*/
    }

    public void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        map = new HashMap<>();
        map.put("userid", id);
        map.put("type", "2");
        OnlineRequest.getDropOffAddressesRequest(mcontext, map);
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
        requestsAdapter = new DropAddressAdapter2(mcontext, requestsList, rv_loc, R.layout.drop_rowitem, ConstVariable.DropOffAddressList);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }

    public void deleteOREditDropAddress(HashMap<String, Object> dmap, Integer type) {
        if (type == 1) {
            map = new HashMap<>();
            map.put("id", dmap.get("id").toString());
            OnlineRequest.deleteDropAddressRequest(mcontext, map);
        } else if (type == 2) {
            Intent i = new Intent(mcontext, AddAddress.class);
            i.putExtra("map", (Serializable) dmap);
            mcontext.startActivity(i);
        }
    }

    public void showMessage(String dlgText, final HashMap<String, Object> data, final Integer type) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog5, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(mcontext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        // final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView textView = (TextView) dialog.findViewById(R.id.desc);
        final Button attend = (Button) dialog.findViewById(R.id.attend);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        textView.setText(dlgText);
        title.setText(R.string.app_name);

        if (type == 1) {
            attend.setText("Delete");
        } else if (type == 2) {
            attend.setText("Update");
        }

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    dialog.cancel();
                    deleteOREditDropAddress(data, type);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
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
}