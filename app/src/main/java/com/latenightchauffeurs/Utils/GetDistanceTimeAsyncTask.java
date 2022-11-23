package com.latenightchauffeurs.Utils;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDistanceTimeAsyncTask extends AsyncTask<Void, Void, String> {

    private String URL = "";
    IonListners ionListners;
    Context context;

    public GetDistanceTimeAsyncTask(Context context, String URL, IonListners ionListners) {
        this.context = context;
        this.URL = URL;
        this.ionListners = ionListners;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utils.initiatePopupWindow(context, "Please wait...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        JSONObject jsonObject = null;
        try {
            URL url = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            jsonObject = new JSONObject(builder.toString());
            // JSONObject main = topLevel.getJSONObject("main");
            // weather = String.valueOf(main.getDouble("temp"));

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (Utils.progressDialog != null) {
            Utils.progressDialog.dismiss();
            Utils.progressDialog = null;
        }

        if (ionListners != null) {
            ionListners.getDistanceTime(result);
        }
    }
}
