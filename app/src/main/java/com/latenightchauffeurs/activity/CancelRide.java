package com.latenightchauffeurs.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/16/2018.
 */

public class CancelRide extends AppCompatActivity implements View.OnClickListener {
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
    public static CancelRide Instance;
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
        }
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
                CancelAmountRequest(CancelRide.this);
                break;
        }
    }

    public void cancelCurrentRideRequest(Context mcontext, String rid) {
        map = new HashMap<>();
        map.put("ride_id", rid);
        map.put("reason", message.getText().toString());
        OnlineRequest.cancelUserRidRequest(CancelRide.this, map);
    }

    public void CancelAmountRequest(Context mcontext) {
        OnlineRequest.cancelRideAmountRequest(CancelRide.this, null);
    }

    public void showMessage(Context context, Map<String, Object> data) {
        if (!data.get("amount").toString().equalsIgnoreCase("") && !data.get("amount").toString().equalsIgnoreCase("null")) {
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogLayout = inflater.inflate(R.layout.alert_dialog6, null);
            final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(context).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setView(dialogLayout);
            dialog.show();

            // final RelativeLayout rootlo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
            final TextView title = (TextView) dialog.findViewById(R.id.title);
            final TextView textView = (TextView) dialog.findViewById(R.id.desc);
            final Button attend = (Button) dialog.findViewById(R.id.attend);
            final Button cancel = (Button) dialog.findViewById(R.id.cancel);

            if (data.containsKey("amount")) {
                if (!data.get("amount").toString().equalsIgnoreCase(""))
                    textView.setText(data.get("amount").toString() + "$" + " will be charged for Ride Cancellation.");
            }

            title.setText(R.string.app_name);

            attend.setText("SUBMIT");

            attend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        dialog.cancel();
                        cancelCurrentRideRequest(CancelRide.this, rId);
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

    public void closeActivity() {
        finish();
    }
}
