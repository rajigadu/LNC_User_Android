package com.latenightchauffeurs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.latenightchauffeurs.adapter.SingleSelectionAdapter;
import com.latenightchauffeurs.fragment.Home;
import com.latenightchauffeurs.model.ItemModel;
import com.latenightchauffeurs.model.SavePref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Rating extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.msg)
    EditText msg;

    @BindView(R.id.amount)
    EditText amount;

    @BindView(R.id.dname)
    TextView dname;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.clear)
    Button clear;

    public String tip = "0";

    @BindView(R.id.driverimage)
    ImageView pic;

    @BindView(R.id.rating)
    RatingBar rating;

    HashMap<String, Object> map;
    public static Rating Instance;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String tag;
    List<String> tips = new ArrayList();
    SingleSelectionAdapter adapterSingle;
    ItemModel modelSelected = null;
    List list;

    private List getList() {
        List<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < tips.size(); i++) {
            ItemModel model = new ItemModel();
            model.setName(tips.get(i));
            model.setId(i);
            list.add(model);
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackalert);
        ButterKnife.bind(this);

        Instance = this;

        submit.setOnClickListener(this);
        clear.setOnClickListener(this);
        back.setOnClickListener(this);

        title.setText("Gratuity");

        Home.Instance.i = 0;

        amount.setVisibility(View.VISIBLE);


        amount.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("click", "onMtouch");
//                amount.setSelection(amount.getText().length());

                adapterSingle = new SingleSelectionAdapter(Rating.this, list);
                mRecyclerView.setAdapter(adapterSingle);
                modelSelected = null;
                tip = "0";
                clear.setVisibility(View.GONE);

                return false;
            }
        });



        tips.add(0, "15");
        tips.add(1, "20");
        tips.add(2, "25");

        clear.setVisibility(View.GONE);

        if (getIntent().hasExtra("map")) {
            map = new HashMap<>();
            map = (HashMap<String, Object>) getIntent().getSerializableExtra("map");

            if (map.containsKey("photo")) {
                if (!map.get("photo").toString().equalsIgnoreCase(""))
                    Picasso.with(Rating.this).load(Settings.URLIMAGEBASE + map.get("photo").toString()).placeholder(R.drawable.appicon).into(pic);
                // userpic.setImageBitmap(getBitmapFromURL(image));

                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Rating.this , ImageZoom.class);
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
        pref1.SavePref(Rating.this);
        pref1.setRatingStatus("");
        pref1.setdRatingmap("");

        list = getList();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterSingle = new SingleSelectionAdapter(this, list);
        mRecyclerView.setAdapter(adapterSingle);
        mRecyclerView.setVisibility(View.VISIBLE);
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

            case R.id.clear:
                adapterSingle = new SingleSelectionAdapter(this, list);
                mRecyclerView.setAdapter(adapterSingle);
                modelSelected = null;
                tip = "0";
                clear.setVisibility(View.GONE);
                break;
        }
    }

    public void submitFeedback() {
        float x = 0.0f;

        if (((SingleSelectionAdapter) adapterSingle).lastCheckedPosition != -1)
            modelSelected = ((SingleSelectionAdapter) adapterSingle).getSelectedItem();

        if (rating.getRating() == x) {
            Utils.toastTxt("Please provide a rating.", Rating.this);
        } else if (tip.equalsIgnoreCase("1")) {
            if (modelSelected == null && amount.getText().toString().isEmpty()) {
                Utils.toastTxt("Please select percentage of tip or your own amount.", Rating.this);
            } else if (modelSelected != null && !amount.getText().toString().isEmpty()) {
                clear.setVisibility(View.VISIBLE);
                Utils.toastTxt("Please select either percentage of tip or your own amount.", Rating.this);
            } else {
                String tp = "";
                getFeedbackRequest(msg.getText().toString(), String.valueOf(rating.getRating()), tp);
            }
        } else {
            String tp = "";
            getFeedbackRequest(msg.getText().toString(), String.valueOf(rating.getRating()), tp);
        }
    }

    public void getFeedbackRequest(String msg, String rating, String tipstatus) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(Rating.this);
        String id = pref1.getUserId();

        double r = Double.parseDouble(rating);
        int rr = (int) r;

        new Utils(Rating.this);
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
        Utils.global.mapMain.put("tip", tipstatus);

        if (modelSelected != null) {
            Utils.global.mapMain.put("percentage", modelSelected.getName());
            Utils.global.mapMain.put("amount", "");
            tip = "1";
        } else if (!amount.getText().toString().equalsIgnoreCase("")) {
            Utils.global.mapMain.put("amount", amount.getText().toString());
            Utils.global.mapMain.put("percentage", "");
            tip = "1";
        } else {
            Utils.global.mapMain.put("amount", "");
            Utils.global.mapMain.put("percentage", "");
        }

        Utils.global.mapMain.put("rating", rr);
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_FEED_BACK);

        if (Utils.isNetworkAvailable(Rating.this)) {
            JsonPost.getNetworkResponse(Rating.this, null, Utils.global.mapMain, ConstVariable.UserRating);
        } else {
            Utils.showInternetErrorMessage(Rating.this);
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






    public void ddd() {
        tip = "1";
        clear.setVisibility(View.VISIBLE);

        amount.setText("");

        InputMethodManager imm = (InputMethodManager) Rating.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(Rating.this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
