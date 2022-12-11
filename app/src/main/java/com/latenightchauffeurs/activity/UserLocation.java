package com.latenightchauffeurs.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.UserLocationAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserLocation extends AppCompatActivity
{
    static RecyclerView rv_loc;
    public static UserLocation Instance;

    public static Context mcontext;
    public static UserLocationAdapter requestsAdapter;
    public static RecyclerView.LayoutManager requestsListMan;
    public static List<modelItem> requestsList;
    public HashMap<String, Object> map;
    public List<HashMap<String,Object>> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlocation);

        rv_loc=(RecyclerView) findViewById(R.id.rv_loc);
        mcontext=UserLocation.this;
        Instance=UserLocation.this;

         //Utils.global.locationIdentity=1;

        requestsListMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL,false);
        rv_loc.setHasFixedSize(true);
        rv_loc.setLayoutManager(requestsListMan);
        MyrequestsListRequest();
    }

    public static  void MyrequestsListRequest()
    {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("userid",id);
        Utils.global.mapMain.put("type","3");
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_SERVICE_INFO);

        if(Utils.isNetworkAvailable(mcontext))
        {
            JsonPost.getNetworkResponse(mcontext,null,Utils.global.mapMain,ConstVariable.ServiceInfo);
        }
        else
        {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public static  void loadRequestsList(Context context,List<HashMap<String, Object>> viewList, String mode)
    {
        // Utils.e(TAG+"81", "load browseMembersList Data"+mode+viewList);
        if (viewList != null && viewList.size() > 0)
        {
            try
            {
                //Utils.e(TAG+"88", "browseMembersList new");
                requestsList = new ArrayList<modelItem>();

                //Utils.e(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++)
                {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if(!requestsList.contains(mp))
                    {
                        requestsList.add(new modelItem(mp));
                    }
                }
                //Utils.e(TAG+"118", "browseMembersList"+eventList.size());
            }
            catch (Exception e)
            {
                //Utils.e(TAG+"122","Exception======================Exception======================Exception");
                e.printStackTrace();
            }
            finally
            {
                // Utils.e(TAG+"127", "browseMembersList"+eventList.size());
                //Utils.e(TAG+"128", "ok");
                if(!mode.equalsIgnoreCase("update"))
                {
                    setAdapterFriendsRequestList(context);
                }
                else
                {
                    requestsAdapter.notifyItemInserted(requestsList.size());
                    requestsAdapter.notifyDataSetChanged();
                }
            }
            requestsListMan.setAutoMeasureEnabled(true);
        }
        else
        {
            rv_loc.setVisibility(View.GONE);
            //noData.setVisibility(View.VISIBLE);
        }
    }

    public static void setAdapterFriendsRequestList(final Context context)
    {
        if(rv_loc != null)
        {
            rv_loc.setVisibility(View.VISIBLE);
            //noData.setVisibility(View.GONE);
        }

        //Utils.e(TAG+"156", "setAdapter ok "+eventList);
        requestsAdapter = new UserLocationAdapter(mcontext,requestsList,rv_loc,R.layout.service_rowitem,ConstVariable.Login);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_loc.setAdapter(requestsAdapter);
    }
    @Override
    public void onBackPressed()
    {
        finishAffinity();
        super.onBackPressed();
    }
}
