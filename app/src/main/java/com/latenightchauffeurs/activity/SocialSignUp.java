package com.latenightchauffeurs.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 2/7/2018.
 */

public class SocialSignUp extends AppCompatActivity
{
    public  String type="",url="";

    @BindView(R.id.fname)
    EditText fname;

    @BindView(R.id.lname)
    EditText lname;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.signup)
    Button signup;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    HashMap<String,Object> map;

    public static List<String> areas;
    public static  List<String> aids;
    public static  List<String> ages;
    public static String aid="",aname="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_signup);

        ButterKnife.bind(this);

        signup.setOnClickListener(mCorkyListener);
       // location.setOnClickListener(mCorkyListener);
        title.setText("Social SignUp");

        SavePref pref1 = new SavePref();
        pref1.SavePref(SocialSignUp.this);
        String fmail=pref1.getsocilamail();
        String uname=pref1.getUserFName();
        url=pref1.getImage();

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

       /* if (getIntent()!=null)
        {
            type=getIntent().getExtras().getString("type");
        }*/

        if (!fmail.equalsIgnoreCase(""))
        {
            email.setText(fmail);
        }

        if (!uname.equalsIgnoreCase(""))
        {
            fname.setText(uname);
        }

        serviceLocationsListRequest();
    }

    private View.OnClickListener mCorkyListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.signup:
                   submit();
                    break;
               /* case R.id.location:
                    pickLocation();
                    break;*/
            }
        }
    };

 /*   public  void  pickLocation()
    {
        new Utils(SocialSignUp.this);
        // Utils.toastTxt("ok",UserSignup.this);

        final CharSequence[] items;
        items=areas.toArray(new CharSequence[areas.size()]);

        if (items.length>0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SocialSignUp.this);
            builder.setTitle("Qwik Ride Preferred Locations");
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int item)
                {
                    // Do something with the selection
                    location.setText(items[item]);
                    aid=aids.get(item);
                    aname=items[item].toString();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }*/

    public  void  submit()
    {
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        new Utils(SocialSignUp.this);

        if (fname.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter first name.",SocialSignUp.this);
        }
        if (lname.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter last name.",SocialSignUp.this);
        }
        else if (email.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter email address.",SocialSignUp.this);
        }
        else if(!Utils.isValidEmail(email.getText().toString().trim()))
        {
            Utils.toastTxt("Please enter valid email address.",SocialSignUp.this);
        }
        else  if(mobile.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter etMobile number.",SocialSignUp.this);
        }
        else
        {
            map=new HashMap<>();
            map.put(ConstVariable.FULLNAME,fname.getText().toString().trim());
            map.put("lname",fname.getText().toString().trim());
            map.put(ConstVariable.EMAIL,email.getText().toString().trim());
            map.put(ConstVariable.MOBILE,mobile.getText().toString().trim());
            map.put("purl",url);
           // map.put("location_id",aid);
            OnlineRequest.socialSignupRequest(SocialSignUp.this,map);
        }
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }

    public  void serviceLocationsListRequest()
    {
        SavePref pref1 = new SavePref();
        pref1.SavePref(SocialSignUp.this);
        String id = pref1.getUserId();

        new Utils(SocialSignUp.this);
        Utils.global.mapMain();
        Utils.global.mapMain.put("type","4");
        Utils.global.mapMain.put(ConstVariable.URL, com.latenightchauffeurs.Utils.Settings.URL_SERVICE_INFO);

        if(Utils.isNetworkAvailable(SocialSignUp.this))
        {
            JsonPost.getNetworkResponse(SocialSignUp.this,null,Utils.global.mapMain,ConstVariable.ServiceInfo);
        }
        else
        {
            Utils.showInternetErrorMessage(SocialSignUp.this);
        }
    }

    public static void  populatecities(List<HashMap<String,Object>> list)
    {
        aids=new ArrayList<>();
        areas=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            aids.add(list.get(i).get("id").toString());
            areas.add(list.get(i).get("location").toString());
        }
    }
}
