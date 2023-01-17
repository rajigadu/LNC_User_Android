package com.latenightchauffeurs.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.latenightchauffeurs.R;
import com.latenightchauffeurs.Utils.Utils;
import com.latenightchauffeurs.activity.ActivityChat;
import com.latenightchauffeurs.activity.Navigation;
import com.latenightchauffeurs.activity.Notifications;
import com.latenightchauffeurs.model.SavePref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by AnaadIT on 3/30/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    MediaPlayer mp;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log.e(TAG, "NotificationMessageBody:================= " + remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0)
            if (remoteMessage.getData().get("data") != null && !Objects.requireNonNull(remoteMessage.getData().get("data")).equalsIgnoreCase("")) {
                try {
                    JSONObject json = new JSONObject(Objects.requireNonNull(remoteMessage.getData().get("data")));
                    String title = json.getString("title").toString();
                    //String body=json.getString("body").toString();

                    JSONArray jarray = json.optJSONArray("body");

                    if (jarray != null) {
                        Utils.global.mapMain = Utils.GetJsonDataIntoMap(getApplicationContext(), jarray, "");
                    }

                    Log.e(TAG, "title Message========123456: " + title);
                    Log.e(TAG, "body map  Message========1234567: " + Utils.global.mapMain);

                    sendNotification(title, Utils.global.mapMain);
                } catch (Exception e) {
                    Log.e("error1233", e.getMessage());
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("lnctoken", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("tokenid", token);
        editor.apply();
    }

    private void sendNotification(String title, HashMap<String, Object> map) {
        Intent intent = null;
        if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newusermessage")) {
            intent = new Intent(this, ActivityChat.class);
            intent.putExtra("back", "back");
        } else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("newfutureride")) {
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 1;
        }else if (map.containsKey("ride") && map.get("ride").toString().equalsIgnoreCase("palert")) {
            intent = new Intent(this, Navigation.class);
            Navigation.nid = 1;
        }else if (map.containsKey("user") && map.get("user").toString().equalsIgnoreCase("richnotification")) {
            intent = new Intent(this, Notifications.class);
            intent.putExtra("back", "back");

            SavePref pref = new SavePref();
            pref.SavePref(getApplicationContext());

            int ii = pref.getBadgeCount();

            Log.e(TAG, "QQQQiii "+ii);

            int xx = ii + 1;

            Log.e(TAG, "QQQQxx "+xx);


            pref.setBadgeCount(xx);

        }else {
            intent = new Intent(this, Navigation.class);
        }



        intent.putExtra("map", map);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int uniqueId = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            String channelID = "my_channel_01";// The id of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelID, "My_Name", importance);
            // Create a notification and set the notification channel.
            Notification notification = getNotificationBuilder(title, pendingIntent, map)
                    .setChannelId(channelID)
                    .build();

            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(uniqueId, notification);
        } else if (notificationManager != null) {
            NotificationCompat.Builder notificationBuilder = getNotificationBuilder(title, pendingIntent, map);
            notificationManager.notify(uniqueId /* ID of notification */,
                    notificationBuilder.build());
        }

        Intent broadcast = new Intent();
        broadcast.setAction("OPEN_NEW_ACTIVITY");
        broadcast.putExtra("isid", "9");
        broadcast.putExtra("status", "9");
        broadcast.putExtra("data", (Serializable) map);
        sendBroadcast(broadcast);
    }

    private NotificationCompat.Builder getNotificationBuilder(String title, PendingIntent pendingIntent,
                                                              HashMap<String, Object> map) {

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.appicon);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
      /*if(Utils.global.mapMain.containsKey(ConstVariable.MESSAGE)&&!Utils.global.mapMain.get(ConstVariable.MESSAGE).toString().equalsIgnoreCase(""))
        {
            msg=Utils.global.mapMain.get(ConstVariable.MESSAGE).toString();
        }*/
        String body = "";

        if (map.containsKey("message") && !map.get("message").toString().equalsIgnoreCase(""))
            body = map.get("message").toString();

        bigText.bigText(body);
        return new NotificationCompat.Builder(this)
                /*.setSmallIcon(image)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);*/

                .setSmallIcon(R.drawable.appicon)
                .setStyle(bigText)
                .setLargeIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setContentText(body);
    }
}