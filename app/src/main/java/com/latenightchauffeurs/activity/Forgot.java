package com.latenightchauffeurs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.ConstVariable;
import com.latenightchauffeurs.Utils.JsonPost;
import com.latenightchauffeurs.Utils.OnlineRequest;
import com.latenightchauffeurs.Utils.Settings;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Forgot extends AppCompatActivity implements View.OnClickListener
{
    public static Forgot Instance;

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.resetnow)
    Button restnow;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    HashMap<String,Object> map;

    @OnClick(R.id.resetnow) void funresetnow()
    {
        submit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Instance=this;
        ButterKnife.bind(this);

       // checkBack=(ImageView) findViewById(R.id.checkBack);
        //title=(TextView) findViewById(R.id.title);
        title.setText("Forgot Password");

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    public void submit()
    {
        new Utils(Forgot.this);

        if (email.getText().toString().trim().isEmpty())
        {
            Utils.showOkDialog("Please enter email address.",Forgot.this);
        }
        else if(!Utils.isValidEmail(email.getText().toString().trim()))
        {
            Utils.showOkDialog("Please enter valid email address.",Forgot.this);
        }
        else
        {
            map=new HashMap<>();
            map.put("email",email.getText().toString());
            OnlineRequest.forgotRequest(Forgot.this,map);
            email.setText("");
        }
    }

    public  void closeactivity()
    {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.resetnow:
               submit();
                break;
        }

    }
}
