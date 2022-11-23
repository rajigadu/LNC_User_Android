package com.latenightchauffeurs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import android.Manifest;

import java.util.HashMap;

/**
 * Created by AnaadIT on 3/16/2017.
 */

public class ChangePassword extends AppCompatActivity implements View.OnClickListener
{
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.newpassword)
    EditText newpassword;

    @BindView(R.id.oldpassword)
    EditText oldpassword;

    @BindView(R.id.cpassword)
    EditText cpassword;

    @BindView(R.id.updatepassword)
    Button updatepassword;

    public  static ChangePassword Instance;
    HashMap<String,Object> map;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ButterKnife.bind(this);
        Instance=this;

        back.setOnClickListener(this);

        title.setText("Change Password");

        updatepassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                submit();
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    public  void successPassword()
    {
        finish();
    }

    public  void  submit()
    {
        new Utils(ChangePassword.this);

        if (oldpassword.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter old password.",ChangePassword.this);
        }
        else  if(newpassword.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter new password.",ChangePassword.this);
        }
        else  if(cpassword.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Please enter conform password again.",ChangePassword.this);
        }
        else  if(!cpassword.getText().toString().trim().equalsIgnoreCase(newpassword.getText().toString().trim()))
        {
            Utils.toastTxt("Password and confirm password must be same.",ChangePassword.this);
        }
        else
        {
            map=new HashMap<>();
            map.put("old_password",oldpassword.getText().toString());
            map.put("new_password",cpassword.getText().toString());
            OnlineRequest.changePawwordRequest(ChangePassword.this,map);
            oldpassword.setText("");
            newpassword.setText("");
            cpassword.setText("");
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
            case R.id.back:
                finish();
                break;


        }

    }
}
