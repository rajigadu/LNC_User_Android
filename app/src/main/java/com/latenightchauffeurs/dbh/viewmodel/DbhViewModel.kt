package com.latenightchauffeurs.dbh.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse
import com.latenightchauffeurs.dbh.model.response.DbhRideHistory
import com.latenightchauffeurs.dbh.model.response.DbhUpcomingRides
import com.latenightchauffeurs.dbh.model.response.DefaultResponseBody
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.repository.DbhRepository
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.util.ArrayList

/**
 * Create by Siru Malayil on 12-04-2023.
 */
class DbhViewModel : ViewModel() {

    private val bookingRepository = DbhRepository()

    fun dbhBookingReservation(bookingData: String): MutableLiveData<Resource<DbhBookingResponse>> {
        return bookingRepository.makeBooking(bookingData)
    }

    fun addNewCard(newCardRequest: ArrayList<MultipartBody.Part>): MutableLiveData<Resource<ResponseBody>> {
        return bookingRepository.addNewCard(newCardRequest)
    }

    fun getCardDetails(userId: String?): MutableLiveData<Resource<ResponseBody>> {
        return bookingRepository.getCardDetails(userId)
    }

    fun applyPromoCode(promoCode: String?): MutableLiveData<Resource<ResponseBody>> {
        return bookingRepository.applyPromoCode(promoCode)
    }

    fun upcomingDbhRides(userId: String?): MutableLiveData<Resource<DbhUpcomingRides>> {
        return bookingRepository.dbhUpcomingRides(userId)
    }

    fun dbhRidesHistory(userid: String?): MutableLiveData<Resource<DbhRideHistory>> {
        return bookingRepository.dbhRidesHistory(userid)
    }

    fun editDbhRide(json: String?): MutableLiveData<Resource<DefaultResponseBody>> {
        return bookingRepository.editDbhRide(json)
    }

    fun cancelDbhRideAmount(json: String?): MutableLiveData<Resource<DefaultResponseBody>> {
        return bookingRepository.cancelDbhRideAmount(json)
    }

    fun cancelDbhRide(json: String?): MutableLiveData<Resource<DefaultResponseBody>> {
        return bookingRepository.cancelDbhRide(json)
    }
}