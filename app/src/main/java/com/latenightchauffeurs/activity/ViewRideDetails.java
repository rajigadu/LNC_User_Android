package com.latenightchauffeurs.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Settings;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/16/2018.
 */

public class ViewRideDetails extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ViewRideDetails";
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    public static ViewRideDetails Instance;
    HashMap<String, Object> map, rMap;

    @BindView(R.id.driverimage)
    ImageView driverimage;

    @BindView(R.id.dname)
    TextView dname;

    @BindView(R.id.distance)
    TextView distance;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.call)
    ImageView call;

    @BindView(R.id.msg)
    ImageView msg;

    @BindView(R.id.rating)
    RatingBar rating;

    @BindView(R.id.ridecancel)
    Button rideCancel;

    @BindView(R.id.dnumber)
    TextView dnumber;

    @BindView(R.id.clt_main)
    RelativeLayout rl_data;

    @BindView(R.id.rl_ridestatus)
    RelativeLayout rl_nodata;

    @BindView(R.id.newchatmsg)
    TextView ischatmsg;

    @BindView(R.id.ride_acc)
    TextView ride_acc;



    private static final int RC_CALL_PERM = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futureride_view);
        ButterKnife.bind(this);
        Instance = this;
        title.setText("Future Ride");

        if (getIntent() != null) {
            rMap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
        }

        //reminder.setOnClickListener(this);
        back.setOnClickListener(this);
        call.setOnClickListener(this);
        msg.setOnClickListener(this);
        rideCancel.setOnClickListener(this);

        getDriverDetailsRequest(ViewRideDetails.this, rMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.call:
                Log.e(TAG , "onClickrMap "+rMap);
                showMessage("Do You Want To Call ?", rMap);
                break;
            case R.id.msg:
                HashMap<String, Object> dmap = new HashMap<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (!value.toString().equalsIgnoreCase("null")) {
                        dmap.put(key, value);
                    }
                }
                ischatmsg.setVisibility(View.GONE);
                Intent i = new Intent(ViewRideDetails.this, ActivityChat.class);
                i.putExtra("map", (Serializable) dmap);
                startActivity(i);

                // Utils.startActivity(ViewRideDetails.this, ActivityChat.class);
                break;
            case R.id.ridecancel:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ViewRideDetails.this);
                builder.setTitle(ViewRideDetails.this.getString(R.string.app_name));
                builder.setMessage("Ride canceled successfully please note if you are cancelling within four hours you are subject to being billed for your ride. To keep the ride please press cancel.")
                        .setCancelable(false)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent j = new Intent(ViewRideDetails.this, CancelFutureRide.class);
                                if (rMap.containsKey("id"))
                                    j.putExtra("rideid", rMap.get("id").toString());
                                startActivity(j);
                            }
                        });
                builder.show();
                break;
        }
    }

    public void showMessage(String dlgText, final HashMap<String, Object> data) {
        LayoutInflater inflater = LayoutInflater.from(ViewRideDetails.this);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog6, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.
                Builder(ViewRideDetails.this).create();
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

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                    callPermission(data);
                } catch (Exception e) {
                    e.printStackTrace();
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

    private void callPermission(HashMap<String, Object> map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(ViewRideDetails.this, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "callPermissionmap "+map);
                getCall(map);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, RC_CALL_PERM);
                }
                // ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},RC_SMS_PERM);
            }
        } else {
            getCall(map);
        }
    }

    public void getDriverDetailsRequest(Context mcontext, HashMap<String, Object> rid) {
        map = new HashMap<>();
        if (rMap.containsKey("id"))
            map.put("ride_id", rMap.get("id").toString());
        OnlineRequest.getDriverDetailsRequest(mcontext, map);
    }

    public void getFutureRideDriverDetails(List<HashMap<String, Object>> list) {
        map = new HashMap<>();

        if (list != null) {
            rl_data.setVisibility(View.VISIBLE);
            rideCancel.setVisibility(View.VISIBLE);
            rl_nodata.setVisibility(View.GONE);

            StringBuilder sb = new StringBuilder();
            StringBuilder sb_one = new StringBuilder();
            map = list.get(0);

            if (map.containsKey("profile_pic")) {
                if (!map.get("profile_pic").toString().equalsIgnoreCase(""))
                    Picasso.with(ViewRideDetails.this).load(Settings.URLIMAGEBASE + map.get("profile_pic").toString()).placeholder(R.drawable.appicon).into(driverimage);
            }

            if (map.get("first_name") != "") {
                if (map.containsKey("first_name") && !map.get("first_name").toString().equalsIgnoreCase("")) {
                    sb.append(map.get("first_name").toString());
                }
            }

            if (map.get("last_name") != "") {
                if (map.containsKey("last_name") && !map.get("last_name").toString().equalsIgnoreCase("")) {
                    sb.append(" ");
                    sb.append(map.get("last_name").toString());
                    dname.setText(sb);
                }
            }

            if (map.get("mobile") != "") {
                if (map.containsKey("mobile") && !map.get("mobile").toString().equalsIgnoreCase("")) {
                    dnumber.setText(map.get(ConstVariable.MOBILE).toString());
                }
            }

            if (map.get("rating") != "") {
                if (map.containsKey("rating") && !map.get("rating").toString().equalsIgnoreCase("")) {
                    rating.setRating(Float.valueOf(map.get("rating").toString()));
                }
            }

            if (rMap.get("distance") != "") {
                if (rMap.containsKey("distance") && !rMap.get("distance").toString().equalsIgnoreCase("")) {
                    distance.setText(String.format("%.2f", Double.valueOf(rMap.get("distance").toString())) + " mi");
                }
            }

            if (rMap.get("otherdate") != "") {
                if (rMap.containsKey("otherdate") && !rMap.get("otherdate").toString().equalsIgnoreCase("")) {
                    sb_one.append(rMap.get("otherdate").toString());
                }
            }

            if (rMap.get("time") != "") {
                if (rMap.containsKey("time") && !rMap.get("time").toString().equalsIgnoreCase("")) {
                    sb_one.append(" ");
                    sb_one.append(rMap.get("time").toString());
                    date.setText(sb_one);
                }
            }


            if (rMap.get("status").toString().equalsIgnoreCase("1")) {
                ride_acc.setVisibility(View.VISIBLE);
            }else{
                ride_acc.setVisibility(View.GONE);
            }



        } else {
            rl_data.setVisibility(View.GONE);
            rideCancel.setVisibility(View.VISIBLE);
            rl_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RC_CALL_PERM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == RC_CALL_PERM) {
                        getCall(rMap);
                    }
                }
                break;
        }
    }

    public void getCall(HashMap<String, Object> map) {
        //if (map.containsKey("driverMobile") && !map.get("driverMobile").toString().equalsIgnoreCase("")) {
        if (dnumber.getText().toString().length() > 0)
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", dnumber.getText().toString(), null)));
        //} else if (map.containsKey("etMobile") && !map.get("etMobile").toString().equalsIgnoreCase("")) {
        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", map.get("etMobile").toString(), null)));
        // }
    }
}
