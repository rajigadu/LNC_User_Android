package com.latenightchauffeurs.dbh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.latenightchauffeurs.dbh.model.response.DbhBookingResponse
import com.latenightchauffeurs.dbh.viewmodel.repository.DbhRepository
import okhttp3.MultipartBody

/**
 * Create by Siru Malayil on 12-04-2023.
 */
class DbhViewModel : ViewModel() {

    private val bookingRepository = DbhRepository()

    fun dbhBookingReservation(bookingData: MultipartBody): LiveData<DbhBookingResponse> {
        return bookingRepository.makeBooking(bookingData)
    }
}