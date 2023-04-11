package com.latenightchauffeurs.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.fragment.Home;
import com.latenightchauffeurs.model.ItemModel;
import com.latenightchauffeurs.model.SavePref;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Feedback extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Feedback";
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.msg)
    EditText msg;


    @BindView(R.id.dname)
    TextView dname;

    @BindView(R.id.submit)
    Button submit;


    @BindView(R.id.driverimage)
    ImageView pic;

    @BindView(R.id.rating)
    RatingBar rating;

    HashMap<String, Object> map;
    public static Feedback Instance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        ButterKnife.bind(this);

        Instance = this;

        submit.setOnClickListener(this);
        back.setOnClickListener(this);

        title.setText("Gratuity");

        Home.Instance.i = 0;



        if (getIntent().hasExtra("map")) {
            map = new HashMap<>();
            map = (HashMap<String, Object>) getIntent().getSerializableExtra("map");

            if (map.containsKey("photo")) {
                if (!map.get("photo").toString().equalsIgnoreCase(""))
                    Picasso.with(Feedback.this).load(Settings.URLIMAGEBASE + map.get("photo").toString()).placeholder(R.drawable.appicon).into(pic);
                // userpic.setImageBitmap(getBitmapFromURL(image));

                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Feedback.this , ImageZoom.class);
                        intent.putExtra("key" , "1");
                        intent.putExtra("imageLink" , ""+Settings.URLIMAGEBASE + map.get("photo").toString());
                        startActivity(intent);
                    }
                });
            }

            StringBuilder sb = new StringBuilder();

            if (map.containsKey("first_name")) {
                if (!map.get("first_name").toString().equalsIgnoreCase(""))
                    sb.append(map.get("first_name").toString());
            }

            if (map.containsKey("last_name")) {
                sb.append(" ");

                if (!map.get("last_name").toString().equalsIgnoreCase(""))
                    sb.append(map.get("last_name").toString());
                dname.setText(sb);
            }
        }

        SavePref pref1 = new SavePref();
        pref1.SavePref(Feedback.this);
        pref1.setRatingStatus("");
        pref1.setdRatingmap("");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submit:
                submitFeedback();
                break;

            case R.id.back:
                finish();
                break;

        }
    }

    public void submitFeedback() {
        float x = 0.0f;


        if (rating.getRating() == x) {
            Utils.toastTxt("Please provide a rating.", Feedback.this);
        } else {
            String tp = "";
            getFeedbackRequest(msg.getText().toString(), String.valueOf(rating.getRating()), tp);
        }
    }

    public void getFeedbackRequest(String msg, String rating, String tipstatus) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(Feedback.this);
        String id = pref1.getUserId();

        double r = Double.parseDouble(rating);
        int rr = (int) r;

        new Utils(Feedback.this);
        Utils.global.mapMain();

        if (map != null) {
            Utils.global.mapMain.put("driverid", map.get("driverid").toString());
        }

        if (map.containsKey("rideid")) {
            if (!map.get("rideid").toString().equalsIgnoreCase(""))
                Utils.global.mapMain.put("rideid", map.get("rideid").toString());
        }

        Utils.global.mapMain.put("userid", id);
        Utils.global.mapMain.put("msg", msg);
                Utils.global.mapMain.put("rating", rr);
        Utils.global.mapMain.put("amount", "");
        Utils.global.mapMain.put("percentage", "");
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_FEED_BACK);


        Log.e(TAG, "Utils.global.mapMainAA "+Utils.global.mapMain.toString());

        if (Utils.isNetworkAvailable(Feedback.this)) {
            JsonPost.getNetworkResponse(Feedback.this, null,
                    Utils.global.mapMain, ConstVariable.UserRating1, null);
        } else {
            Utils.showInternetErrorMessage(Feedback.this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void closeActivity() {
        finish();
    }
}
