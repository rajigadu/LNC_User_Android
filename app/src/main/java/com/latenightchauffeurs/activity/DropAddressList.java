package com.latenightchauffeurs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.adapter.DropAddressAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by narayana on 10/25/2017.
 */

public class DropAddressList extends AppCompatActivity {
    public static RecyclerView rv_loc;
    Button addDropButton;
    ImageView back;
    TextView title;
    public static DropAddressList Instance;
    public static Context mcontext;
    public static DropAddressAdapter requestsAdapter;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_droplist);
        super.onCreate(savedInstanceState);

        rv_loc = (RecyclerView) findViewById(R.id.rv_loc);
        addDropButton = (Button) findViewById(R.id.add_drop);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);

        title.setText("Drop Address List");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mcontext, AddAddress.class);
                i.putExtra("type", "1");
                startActivity(i);
            }
        });


        Instance = this;
        mcontext = this;

        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));

        dropAddressRequest();
      /*  for (int i=0;i<5;i++)
        {
            map=new HashMap<>();
            map.put("fname","c"+String.valueOf(i));
            stopsList.add(map);
        }*/

       /* if (Utils.global.dropAddresslist!=null&&Utils.global.dropAddresslist.size()>0)
        loadRequestsList(mContext,Utils.global.dropAddresslist,"");*/
    }

    public void dropAddressRequest() {
        MyrequestsListRequest();
    }

    public void MyrequestsListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        map = new HashMap<>();
        map.put("userid", id);
        map.put("type", "1");
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
        requestsAdapter = new DropAddressAdapter(mcontext, requestsList, rv_loc, R.layout.drop_rowitem, ConstVariable.DropOffAddressList);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }
}
