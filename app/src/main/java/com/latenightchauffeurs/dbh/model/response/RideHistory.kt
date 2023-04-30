package com.latenightchauffeurs.dbh.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RideHistory(
    val acctid: String,
    val admin_charge: String,
    val booking_type: String,
    val cancel_status: String,
    val cancel_time: String,
    val car_transmission: String,
    val card_id: String,
    val city_pickup: String,
    val city_pickup2: String,
    val created_at: String,
    val date: String,
    val driver_id_for_future_ride: String,
    val driver_status: String,
    val end_date: String,
    val end_time: String,
    val extra_charges: String,
    val feedback_status: String,
    val future_accept: String,
    val future_ride_start: String,
    val hourly_rate: String,
    val hourly_rate_while_ride_completed: String,
    val id: String,
    val notes: String,
    val otherdate: String,
    val pickup_address: String,
    val pickup_lat: String,
    val pickup_long: String,
    val platform: String,
    val promo: String,
    val reason: String,
    val ride_assign_status: String,
    val ride_cancel_by: String,
    val ride_end_time: String,
    val ride_start_time: String,
    val ride_total_minute: String,
    val ride_total_time: String,
    val second: String,
    val status: String,
    val time: String,
    val tip_status: String,
    val user_id: String
): Parcelable