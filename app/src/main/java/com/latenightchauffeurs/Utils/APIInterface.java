package com.latenightchauffeurs.Utils;



import java.util.ArrayList;
import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {




    @GET("temporary-ride-detail.php?")
    Call<ResponseBody>
    getSecondDriver(
            @Query("driverid") String devicetoken,
            @Query("userid") String userid,
            @Query("type") String device_type
    );


    @GET("signup.php?")
    Call<ResponseBody>
    register(
            @Query("user_full_name") String user_full_name,
            @Query("user_mobile") String user_mobile,
            @Query("user_email") String user_email,
            @Query("user_password") String user_password
//            @Query("card_number") String card_number,
//            @Query("card_type") String card_type,
//            @Query("card_exp_date") String card_exp_date,
//            @Query("cvv") String cvv
    );

    @GET("social-signup.php?")
    Call<ResponseBody>
    socialRegister(
            @Query("full_name") String full_name,
            @Query("email") String email,
            @Query("purl") String card_exp_date
    );


    @GET("user-detail.php?")
    Call<ResponseBody>
    userProfile(
            @Query("userid") String full_name
    );





    @GET("find-drivers.php?")
    Call<ResponseBody>
    findDrivers(
            @Query("user_id") String user_id,
            @Query("pickup_location") String pickup_location,
            @Query("trip_detail") String trip_detail,
            @Query("trip_date") String trip_date,
            @Query("trip_time") String trip_time,
            @Query("pickup_lat") String pickup_lat,
            @Query("pickup_long") String pickup_long,
            @Query("drop_lat") String drop_lat,
            @Query("drop_long") String drop_long
    );


    @GET("send-request.php?")
    Call<ResponseBody>
    sendRequest(
            @Query("user_id") String user_id,
            @Query("driver_id") String pickup_location,
            @Query("trip_id") String trip_id,
            @Query("card_id") String card_id,
            @Query("total_cost") String total_cost,
            @Query("booking_cost") String booking_cost,
            @Query("pending_cost") String pending_cost
    );



    @GET("user-send_request-list.php?")
    Call<ResponseBody>
    userSendRequesList(
            @Query("userid") String username
    );


    @GET("user-booking-list.php?")
    Call<ResponseBody>
    bookingList(
            @Query("userid") String username
    );


    @GET("cancel-ride.php?")
    Call<ResponseBody>
    cancelRide(
            @Query("userid") String userid,
            @Query("ride_id") String ride_id
    );



    @Multipart
    @POST("uploadfile.php?")
    Call<ResponseBody> uploadFile(
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile,
            @Part MultipartBody.Part file
    );



    @Multipart
    @POST("login")
    Call<ResponseBody> uploadFile1(
            @Part("csrf_quaiq_insurance") RequestBody user_id,
            @Part("user_login") RequestBody name,
            @Part("user_password") RequestBody mobile
    );


    @Multipart
    @POST("chk_apiresponse")
    Call<ResponseBody> uploadFile1(@Part("csrf_quaiq_insurance") RequestBody csrf_quaiq_insurance, @Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("chk_apiresponse")
    Call<ResponseBody> uploadFile1(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("update-profile.php?")
    Call<ResponseBody>
    updateProfile(
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile
            ,
            @Part MultipartBody.Part bodyrofileURL
    );


    @GET("forgot-password.php?")
    Call<ResponseBody> forgotPassword(
            @Query("email_address") String userid
    );




    @GET("ride-detail.php?")
    Call<ResponseBody> rideDetail(
            @Query("userid") String driverid
    );




    @GET("driver-rating.php?")
    Call<ResponseBody>
    feedback(
            @Query("userid") String userid,
            @Query("driverid") String driverid,
            @Query("trip_id") String rideId,
            @Query("msg") String message,
            @Query("rating") String value
    );


    @GET("customer-feedback.php?")
    Call<ResponseBody> customerFeedback(
            @Query("driverid") String driverid
    );




    @GET("contact-us.php?")
    Call<ResponseBody>
    contactUs(
            @Query("fullname") String name,
            @Query("email") String emailid,
            @Query("message") String message
    );



    @GET("driver-info.php?")
    Call<ResponseBody>
    driverInfo(
            @Query("driverid") String full_name
    );




    @GET("paypal-add-card/paypal/rest-api-sdk-php/sample/vault/addCreditCard.php?")
    Call<ResponseBody>
    addCard(
            @Query("userid") String driver_id,
            @Query("username") String username,
            @Query("useremail") String useremail,
            @Query("cardName") String cardName,
            @Query("cardNumber") String cardNumber,
            @Query("cardType") String cardType,
            @Query("ExpireMonth") String ExpireMonth,
            @Query("ExpireYear") String ExpireYear,
            @Query("CVV") String CVV
    );




    @GET("user-payment-history.php?")
    Call<ResponseBody>
    userPaymentHistory(
            @Query("userid") String userid);


    @GET("user-card-list.php?")
    Call<ResponseBody>
    userCardList(
            @Query("userid") String userid);




    @GET("user-remove-card.php?")
    Call<ResponseBody>
    removeCard(
            @Query("userid") String userid,
            @Query("card_id") String card_id
    );




    @GET("chat.php?")
    Call<ResponseBody>
    userChat(
            @Query("senderid") String senderid,
            @Query("recieverid") String recieverid,
            @Query("trip_id") String trip_id,
            @Query("msg") String msg,
            @Query("keyvalue") String keyvalue);



   // @Multipart
   // @FormUrlEncoded
    @POST("request-fire-department.php")
    Call<ResponseBody> uploadMultiFile(@Body RequestBody file);






    @Multipart
    @POST("social_login")
    Call<ResponseBody> socialLogin(@Part ArrayList<MultipartBody.Part> arrayListMash);



    @Multipart
    @POST("login")
    Call<ResponseBody> login(@Part ArrayList<MultipartBody.Part> arrayListMash);


    @Multipart
    @POST("add_address")
    Call<ResponseBody> addAddress(@Part ArrayList<MultipartBody.Part> arrayListMash);


    @Multipart
    @POST("update_address")
    Call<ResponseBody> updateAddress(@Part ArrayList<MultipartBody.Part> arrayListMash);


    @Multipart
    @POST("remove_address")
    Call<ResponseBody> removeAddress(@Part ArrayList<MultipartBody.Part> arrayListMash);


    @Multipart
    @POST("signup")
    Call<ResponseBody> signup(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("ic_settings")
    Call<ResponseBody> icSettings(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("ic_get_settings")
    Call<ResponseBody> icGetSettings(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("get_car_by_vin")
    Call<ResponseBody> getCarByVin(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("service_brand_list")
    Call<ResponseBody> getBSTYPE(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("mc_selected_service_brand_list")
    Call<ResponseBody> mcSelectedServiceBrandList(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("mc_selected_services")
    Call<ResponseBody> mcSelectedServices(@Part ArrayList<MultipartBody.Part> hashMap);





    @Multipart
    @POST("mc_vo_selected_brand_list")
    Call<ResponseBody> mcSelectedServiceList(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("mc_vo_selected_services_list")
    Call<ResponseBody> mcVoSelectedServicesList(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("normal_categories")
    Call<ResponseBody> normalCategories(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("seasonal_services")
    Call<ResponseBody> seasonalServices(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("featured_services")
    Call<ResponseBody> featuredServices(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("services_details")
    Call<ResponseBody> seasonalServicesDetail(@Part ArrayList<MultipartBody.Part> hashMap);


   // @Multipart
    @GET("token")
    Call<ResponseBody> token();


    @Multipart
    @POST("forget_password")
    Call<ResponseBody> forgotPassword(@Part ArrayList<MultipartBody.Part> arrayListMash);


    @Multipart
    @POST("my_cars_list")
    Call<ResponseBody> myCarsList(@Part ArrayList<MultipartBody.Part> arrayListMash);

    @Multipart
    @POST("delete_my_car")
    Call<ResponseBody> deleteCar(@Part ArrayList<MultipartBody.Part> arrayListMash);


    @Multipart
    @POST("remove_car_media")
    Call<ResponseBody> deleteCarMedia(@Part ArrayList<MultipartBody.Part> arrayListMash);



    @Multipart
    @POST("set_default_car")
    Call<ResponseBody> setDefaultCar(@Part ArrayList<MultipartBody.Part> arrayListMash);

    @Multipart
    @POST("set_default_address")
    Call<ResponseBody> setDefaultAddress(@Part ArrayList<MultipartBody.Part> arrayListMash);





    @Multipart
    @POST("view_car_details")
    Call<ResponseBody> viewCarDetail(@Part ArrayList<MultipartBody.Part> arrayListMash);


    @Multipart
    @POST("add_single_accident")
    Call<ResponseBody> addSingleAccident(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("upload_existing_request_as_new")
    Call<ResponseBody> uploadExistingRequestAsNew(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("advertise_with_us")
    Call<ResponseBody> advertiseWithUs(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("advertise_update_vo")
    Call<ResponseBody> advertiseUpdateUs(@Part ArrayList<MultipartBody.Part> hashMap);



//    @Multipart
//    @POST("advertise_with_us_vo")
//    Call<ResponseBody> advertiseWithUsVO(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("advertise_with_us_common")
    Call<ResponseBody> advertiseWithUsVO(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("add_new_vehicle")
    Call<ResponseBody> addCar(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("update_car_media")
    Call<ResponseBody> updateCarMedia(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("update_insurance")
    Call<ResponseBody> updateInsurance(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("update_car_details")
    Call<ResponseBody> updateCarDetails(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("add_car_media")
    Call<ResponseBody> addNewMedia(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("change_password")
    Call<ResponseBody> changePassword(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("get_user_profile")
    Call<ResponseBody> getUserProfile(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("push_notification")
    Call<ResponseBody> pushNotification(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("add_update_device_token")
    Call<ResponseBody> addUpdateDeviceToken(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("update_brand_services")
    Call<ResponseBody> updateBrandService(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("update_profile")
    Call<ResponseBody> getUserProfileUpdate(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("mc_serach_results")
    Call<ResponseBody> mcSerachResults(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("vo_serach_results")
    Call<ResponseBody> voSerachResults(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("mc_update_service_brand")
    Call<ResponseBody> mcUpdateServiceBrand(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("vo_active_bids")
    Call<ResponseBody> voActiveBids(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("vo_active_bids_2")
    Call<ResponseBody> voActiveBids2(@Part ArrayList<MultipartBody.Part> hashMap);


//    @Multipart
    @GET("ads_by_screen")
    Call<ResponseBody> adsByScreen();


    @Multipart
    @POST("browse_vo_accepted_jobs")
    Call<ResponseBody> browseVoAcceptedJobs(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("browse_vo_pending_jobs_details")
    Call<ResponseBody> browseVoPendingJobsDetails(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("browse_vo_completed_history_details")
    Call<ResponseBody> browseVoCompletedHistoryDetails(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("browse_ic_completed_history_details")
    Call<ResponseBody> browseICCompletedHistoryDetails(@Part ArrayList<MultipartBody.Part> hashMap);




    @Multipart
    @POST("browse_mc_completed_history_details")
    Call<ResponseBody> browseMCCompletedHistoryDetails(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("browse_vo_active_jobs_details")
    Call<ResponseBody> browseVoActiveJobsDetails(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("browse_vo_pending_job_bidders")
    Call<ResponseBody> browseVoPendingJobBidders(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("delete_message")
    Call<ResponseBody> deleteChatUser(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("mark_unread")
    Call<ResponseBody> markReadChatUser(@Part ArrayList<MultipartBody.Part> hashMap);




    @Multipart
    @POST("vo_view_mc_full_details")
    Call<ResponseBody> voViewMcFullDetails(@Part ArrayList<MultipartBody.Part> hashMap);




    @Multipart
    @POST("vo_active_bid_details")
    Call<ResponseBody> voActiveBidsDetail(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("view_vehicle_bids")
    Call<ResponseBody> viewVehicleBids(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("view_vehicle_bids_bidder")
    Call<ResponseBody> viewVehicleBidsBidder(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("accept_bid")
    Call<ResponseBody> acceptBid(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("remove_bid")
    Call<ResponseBody> removeBid(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("vo_remove_self_active_job")
    Call<ResponseBody> voRemoveSelfActiveJob(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("vo_unhide_active_bids")
    Call<ResponseBody> voUnhideActiveBids(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("vo_hide_active_bid_2")
    Call<ResponseBody> hideBid(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("view_history")
    Call<ResponseBody> viewHistory(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("highest_to_lowest_bids")
    Call<ResponseBody> highestToLowestBids(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("list_advertise")
    Call<ResponseBody> listAdvertise(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("pause_my_aid")
    Call<ResponseBody> pauseMyAid(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("remove_my_aid")
    Call<ResponseBody> removeMyAid(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("list_my_aids")
    Call<ResponseBody> listMyAids(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("list_advertise_package")
    Call<ResponseBody> listAdvertisePackage(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("countries")
    Call<ResponseBody> countries(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("list_cities_by_countries")
    Call<ResponseBody> states(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("count_new_message")
    Call<ResponseBody> countNewMessage(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("vo_list_mc_requests")
    Call<ResponseBody> voListMcRequests(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("mc_change_requested_bid_price")
    Call<ResponseBody> mcChangeRequestedBidPrice(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("vo_manage_mc_updated_price_request")
    Call<ResponseBody> voManageMcUpdatedPriceRequest(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("remove_accepted_bid_and_repost_for_bidding")
    Call<ResponseBody> removeAcceptedBidRepostForBidding(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("request_mc_to_change_his_requested_bid_price")
    Call<ResponseBody> requestMcChangeHisRequestedBidPrice(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("mc_list_subscription_screen")
    Call<ResponseBody> mcListSubscriptionScreen(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("subscribe_screen_mc")
    Call<ResponseBody> subscribeScreenMc(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("set_user_rating")
    Call<ResponseBody> setUserRating(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("view_vehicle_accepted_bids")
    Call<ResponseBody> viewVehicleAcceptedBids(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("post_job_status")
    Call<ResponseBody> postBobStatus(@Part ArrayList<MultipartBody.Part> hashMap);





    @Multipart
    @POST("browse_jobs")
    Call<ResponseBody> browseJobs(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("mc_delete_active_bid")
    Call<ResponseBody> removeMCBid(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("view_job_details")
    Call<ResponseBody> viewJobDetails(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("mc_view_self_job_bid_details")
    Call<ResponseBody> mcViewSelfJobBidDetails(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("mc_view_self_job_bid_details_pending")
    Call<ResponseBody> mcViewSelfJobBidDetailsPending(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("post_job_offer")
    Call<ResponseBody> postJobOffer(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("update_job_offers_price")
    Call<ResponseBody> updateJobOffersPrice(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("post_another_job_offer")
    Call<ResponseBody> postAnotherJobOffer(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("post_multiple_job_offers")
    Call<ResponseBody> postMultipleJobOffers(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("update_multiple_job_offers")
    Call<ResponseBody> postMultipleJobOffers2(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("update_opne_multiple_job_offers")
    Call<ResponseBody> updateOpneMultipleJobOffers(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("cancel_active_bid")
    Call<ResponseBody> cancelActiveBid(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("complete_multiple_job_status")
    Call<ResponseBody> completeMultipleJobOffers(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("view_pending_job_details")
    Call<ResponseBody> viewPendingJobDetails(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("view_completed_job_details")
    Call<ResponseBody> viewCompletedJobDetails(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("complete_job_ic")
    Call<ResponseBody> completeJobIc(@Part ArrayList<MultipartBody.Part> hashMap);


//    @Multipart
//    @POST("browse_jobs")
//    Call<ResponseBody> activeJobs(@Part ArrayList<MultipartBody.Part> hashMap);





    @Multipart
    @POST("view_active_bids")
    Call<ResponseBody> viewActiveBids(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("browse_mc_jobs_with_bids")
    Call<ResponseBody> viewActiveBids2(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("browse_mc_completed_jobs")
    Call<ResponseBody> viewActiveBids3(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("list_insurance_cmp")
    Call<ResponseBody> listInsuranceCmp(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("manual_insurance")
    Call<ResponseBody> manualInsurance(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("ic_actionable_claims_list")
    Call<ResponseBody> icActionableClaimsList(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("ic_non_actionable_claims_list")
    Call<ResponseBody> icNonActionableClaimsList(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("ic_actionable_claims_details")
    Call<ResponseBody> icActionableClaimsDetail(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("ic_active_claims_details")
    Call<ResponseBody> icActiveClaimsDetails(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("ic_actionable_open_bids_claims_list")
    Call<ResponseBody> icActiveBidsList(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST("ic_actionable_accepted_bids_claims_list")
    Call<ResponseBody> icWorkInProgressList(@Part ArrayList<MultipartBody.Part> hashMap);




    @Multipart
    @POST("ic_actionable_complete_bids_claims_list")
    Call<ResponseBody> icCompleteList(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("ic_pending_claims_details")
    Call<ResponseBody> icWorkInProgressDetail(@Part ArrayList<MultipartBody.Part> hashMap);




    @Multipart
    @POST("view_ic_jobs")
    Call<ResponseBody> icHistoryList(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("link_mc_to_ic")
    Call<ResponseBody> linkMcToIc(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("list_mc_links")
    Call<ResponseBody> listMcLinks(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("list_all_mechanics")
    Call<ResponseBody> listAllMechanics(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("send_message")
    Call<ResponseBody> sendMessage(@Part ArrayList<MultipartBody.Part> hashMap);

    @Multipart
    @POST("msg_status_read")
    Call<ResponseBody> msgStatusRead(@Part ArrayList<MultipartBody.Part> hashMap);





//    @GET("active-reward-program.php?")
//    Call<ResponseBody>
//    activeRewardProgram1(
//            @Query("driver_id") String userid);






    @Multipart
    @POST("active-reward-program.php?")
    Call<ResponseBody> activeRewardProgram(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("reward-program-list.php?")
    Call<ResponseBody> rewardProgramList(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("my-reward-program-list.php?")
    Call<ResponseBody> myRewardProgramList(@Part ArrayList<MultipartBody.Part> hashMap);


    @GET("card-list.php?")
    Call<ResponseBody> cardList(@Query("userid") String userid);


    @Multipart
    @POST("history-reward-program-list.php?")
    Call<ResponseBody> historyRewardProgramList(@Part ArrayList<MultipartBody.Part> hashMap);



    @Multipart
    @POST(":6443/cardconnect/rest")
    Call<ResponseBody> historyRewardProgramList111(@Part ArrayList<MultipartBody.Part> hashMap);


    @Multipart
    @POST("cardconnect/rest")
    Call<ResponseBody> uploadFile1();



    @Multipart
    @POST("add-cc-card.php?")
    Call<ResponseBody> addCard(@Part ArrayList<MultipartBody.Part> hashMap);




    @GET("notification-message-list.php?")
    Call<ResponseBody>
    getNotification();


}
