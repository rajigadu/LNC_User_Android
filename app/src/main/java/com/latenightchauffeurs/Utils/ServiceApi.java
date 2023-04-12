package com.latenightchauffeurs.Utils;

import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse;
import com.latenightchauffeurs.model.ChatPojo;
import com.latenightchauffeurs.model.GApiKeyPojo;
import com.latenightchauffeurs.model.StopsList;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by narip on 2/3/2017.
 */
public interface ServiceApi {
    // @POST("{login}")
    // Call<ResponseBody> login(@Path("login") String postfix, @Body RequestBody params);


    @GET("edit-ride-difference.php?")
    Call<ResponseBody> listRepos(@Path("ride_id") String user);



    @GET("{edit-ride-difference.php?}")
    Call<ResponseBody> editRideDifference(@Path("edit-ride-difference.php?") String postfix, @QueryMap HashMap<String, Object> params);

    @GET("{login}")
    Call<ResponseBody> login(@Path("login") String postfix, @QueryMap HashMap<String, Object> params);

    @POST("dbh-booking-reservation.php")
    Call<DbhBookingResponse> dbhBookingReservation(@Body RequestBody body);

    @Headers("Accept: " + "application/json")
    @GET("num-stops-addres-list.php")
    Call<StopsList> fetchStopsList(@Query("rideid") String rideId);

    @Headers("Accept: " + "application/json")
    @GET("chat.php")
    Call<ChatPojo> fetchMsgList(@Query("senderid") String senderid, @Query("recieverid") String recieverid
            , @Query("msg") String msg, @Query("keyvalue") String keyvalue);

    @Headers("Accept: " + "application/json")
    @GET("google-api-key.php")
    Call<GApiKeyPojo> getGoogleApiKey();

   /* @Headers("Accept: " + "application/json")
    @GET("edit-ride.php")
    Call<JSONObject> postUpdateData(@Body JSONObject mainJson);*/

    @GET("{login}")
    Call<JSONObject> postUpdateData(@Path("login") String postfix, @QueryMap HashMap<String, Object> params);
}
