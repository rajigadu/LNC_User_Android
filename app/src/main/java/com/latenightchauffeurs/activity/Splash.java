package com.latenightchauffeurs.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.model.SavePref;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;

/**
 * Created by narayana on 10/25/2017.
 */

public class Splash extends AppCompatActivity
{

    private static final String TAG = "Splash";
    Handler handler;
    String currentVersion = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        handler = new Handler();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("lnctoken", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("tokenid", task.getResult());
                editor.apply();
            }
        });

//        try {
////            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
////            currentVersion = pInfo.versionName;
////            int verCode = pInfo.versionCode;
////        } catch (PackageManager.NameNotFoundException e) {
////            e.printStackTrace();
////        }
////
        try
        {
            new GetLatestVersion().execute().get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }



      // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));


      //  CheckUPdate();
    }
//
//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//
//        new Handler().postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                SavePref pref1 = new SavePref();
//                pref1.SavePref(Splash.this);
//
//                String email = pref1.getEmail();
//                String id = pref1.getUserId();
//               // String ulocationinfoid = pref1.getClocation();
//
//                if (!email.equalsIgnoreCase(""))
//                {
//                    Utils.startActivity(Splash.this, Navigation.class);
//                    finish();
//
//                   /* if (!ulocationinfoid.equalsIgnoreCase(""))
//                    {
//                    }
//                    else
//                    {
//                        Utils.startActivity(Splash.this, UserLocation.class);
//                        finish();
//                    }*/
//                }
//                else
//                {
//                    Utils.startActivity(Splash.this, Login.class);
//                    finish();
//                }
//            }
//        }, 3000);
//    }
//






    private String getCurrentVersion()
    {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try
        {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);
        }
        catch (PackageManager.NameNotFoundException e1)
        {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        return currentVersion;
    }

    private class GetLatestVersion extends AsyncTask<String,String, String>
    {
        String latestVersion;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String newVersion = null;

            try
            {
               /* String urlOfAppFromPlayStore ="https://play.google.com/store/apps/details?id=com.qwikride";
                Document  doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();
*/
                Document document = Jsoup.connect("http://play.google.com/store/apps/details?id=" + Splash.this.getPackageName()  + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null)
                {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element)
                    {
                        if (ele.siblingElements() != null)
                        {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets)
                            {
                                newVersion = sibElemet.text();
                                //Log.e("latestversion___123",newVersion);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Log.e("error1231234567",e.getMessage());
                e.printStackTrace();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.e(TAG, "onPostExecute "+s);
            update(s);
        }
    }


    public  void  update(String latestVersion)
    {
        String currentVersion = getCurrentVersion();
        // Log.e("haystack", "Current version = " + currentVersion);

        //If the versions are not the same
        if(latestVersion!=null && !currentVersion.equals(latestVersion))
        {
            // Log.e("version1234567",latestVersion);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("An Update is Available");
            builder.setNeutralButton("Update", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //Click button action


                    //startActivityIfNeeded(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Splash.this.getPackageName())),2);


                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }

                    dialog.dismiss();

//                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                    try {
//                        startActivityIfNeeded(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)), 1);
//                    } catch (android.content.ActivityNotFoundException anfe) {
//                        startActivityIfNeeded(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)), 2);
//                    }
                }
            });



            builder.setCancelable(false);
            builder.show();
        }
        else
        {
           startApp();
        }
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                SavePref pref1 = new SavePref();
                pref1.SavePref(Splash.this);

                String email = pref1.getEmail();
                String id = pref1.getUserId();
               // String ulocationinfoid = pref1.getClocation();

                if (!email.equalsIgnoreCase(""))
                {
                    Utils.startActivity(Splash.this, Navigation.class);
                    finish();

                   /* if (!ulocationinfoid.equalsIgnoreCase(""))
                    {
                    }
                    else
                    {
                        Utils.startActivity(Splash.this, UserLocation.class);
                        finish();
                    }*/
                }
                else
                {
                    Utils.startActivity(Splash.this, Login.class);
                    finish();
                }
            }
        }, 3000);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            Bundle extras = intent.getExtras();
            switch(requestCode)
            {
                case 1:
                    break;
                case 2:
                    break;
            }
        }
        else if (resultCode==RESULT_CANCELED)
        {
//            try
//            {
//                new GetLatestVersion().execute().get();
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//            catch (ExecutionException e)
//            {
//                e.printStackTrace();
//            }
        }

    }












//    private class GetVersionCode extends AsyncTask<Void, String, String> {
//
//        @Override
//
//        protected String doInBackground(Void... voids) {
//
//            String newVersion = null;
//
//            try {
//                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get();
//                if (document != null) {
//                    Elements element = document.getElementsContainingOwnText("Current Version");
//                    for (Element ele : element) {
//                        if (ele.siblingElements() != null) {
//                            Elements sibElemets = ele.siblingElements();
//                            for (Element sibElemet : sibElemets) {
//                                newVersion = sibElemet.text();
//                            }
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return newVersion;
//
//        }
//
//
//        @Override
//
//        protected void onPostExecute(String onlineVersion) {
//
//            super.onPostExecute(onlineVersion);
//
//            if (onlineVersion != null && !onlineVersion.isEmpty()) {
//
//                if (onlineVersion.equals(currentVersion)) {
//
//                } else {
//                    AlertDialog alertDialog = new AlertDialog.Builder(Splash.this).create();
//                    alertDialog.setTitle("Update");
//                    alertDialog.setIcon(R.mipmap.ic_launcher);
//                    alertDialog.setMessage("New Update is available");
//
//                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            try {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//                            } catch (android.content.ActivityNotFoundException anfe) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
//                            }
//                        }
//                    });
//
//                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                    alertDialog.show();
//                }
//
//            }
//
//            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
//
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//    private void CheckUPdate() {
//        VersionChecker versionChecker = new VersionChecker();
//        try
//        {   String appVersionName = BuildConfig.VERSION_NAME;
//            String mLatestVersionName = versionChecker.execute().get();
//            if(!appVersionName.equals(mLatestVersionName)){
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Splash.this);
//                alertDialog.setTitle("Please update your app");
//                alertDialog.setMessage("This app version is no longer supported. Please update your app from the Play Store.");
//                alertDialog.setPositiveButton("UPDATE NOW", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        final String appPackageName = getPackageName();
//                        try {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                        } catch (android.content.ActivityNotFoundException anfe) {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                        }
//                    }
//                });
//                alertDialog.show();
//            }
//
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    public class VersionChecker extends AsyncTask<String, String, String> {
//        private String newVersion;
//        @Override
//        protected String doInBackground(String... params) {
//
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="+getPackageName())
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
////                        .select(".hAyfc .htlgb")
//                        .select("div[itemprop=softwareVersion]")
//                        .get(7)
//                        .ownText();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return newVersion;
//        }
//    }
//
//
//


}

