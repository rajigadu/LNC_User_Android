package com.latenightchauffeurs.dbh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.repository.DbhRepository

/**
 * Create by Siru Malayil on 12-04-2023.
 */
class DbhViewModel : ViewModel() {

    private val bookingRepository = DbhRepository()

    fun dbhBookingReservation(bookingData: String): MutableLiveData<Resource<DbhBookingResponse>> {
        return bookingRepository.makeBooking(bookingData)
    }
}