package com.latenightchauffeurs.Utils;

import androidx.annotation.Nullable;

import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse;
import com.latenightchauffeurs.dbh.model.response.DbhDriver;
import com.latenightchauffeurs.dbh.model.response.DbhPaymentDetails;
import com.latenightchauffeurs.dbh.model.response.DbhRideHistory;
import com.latenightchauffeurs.dbh.model.response.DbhUpcomingRides;
import com.latenightchauffeurs.dbh.model.response.DefaultResponseBody;
import com.latenightchauffeurs.model.ChatPojo;
import com.latenightchauffeurs.model.GApiKeyPojo;
import com.latenightchauffeurs.model.StopsList;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
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

    @POST(Settings.URL_DBH_BOOKING_RESERVATION)
    Call<DbhBookingResponse>dbhBookingReservation(@Query("json") String json);

    @POST(Settings.URL_DBH_EDIT_RIDE)
    Call<DefaultResponseBody>dbhEditRide(@Query("json") String json);

    @GET(Settings.URL_DBH_RIDE_INFO)
    Call<DbhUpcomingRides>dbhUpcomingRides(@Query("userid") String userId);


    @GET(Settings.URL_DBH_RIDE_HISTORY)
    Call<DbhRideHistory>dbhRidesHistory(@Query("userid") String userId);

    @GET(Settings.URL_DBH_PROMO_CHECK)
    Call<ResponseBody>dbhPromoCheck(@Query("promo") String userId);

    @GET(Settings.URL_DBH_CANCEL_FUTURE_RIDE)
    Call<DefaultResponseBody>cancelDbhRide(
            @Query("userid") String userId,
            @Query("ride_id") String rideId,
            @Query("cancel_time") String cancelTime
    );

    @GET(Settings.URL_DBH_RIDE_FEEDBACK)
    Call<DefaultResponseBody>dbhRideFeedback(
            @Query("userid") String userId,
            @Query("rideid") String rideId,
            @Query("driverid") String driverId,
            @Query("msg") String message,
            @Query("rating") String rating,
            @Query("tip") String tip,
            @Query("percentage") String percentage
    );

    @GET(Settings.URL_DBH_DRIVER_DETAILS)
    Call<DbhDriver>getDbhDriverDetails(
            @Query("driverid") String driverId
    );

    @GET(Settings.URL_DBH_CANCEL_RIDE_AMOUNT)
    Call<DefaultResponseBody>cancelDbhRideAmount(
            @Query("cancel_time") String cancelTime,
            @Query("ride_id") String rideId
    );

    @GET(Settings.URL_DBH_PAYMENT_DETAILS)
    Call<DbhPaymentDetails>dbhPaymentDetails(
            @Query("userid") String userId,
            @Query("ride_id") String rideId
    );

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
