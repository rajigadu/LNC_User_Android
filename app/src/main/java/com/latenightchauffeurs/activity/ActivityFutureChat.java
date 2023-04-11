package com.latenightchauffeurs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.adapter.FutureChatAdapter;
import com.latenightchauffeurs.model.SavePref;
import com.latenightchauffeurs.model.modelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manjeet on 8/23/2017.
 */

public class ActivityFutureChat extends AppCompatActivity {
    public static String TAG = ActivityFutureChat.class.getName();

    public static Context mcontext;
    public static RecyclerView rv_chat;
    public static TextView noData;
    public static FutureChatAdapter chatAdapter;
    public static boolean isDataLoad = false;
    public static RecyclerView.LayoutManager chatMan;
    public static List<modelItem> chatList;
    public HashMap<String, Object> map;
    public ImageView image;
    public TextView name;
    ImageView back;
    TextView title;
    EditText message;
    Button send;
    private CountDownTimer timer;
    //public Handler handler;
    // public Runnable runnable;
    public static String reqid, receiverid;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandleMessageReceiver != null)
            unregisterReceiver(mHandleMessageReceiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        noData = (TextView) findViewById(R.id.nodata);
        name = (TextView) findViewById(R.id.name);
        back = (ImageView) findViewById(R.id.back);
        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        title.setVisibility(View.VISIBLE);
        title.setText("Chat");

        if (getIntent() != null) {
            reqid = getIntent().getStringExtra("requestId");
            receiverid = getIntent().getStringExtra("userid");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent= new Intent(ActivityLogin.this,ActivityNavigation.class);
                //startActivity(intent);
                // handler.removeCallbacks(runnable);
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        mcontext = ActivityFutureChat.this;

        chatMan = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        rv_chat.setHasFixedSize(true);
        rv_chat.setLayoutManager(chatMan);

        //handler = new Handler();

        /* runnable = new Runnable()
         {
            @Override
            public void run()
            {
                chatListRequest();
                handler.postDelayed(this,5000);
            }
        };
        handler.postDelayed(runnable,5000);*/

        chatListRequest();

        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        pref1.setisfnewmsg("");

        registerReceiver(mHandleMessageReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
    }

    public static void chatListRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String did = pref1.getDriverId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put("senderid", id);
        Utils.global.mapMain.put("recieverid", did);
        Utils.global.mapMain.put("msg", "");
        Utils.global.mapMain.put("keyvalue", "user");
        Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_CHAT);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain,
                    ConstVariable.ChatList, null);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public void submit() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();
        String did = pref1.getDriverId();

        new Utils(ActivityFutureChat.this);

        if (message.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter message .", ActivityFutureChat.this);
        } else if (message.getText().toString().length() > 250) {
            Utils.toastTxt(" Maximum message length 250 Charecters.", ActivityFutureChat.this);
        } else {
            new Utils(ActivityFutureChat.this);
            Utils.global.mapMain();
            Utils.global.mapMain.put("senderid", id);
            Utils.global.mapMain.put("recieverid", did);
            Utils.global.mapMain.put("msg", message.getText().toString().trim());
            Utils.global.mapMain.put("keyvalue", "user");

            Utils.global.mapMain.put(ConstVariable.URL, Settings.URL_CHAT);

            if (Utils.isNetworkAvailable(ActivityFutureChat.this)) {
                message.setText("");
                JsonPost.getNetworkResponse(ActivityFutureChat.this, null,
                        Utils.global.mapMain, ConstVariable.Chat, null);
            } else {
                Utils.showInternetErrorMessage(ActivityFutureChat.this);
            }
        }
    }

    public static void loadRequestsList(Context context, List<HashMap<String, Object>> viewList, String mode) {
        // Utils.e(TAG+"81", "load browseMembersList Data "+mode+viewList);
        if (viewList != null && viewList.size() > 0) {
            try {
                //Utils.e(TAG+"88", "browseMembersList new");
                chatList = new ArrayList<modelItem>();

                //Utils.e(TAG+"93", "browseMembersList new "+eventList);

                for (int i = 0; i < viewList.size(); i++) {
                    HashMap<String, Object> mp = new HashMap<String, Object>();
                    mp = viewList.get(i);

                    if (!chatList.contains(mp)) {
                        chatList.add(new modelItem(mp));
                    }
                }
                //Utils.e(TAG+"118", "browseMembersList"+eventList.size());
            } catch (Exception e) {
                //Utils.e(TAG+"122", "Exception======================Exception======================Exception");
                e.printStackTrace();
            } finally {
                // Utils.e(TAG+"127", "browseMembersList "+eventList.size());
                //Utils.e(TAG+"128", "ok");
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

    public static void setAdapterFriendsRequestList(final Context context) {
        if (rv_chat != null) {
            rv_chat.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }

        //Utils.e(TAG+"156", "setAdapter ok "+eventList);
        chatAdapter = new FutureChatAdapter(mcontext, chatList, rv_chat, R.layout.chat_rowitem_left_other, ConstVariable.Chat);
        //set the adapter object to the Recyclerview
        //Utils.e(TAG+"159", "setAdapter ok "+eventsAdapter.getItemCount());
        rv_chat.setAdapter(chatAdapter);
        rv_chat.scrollToPosition(chatList.size() - 1);
    }

    @Override
    public void onBackPressed() {
        // handler.removeCallbacks(runnable);
        super.onBackPressed();
    }

    public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String isid = intent.getStringExtra("isid");

                if (isid.equalsIgnoreCase("9")) {
                    String status = intent.getStringExtra("status");

                    HashMap<String, Object> data = new HashMap<>();
                    data = (HashMap<String, Object>) intent.getSerializableExtra("data");

                    if (data.containsKey("ride") && data.get("ride").toString().equalsIgnoreCase("newusermessage")) {
                        chatListRequest();

                        SavePref pref1 = new SavePref();
                        pref1.SavePref(mcontext);
                        pref1.setisfnewmsg("");
                    }
                }
            }
        }
    };
}
