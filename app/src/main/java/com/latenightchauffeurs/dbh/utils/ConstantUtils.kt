package com.latenightchauffeurs.dbh.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import com.latenightchauffeurs.R
import com.latenightchauffeurs.dbh.model.response.DbhRide
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by Siru Malayil on 28-04-2023.
 */
object ConstantUtils {

    enum class RideStatus {
        PENDING,
        ACCEPTED,
        RIDE_STARTED
    }

    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it ->
        it.lowercase().replaceFirstChar { it.uppercase() }
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateAndTime(): String? {
        val todayDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm a")
        return formatter.format(todayDate)
    }

    fun getRideStatus(ride: DbhRide?): String {
        return if (ride?.future_accept == "0") RideStatus.PENDING.name
        else if (ride?.future_ride_start == "1" &&
            ride.status == "1") RideStatus.RIDE_STARTED.name
        else RideStatus.ACCEPTED.name
    }

    fun rideStatusTextColor(ride: DbhRide?, context: Context): Int {
        return when(getRideStatus(ride)) {
            RideStatus.PENDING.name -> ContextCompat.getColor(context, R.color.yellow)
            RideStatus.ACCEPTED.name,
            RideStatus.RIDE_STARTED.name -> ContextCompat.getColor(context, R.color.green_color)
            else -> ContextCompat.getColor(context, R.color.black)
        }
    }

}