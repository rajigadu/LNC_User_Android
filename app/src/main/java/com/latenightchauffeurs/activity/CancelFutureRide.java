package com.latenightchauffeurs.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.APIClient;
import com.latenightchauffeurs.Utils.APIInterface;
import com.latenightchauffeurs.Utils.OnlineRequest;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by narayana on 5/16/2018.
 */

public class CancelFutureRide extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CancelFutureRide";
    public APIInterface apiInterface  = APIClient.getClientVO().create(APIInterface.class);
    public Call<ResponseBody> call = null;


    String cancelRideCharges = "";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.message)
    EditText message;

    String rId = "";
    public static CancelFutureRide Instance;
    HashMap<String, Object> map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelride);
        ButterKnife.bind(this);

        Instance = this;

        title.setText("Ride Cancel");

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);

        if (getIntent() != null) {
            rId = getIntent().getStringExtra("rideid");


          //  advertise();
        }
    }





    private void advertise() {

//        ArrayList<MultipartBody.Part> arrayListMash = new ArrayList<MultipartBody.Part>();
//        MultipartBody.Part key1 = MultipartBody.Part.createFormData(pref.getTokenKey(), pref.getTokenValue());
//        arrayListMash.add(key1);
//
//        MultipartBody.Part userid = MultipartBody.Part.createFormData("userid", pref.getId());
//        arrayListMash.add(userid);
//
//
//
//        MultipartBody.Part pageing = MultipartBody.Part.createFormData("display_screen", Utility.VO_ADS_HISTORY);
//        arrayListMash.add(pageing);
//

        call = apiInterface.adsByScreen();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //progressBar.setVisibility(View.GONE);
                String responseCode = "";
                try {
                    if(response.body() != null) {
                        responseCode = response.body().string();

//                        {"status":"0","message":"No Bids Yet!.","data":[]}
                        Log.e(TAG, "Refreshed getadvertiseCC: " + responseCode);

//                        JSONObject jsonObject = new JSONObject(responseCode);
//                        String status = jsonObject.getString("status");
//

                    }else{
                       // new ShowMsg().createSnackbar(getActivity(), "Something went wrong!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                   // new ShowMsg().createSnackbar(getActivity(), ""+e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // progressDialog.dismiss();

            }
        });




    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.submit:

//                if(!cancelRideCharges.equalsIgnoreCase("")){
//
//                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CancelFutureRide.this);
//                    builder.setTitle(CancelFutureRide.this.getString(R.string.app_name));
//                    builder.setMessage("Your Cancel Ride Cost is $"+cancelRideCharges)
//                            .setCancelable(false)
//                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.dismiss();
//                                    CancelAmountRequest();
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    builder.show();
//
////                    CancelAmountRequest();
//                }

                CancelAmountRequest();

//                 cancelCurrentRideRequest(rId);
                break;
        }
    }

    public void CancelAmountRequest() {
        map = new HashMap<>();
        map.put("ride_id", rId);
        map.put("reason", message.getText().toString());
        OnlineRequest.cancelFutureRideAmountRequest(CancelFutureRide.this, map);
    }

    public void cancelCurrentRideRequest(String rid) {
        map = new HashMap<>();
        map.put("ride_id", rid);
        map.put("reason", message.getText().toString());
        OnlineRequest.cancelFutureRidRequest(CancelFutureRide.this, map);
    }

    public void closeActivity() {
        finish();
    }

    public void showMessage(Context context, String status, String amt) {
        if (!status.equalsIgnoreCase("") && !status.equalsIgnoreCase("null")) {
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogLayout = inflater.inflate(R.layout.alert_dialog6, null);
            final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setView(dialogLayout);
            dialog.show();

            // final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
            final TextView title = (TextView) dialog.findViewById(R.id.title);
            final TextView textView = (TextView) dialog.findViewById(R.id.desc);
            final Button attend = (Button) dialog.findViewById(R.id.attend);
            final Button cancel = (Button) dialog.findViewById(R.id.cancel);

            if (amt != null && !amt.equalsIgnoreCase("") && !amt.equalsIgnoreCase("null")) {
                textView.setText("$"+amt + " will be charged for Ride Cancellation.");
            } else {
                textView.setText("Are you sure you want to cancel?");
            }

            title.setText(R.string.app_name);

            attend.setText("SUBMIT");

            attend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        dialog.cancel();
                        cancelCurrentRideRequest(rId);
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
}
