package com.latenightchauffeurs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.APIClient;
import com.latenightchauffeurs.Utils.APIInterface;
import com.latenightchauffeurs.Utils.ParsingHelper;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Notifications extends AppCompatActivity {

    TextView textViewTitle;

    private static final String TAG = "Notifications";
    SavePref pref1 = new SavePref();

    public Call<ResponseBody> call = null;
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);

    Activity activity;

    RecyclerView recyclerViewMyRewardProgram;
    NotificationsAdapter historyAdapter;

    TextView textViewMsg, textViewTotalPoints;
    ProgressBar progressBar;

    ImageView imageViewBack;

    ArrayList<ItemNotificaitons> arrayList = new ArrayList<ItemNotificaitons>();

    String backPress = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notifications);
        textViewTitle = (TextView) findViewById(R.id.title);

        textViewTitle.setText("Notifications");


        textViewMsg = (TextView) findViewById(R.id.textView123124);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1444);

        imageViewBack = (ImageView) findViewById(R.id.back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerViewMyRewardProgram = (RecyclerView) findViewById(R.id.actionable_items);
        recyclerViewMyRewardProgram.setHasFixedSize(true);
        recyclerViewMyRewardProgram.setLayoutManager(new LinearLayoutManager(activity , LinearLayoutManager.VERTICAL,false));
        historyAdapter = new NotificationsAdapter(Notifications.this, arrayList);
        recyclerViewMyRewardProgram.setAdapter(historyAdapter);


        pref1.SavePref(Notifications.this);
        // String id = pref1.getUserId();

        if (getIntent().hasExtra("back")) {
            backPress = getIntent().getStringExtra("back");
        }



        SavePref pref = new SavePref();
        pref.SavePref(Notifications.this);
        pref.setBadgeCount(0);


        advertise();
    }



    private void advertise() {
        progressBar.setVisibility(View.VISIBLE);
//        ArrayList<MultipartBody.Part> arrayListMash = new ArrayList<MultipartBody.Part>();
////        MultipartBody.Part key1 = MultipartBody.Part.createFormData("", pref1.getUserId());
////        arrayListMash.add(key1);
//
//        MultipartBody.Part userid = MultipartBody.Part.createFormData("driver_id", pref1.getUserId());
//        arrayListMash.add(userid);
//
//        MultipartBody.Part program_id = MultipartBody.Part.createFormData("program_id", program_id1);
//        arrayListMash.add(program_id);
//
//        MultipartBody.Part driver_time = MultipartBody.Part.createFormData("driver_time", driver_time1);
//        arrayListMash.add(driver_time);
//
//        //  Log.e(TAG , "keyQQQ "+key);

        call = apiInterface.getNotification();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();

                        Log.e(TAG, "Refreshed historyRewardProgramList: " + responseCode);


                        JSONObject jsonObject = new JSONObject(responseCode);
                        String status = jsonObject.getString("status");

                        arrayList = new ParsingHelper().getNotications(responseCode);
                        Log.e(TAG, "Refreshed getHistoryRewardProgramList: " + arrayList.size());

                        if(status.equals("1")){
                            textViewMsg.setVisibility(View.GONE);
                        }else{
                            textViewMsg.setVisibility(View.VISIBLE);
                            String message = jsonObject.getString("msg");
                            textViewMsg.setText(""+message);
                        }

                        historyAdapter.updateData(arrayList);


                    }else{

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);

            }
        });




    }







    public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

        private static final String TAG = "MyRewardProgramAdapter";

        ArrayList<ItemNotificaitons> arrayList = new ArrayList<ItemNotificaitons>();

        Activity context;

        public NotificationsAdapter(Activity context11, ArrayList<ItemNotificaitons> arrayList1) {
            super();
            this.context = context11;
            this.arrayList = arrayList1;


        }

        @Override
        public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.notification_item, viewGroup, false);
            return new NotificationsAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final NotificationsAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.bindItems(arrayList.get(i), i, context);
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view11 = null;

            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
            }


            public void bindItems(final ItemNotificaitons itemMyRewardProgram, int i, final Activity context) {

                TextView textViewDate = (TextView) itemView.findViewById(R.id.date);
                TextView textViewTitle = (TextView) itemView.findViewById(R.id.tv_title);
                TextView textViewMessage = (TextView) itemView.findViewById(R.id.tv_message);


                textViewDate.setText(""+itemMyRewardProgram.getDate());
                textViewTitle.setText(""+itemMyRewardProgram.getTitle());
                textViewMessage.setText(""+itemMyRewardProgram.getMessage());



            }

        }



        public void updateData(ArrayList<ItemNotificaitons> arrayList2) {
            // TODO Auto-generated method stub
            //arrayList.clear();
//            alName.addAll(arrayList2);
            arrayList = arrayList2;
            notifyDataSetChanged();
        }



    }






    @Override
    public void onBackPressed() {
        // handler.removeCallbacks(runnable);
        if(backPress.equalsIgnoreCase("back")){
            Intent i = new Intent(Notifications.this, Navigation.class);
            //i.putExtra("keyPosition" , 1);
            startActivity(i);
            finishAffinity();
            finish();
        }else{
            super.onBackPressed();
            Utils.hideSoftKeyboard(this);
        }
    }



}

