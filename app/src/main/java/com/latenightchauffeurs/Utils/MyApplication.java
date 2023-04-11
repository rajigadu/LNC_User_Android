package com.latenightchauffeurs.Utils;

import android.app.Application;
import android.content.Context;
import com.cardconnect.consumersdk.CCConsumer;
import com.cardconnect.consumersdk.network.CCConsumerApi;

import java.util.HashMap;

/**
 * Created by narayana on 3/16/2018.
 */

public class MyApplication extends Application {


    public HashMap<String, Object> map;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
      //  Fabric.with(this, new Crashlytics());
        context = getApplicationContext();

        setupConsumerApi();
    }


    public static CCConsumerApi getConsumerApi() {
        return CCConsumer.getInstance().getApi();
    }




//    public static CCConsumerAndroidPayGetTokenRequest getConsumerApi1() {
//        return CCConsumerAndroidPayConfiguration.getInstance().;
//    }

    public static Context getContext() {
        return context;
    }


    private void setupConsumerApi() {

        CCConsumer.getInstance().getApi().setEndPoint("https://fts.cardconnect.com:8443/cardsecure/cs");
        CCConsumer.getInstance().getApi().setDebugEnabled(true);




        }


}
