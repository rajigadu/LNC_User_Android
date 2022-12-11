package com.latenightchauffeurs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.ChangePassword;
import com.latenightchauffeurs.activity.ContactUs;
import com.latenightchauffeurs.activity.EditProfile;
import com.latenightchauffeurs.activity.Login;
import com.latenightchauffeurs.activity.WebViewAll;

public class Settings extends Fragment {
    public static String TAG = Settings.class.getName();
    public static Context mcontext;

    public TextView editprofile, changep, privacy, terms, contactus, version;
    public Button logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_settings, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mcontext = getContext();

        editprofile = (TextView) v.findViewById(R.id.editprofile);
        changep = (TextView) v.findViewById(R.id.changepassword);
        privacy = (TextView) v.findViewById(R.id.privacypolicy);
        terms = (TextView) v.findViewById(R.id.terms);
        version = (TextView) v.findViewById(R.id.version);
        contactus = (TextView) v.findViewById(R.id.contactus);

        logout = (Button) v.findViewById(R.id.logout);

        String vername = "";
        try {
            PackageInfo pInfo = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0);
            vername = pInfo.versionName;
            // verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!vername.equalsIgnoreCase("")) {
            version.setText(vername);
        }

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utils(getActivity());
                Utils.startActivity(getActivity(), EditProfile.class);
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utils(getActivity());
                Utils.startActivity(getActivity(), ContactUs.class);
            }
        });

        changep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utils(getActivity());
                Utils.startActivity(getActivity(), ChangePassword.class);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WebViewAll.class);
                i.putExtra("webpage", com.latenightchauffeurs.Utils.Settings.URL_PRIVACY_POLICY);
                i.putExtra("pageheading", "Privacy Policy");
                startActivity(i);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WebViewAll.class);
                i.putExtra("webpage", com.latenightchauffeurs.Utils.Settings.URL_TERMS_CONDITIONS);
                i.putExtra("pageheading", "Terms and  Conditions");
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utils(getActivity());
                Utils.setLogOut(getActivity());
                Utils.startActivity(getActivity(), Login.class);
            }
        });
    }
}