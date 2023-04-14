package com.latenightchauffeurs.dbh.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.latenightchauffeurs.Utils.APIClient
import com.latenightchauffeurs.Utils.APIInterface
import com.latenightchauffeurs.Utils.ServiceApi
import com.latenightchauffeurs.Utils.ServiceGenerator
import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse
import com.latenightchauffeurs.dbh.utils.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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
}