package com.latenightchauffeurs.dbh.viewmodel.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.latenightchauffeurs.Utils.ServiceApi
import com.latenightchauffeurs.Utils.ServiceGenerator
import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Create by Siru Malayil on 12-04-2023.
 */
class DbhRepository() {

    private val apiService: ServiceApi = ServiceGenerator.createService(ServiceApi::class.java)

    fun makeBooking(bookingData: MultipartBody): LiveData<DbhBookingResponse> {
        val data = MutableLiveData<DbhBookingResponse>()

        //val json = JSONObject(bookingData).toString()
        //val requestBody = RequestBody.create(MediaType.parse("application/json"), json)

        apiService.dbhBookingReservation(bookingData).enqueue(object : Callback<DbhBookingResponse> {
            override fun onResponse(
                call: Call<DbhBookingResponse>,
                response: Response<DbhBookingResponse>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    // handle error
                }
            }
            override fun onFailure(call: Call<DbhBookingResponse>, t: Throwable) {
                // handle error
            }
        })

        return data
    }
}