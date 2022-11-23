package com.latenightchauffeurs.Utils;


import com.latenightchauffeurs.activity.ItemNotificaitons;
import com.latenightchauffeurs.model.ItemCardList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingHelper {


    public ArrayList<ItemNotificaitons> getNotications(String responseCode) {
        ArrayList<ItemNotificaitons> arrayList = new ArrayList<ItemNotificaitons>();

        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        String id = jsonObjectOuter.getString("id");
                        String title = jsonObjectOuter.getString("title");
                        String message = jsonObjectOuter.getString("message");
                        String date = jsonObjectOuter.getString("date");


                        ItemNotificaitons program = new ItemNotificaitons();
                        program.setId(id);
                        program.setTitle(title);
                        program.setMessage(message);
                        program.setDate(date);

                        arrayList.add(program);
                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }


    public ArrayList<ItemCardList> getCardList(String responseCode) {
        ArrayList<ItemCardList> arrayList = new ArrayList<ItemCardList>();

        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        String gsacard = jsonObjectOuter.getString("gsacard");
                        String profileid = jsonObjectOuter.getString("profileid");
                        String acctid = jsonObjectOuter.getString("acctid");
                        String auoptout = jsonObjectOuter.getString("auoptout");
                        String postal = jsonObjectOuter.getString("postal");
                        String expiry = jsonObjectOuter.getString("expiry");
                        String accttype = jsonObjectOuter.getString("accttype");
                        String token = jsonObjectOuter.getString("token");

                        ItemCardList program = new ItemCardList();


                        program.setGsacard(gsacard);
                        program.setProfileid(profileid);
                        program.setAcctid(acctid);
                        program.setAuoptout(auoptout);
                        program.setPostal(postal);
                        program.setExpiry(expiry);
                        program.setAccttype(accttype);
                        program.setToken(token);

                        arrayList.add(program);
                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }
}
