package com.latenightchauffeurs.dbh.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.latenightchauffeurs.Utils.APIClient
import com.latenightchauffeurs.Utils.APIInterface
import com.latenightchauffeurs.Utils.ServiceApi
import com.latenightchauffeurs.Utils.ServiceGenerator
import com.latenightchauffeurs.dbh.model.response.*
import com.latenightchauffeurs.dbh.utils.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Create by Siru Malayil on 12-04-2023.
 */
class DbhRepository {

    private val apiService: ServiceApi = ServiceGenerator.createService(ServiceApi::class.java)
    private var apiInterface = APIClient.getClientVO().create(APIInterface::class.java)

    fun makeBooking(bookingData: String): MutableLiveData<Resource<DbhBookingResponse>> {
        val bookingResult = MutableLiveData<Resource<DbhBookingResponse>>()
        bookingResult.postValue(Resource.loading(null))
        apiService.dbhBookingReservation(bookingData).enqueue(object : Callback<DbhBookingResponse> {
            override fun onResponse(
                call: Call<DbhBookingResponse>,
                response: Response<DbhBookingResponse>) {
                if (response.isSuccessful) {
                    bookingResult.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    bookingResult.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhBookingResponse>, t: Throwable) {
                // handle error
                bookingResult.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return bookingResult
    }

    fun addNewCard(bookingData: ArrayList<MultipartBody.Part>): MutableLiveData<Resource<ResponseBody>> {
        val addCardResult = MutableLiveData<Resource<ResponseBody>>()
        addCardResult.postValue(Resource.loading(null))
        apiInterface.addCard(bookingData).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    addCardResult.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    addCardResult.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // handle error
                addCardResult.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return addCardResult
    }

    fun getCardDetails(userId: String?): MutableLiveData<Resource<ResponseBody>> {
        val addCardResult = MutableLiveData<Resource<ResponseBody>>()
        addCardResult.postValue(Resource.loading(null))
        apiInterface.cardList(userId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    addCardResult.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    addCardResult.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // handle error
                addCardResult.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return addCardResult
    }

    fun applyPromoCode(promoCode: String?): MutableLiveData<Resource<ResponseBody>> {
        val promoCodeResult = MutableLiveData<Resource<ResponseBody>>()
        promoCodeResult.postValue(Resource.loading(null))
        apiService.dbhPromoCheck(promoCode).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    promoCodeResult.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    promoCodeResult.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // handle error
                promoCodeResult.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return promoCodeResult
    }

    fun editDbhRide(editRideRequest: String?): MutableLiveData<Resource<DefaultResponseBody>> {
        val editRideResponse = MutableLiveData<Resource<DefaultResponseBody>>()
        editRideResponse.postValue(Resource.loading(null))
        apiService.dbhEditRide(editRideRequest).enqueue(object : Callback<DefaultResponseBody> {
            override fun onResponse(
                call: Call<DefaultResponseBody>,
                response: Response<DefaultResponseBody>) {
                if (response.isSuccessful) {
                    editRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    editRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DefaultResponseBody>, t: Throwable) {
                // handle error
                editRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return editRideResponse
    }

    fun dbhUpcomingRides(userId: String?): MutableLiveData<Resource<DbhUpcomingRides>> {
        val dbhUpcomingRideResponse = MutableLiveData<Resource<DbhUpcomingRides>>()
        dbhUpcomingRideResponse.postValue(Resource.loading(null))
        apiService.dbhUpcomingRides(userId).enqueue(object : Callback<DbhUpcomingRides> {
            override fun onResponse(
                call: Call<DbhUpcomingRides>,
                response: Response<DbhUpcomingRides>) {
                if (response.isSuccessful) {
                    dbhUpcomingRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    dbhUpcomingRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhUpcomingRides>, t: Throwable) {
                // handle error
                dbhUpcomingRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return dbhUpcomingRideResponse
    }

    //TODO API implementation in ServiceAPI class
    fun dbhRidesHistory(userId: String?): MutableLiveData<Resource<DbhRideHistory>> {
        val dbhUpcomingRideResponse = MutableLiveData<Resource<DbhRideHistory>>()
        dbhUpcomingRideResponse.postValue(Resource.loading(null))
        apiService.dbhRidesHistory(userId).enqueue(object : Callback<DbhRideHistory> {
            override fun onResponse(
                call: Call<DbhRideHistory>,
                response: Response<DbhRideHistory>) {
                if (response.isSuccessful) {
                    dbhUpcomingRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    dbhUpcomingRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhRideHistory>, t: Throwable) {
                // handle error
                dbhUpcomingRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return dbhUpcomingRideResponse
    }

    fun cancelDbhRideAmount(cancelTime: String,rideId: String): MutableLiveData<Resource<DefaultResponseBody>> {
        val dbhUpcomingRideResponse = MutableLiveData<Resource<DefaultResponseBody>>()
        dbhUpcomingRideResponse.postValue(Resource.loading(null))
        apiService.cancelDbhRideAmount(cancelTime, rideId).enqueue(object : Callback<DefaultResponseBody> {
            override fun onResponse(
                call: Call<DefaultResponseBody>,
                response: Response<DefaultResponseBody>) {
                if (response.isSuccessful) {
                    dbhUpcomingRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    dbhUpcomingRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DefaultResponseBody>, t: Throwable) {
                // handle error
                dbhUpcomingRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return dbhUpcomingRideResponse
    }

    fun cancelDbhRide(userId: String, rideId: String, cancelTime: String): MutableLiveData<Resource<DefaultResponseBody>> {
        val cancelRideResponse = MutableLiveData<Resource<DefaultResponseBody>>()
        cancelRideResponse.postValue(Resource.loading(null))
        apiService.cancelDbhRide(userId, rideId, cancelTime).enqueue(object : Callback<DefaultResponseBody> {
            override fun onResponse(
                call: Call<DefaultResponseBody>,
                response: Response<DefaultResponseBody>) {
                if (response.isSuccessful) {
                    cancelRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    cancelRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DefaultResponseBody>, t: Throwable) {
                // handle error
                cancelRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return cancelRideResponse
    }

    fun dbhRideFeedback(json: JSONObject): MutableLiveData<Resource<DefaultResponseBody>> {
        val cancelRideResponse = MutableLiveData<Resource<DefaultResponseBody>>()
        cancelRideResponse.postValue(Resource.loading(null))
        apiService.dbhRideFeedback(
            json.getString("userid"),
            json.getString("driverid"),
            json.getString("rideid"),
            json.getString("msg"),
            json.getString("rating")
        ).enqueue(object : Callback<DefaultResponseBody> {
            override fun onResponse(
                call: Call<DefaultResponseBody>,
                response: Response<DefaultResponseBody>) {
                if (response.isSuccessful) {
                    cancelRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    cancelRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DefaultResponseBody>, t: Throwable) {
                // handle error
                cancelRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return cancelRideResponse
    }

    fun getDbhDriverDetails(driverId: String): MutableLiveData<Resource<DbhDriver>> {
        val cancelRideResponse = MutableLiveData<Resource<DbhDriver>>()
        cancelRideResponse.postValue(Resource.loading(null))
        apiService.getDbhDriverDetails(driverId).enqueue(object : Callback<DbhDriver> {
            override fun onResponse(
                call: Call<DbhDriver>,
                response: Response<DbhDriver>) {
                if (response.isSuccessful) {
                    cancelRideResponse.postValue(Resource.success(response.body()!!))
                } else {
                    // handle error
                    cancelRideResponse.postValue(Resource.error(response.message() ?: "An error occurred", null))
                }
            }
            override fun onFailure(call: Call<DbhDriver>, t: Throwable) {
                // handle error
                cancelRideResponse.postValue(Resource.error(t.localizedMessage ?: "An error occurred", null))
            }
        })

        return cancelRideResponse
    }

}