package com.latenightchauffeurs.fcm;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by AnaadIT on 3/30/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    public  int i=0;
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        if ((refreshedToken != null || refreshedToken != "") && i == 0)
            sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token)
    {
        /*SavePref pref1 = new SavePref();
        pref1.SavePref(getApplicationContext());
        pref1.setdToken(token);*/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("lnctoken", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("tokenid", token);
        editor.commit();

        // Log.e(TAG, "db token token123456: " +pref1.getdToken());
        i=1;
    }
}
