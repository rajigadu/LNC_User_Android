package com.latenightchauffeurs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.ServiceApi;
import com.latenightchauffeurs.Utils.ServiceGenerator;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.ChatAdapter;
import com.latenightchauffeurs.model.ChatData;
import com.latenightchauffeurs.model.ChatPojo;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.StopsList;
import com.latenightchauffeurs.model.modelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manjeet on 8/23/2017.
 */

public class ActivityChat extends AppCompatActivity {
    public static String TAG = ActivityChat.class.getName();

    public static Context mcontext;
    public RecyclerView rv_chat;
    public static TextView noData;
    public ChatAdapter chatAdapter;
    public RecyclerView.LayoutManager chatMan;
    public List<modelItem> chatList;
    public HashMap<String, Object> map;
    public ImageView image;
    public TextView name;
    ImageView back;
    TextView title;
    EditText message;
    Button send;
    public static ActivityChat instance;
    public static HashMap<String, Object> dmap;
    private ArrayList<ChatData> chatListData;


    String backPress = "";


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandleMessageReceiver != null)
            unregisterReceiver(mHandleMessageReceiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        mcontext = this;

        setContentView(R.layout.activity_chat);
        rv_chat = findViewById(R.id.rv_chat);
        noData = findViewById(R.id.nodata);
        name = findViewById(R.id.name);
        back = findViewById(R.id.back);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        chatListData = new ArrayList<>();

        title.setVisibility(View.VISIBLE);
        title.setText("Chat");

        if (getIntent() != null) {
            dmap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
        }



        if (getIntent().hasExtra("back")) {
            backPress = getIntent().getStringExtra("back");
        }


        chatMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_chat.setHasFixedSize(true);
        rv_chat.setLayoutManager(chatMan);
        chatAdapter = new ChatAdapter(mcontext, chatListData);
        rv_chat.setAdapter(chatAdapter);
        rv_chat.setNestedScrollingEnabled(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent= new Intent(ActivityLogin.this,ActivityNavigation.class);
                //startActivity(intent);
                // handler.removeCallbacks(runnable);
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        mcontext = ActivityChat.this;

       // chatListRequest();

        //chatAdapter = new ChatAdapter(mcontext, chatList, rv_chat, R.layout.chat_rowitem_left_other, ConstVariable.Chat);

        if (Utils.isNetworkAvailable(ActivityChat.this)) {
            fetchMsgsList();
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        pref1.setisnewmsg("");
        registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
    }

    public void fetchMsgsList() {
        chatListData.clear();
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String did = "";
//        if (dmap != null)
//            did = dmap.get("id").toString();
//        else
//            did = pref1.getDriverId();


                if (dmap != null){
                    if(dmap.containsKey("driver_id")){
                        did = dmap.get("driver_id").toString();
                        Log.e(TAG, "AAAAA: "+did);
                    }
                    if(dmap.containsKey("id")){
                        did = dmap.get("id").toString();
                        Log.e(TAG, "BBBBB: "+did);
                    }
                }
                else{
                    did = pref1.getDriverId();
                    Log.e(TAG, "CCCCC: "+did);
                }


                Log.e(TAG, "id1: "+id);
                Log.e(TAG, "did1: "+did);


        // Utils.initiatePopupWindow(mcontext, "Please wait...");
        ServiceApi service = ServiceGenerator.createService(ServiceApi.class);
        Call<ChatPojo> response = service.fetchMsgList(id, did, "", "user");
        response.enqueue(new Callback<ChatPojo>() {
            @Override
            public void onResponse(Call<ChatPojo> call, Response<ChatPojo> response) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }

                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                    rv_chat.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    chatListData.addAll(response.body().getDataList());
                    chatAdapter.notifyDataSetChanged();
                    rv_chat.scrollToPosition(chatListData.size() - 1);
                } else {
                    rv_chat.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ChatPojo> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
            }
        });
    }




//    public void chatListRequest() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                SavePref pref1 = new SavePref();
//                pref1.SavePref(mcontext);
//                String id = pref1.getUserId();
//                String did = "";
//                if (dmap != null){
//                    did = dmap.get("id").toString();
//                    Log.e(TAG, "AAAAA: "+id);
//                }
//                else{
//                    did = pref1.getDriverId();
//                    Log.e(TAG, "BBBBB: "+id);
//                }
//
//
//
//
//
//                Log.e(TAG, "id1: "+id);
//                Log.e(TAG, "did1: "+did);
//
//
//                new Utils(mcontext);
//                Utils.global.mapMain();
//                Utils.global.mapMain.put("senderid", id);
//                Utils.global.mapMain.put("recieverid", did);
//                Utils.global.mapMain.put("msg", "");
//                Utils.global.mapMain.put("keyvalue", "user");
//                Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_CHAT);
//
//                if (Utils.isNetworkAvailable(mcontext)) {
//                    JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.ChatList);
//                } else {
//                    Utils.showInternetErrorMessage(mcontext);
//                }
//            }
//        });
//
//    }

    public void submit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SavePref pref1 = new SavePref();
                pref1.SavePref(mcontext);
                String id = pref1.getUserId();

                String did = "";



//                if (dmap != null)
//                    did = dmap.get("id").toString();
//                else
//                    did = pref1.getDriverId();


                if (dmap != null){
                    if(dmap.containsKey("driver_id")){
                        did = dmap.get("driver_id").toString();
                        Log.e(TAG, "AAAAA: "+did);
                    }
                    if(dmap.containsKey("id")){
                        did = dmap.get("id").toString();
                        Log.e(TAG, "BBBBB: "+did);
                    }
                }
                else{
                    did = pref1.getDriverId();
                    Log.e(TAG, "CCCCC: "+did);
                }


                Log.e(TAG, "id: "+id);
                Log.e(TAG, "did: "+did);

                new Utils(ActivityChat.this);

                if (message.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please enter message .", ActivityChat.this);
                } else if (message.getText().toString().length() > 250) {
                    Utils.toastTxt(" Maximum message length 250 characters.", ActivityChat.this);
                } else {
                    new Utils(ActivityChat.this);
                    Utils.global.mapMain();
                    Utils.global.mapMain.put("senderid", id);
                    Utils.global.mapMain.put("recieverid", did);
                    Utils.global.mapMain.put("msg", message.getText().toString().trim());
                    Utils.global.mapMain.put("keyvalue", "user");

                    Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_CHAT);

                    if (Utils.isNetworkAvailable(ActivityChat.this)) {
                        message.setText("");
                        JsonPost.getNetworkResponse(ActivityChat.this, null,
                                Utils.global.mapMain, ConstVariable.Chat);
                    } else {
                        Utils.showInternetErrorMessage(ActivityChat.this);
                    }
                }
            }
        });

    }

    public void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.e(TAG+"81", "load browseMembersList Data "+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                if (chatList != null) {
                    chatList.clear();
                }

                for (int i = 0; i < viewList.size(); i++) {
                    // HashMap<String, Object> mp = new HashMap<String, Object>();
                    // mp = viewList.get(i);
                    // modelItem mp = viewList.get(i);
                    //if (!chatList.contains(mp)) {
                    chatList.add(new modelItem(viewList.get(i)));
                    // }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!mode.equalsIgnoreCase("update")) {
                    setAdapterFriendsRequestList(context);
                } else {
                    chatAdapter.notifyItemInserted(chatList.size());
                    chatAdapter.notifyDataSetChanged();
                }
            }
            chatMan.setAutoMeasureEnabled(true);
        } else {
            rv_chat.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    public void setAdapterFriendsRequestList(final Context context) {
        if (rv_chat != null) {
            rv_chat.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatAdapter = new ChatAdapter(mcontext, chatList, rv_chat, R.layout.chat_rowitem_left_other, ConstVariable.Chat);
                rv_chat.setAdapter(chatAdapter);
                rv_chat.scrollToPosition(chatList.size() - 1);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(backPress.equalsIgnoreCase("back")){
            Intent i = new Intent(ActivityChat.this, Navigation.class);
            //i.putExtra("keyPosition" , 1);
            startActivity(i);
            finishAffinity();
            finish();
        }else{
            super.onBackPressed();
            Utils.hideSoftKeyboard(this);
        }
    }

    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String isid = intent.getStringExtra("isid");

                if (isid.equalsIgnoreCase("9")) {
                    String status = intent.getStringExtra("status");

                    HashMap<String, Object> data;
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newusermessage")) {
                        //chatListRequest();
                        if (Utils.isNetworkAvailable(ActivityChat.this)) {
                            fetchMsgsList();
                        } else {
                            Utils.showInternetErrorMessage(mcontext);
                        }
                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        pref1.setisnewmsg("");
                    }
                }
            }
        }
    };
}
