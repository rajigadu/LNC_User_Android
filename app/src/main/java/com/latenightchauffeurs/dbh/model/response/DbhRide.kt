package com.latenightchauffeurs.dbh.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Create by Siru Malayil on 20-04-2023.
 */
@Parcelize
data class DbhRide(
    val acctid: String? = null,
    val admin_charge: String? = null,
    val booking_type: String? = null,
    val cancel_status: String? = null,
    val cancel_time: String? = null,
    val car_transmission: String? = null,
    val card_id: String? = null,
    val city_pickup: String? = null,
    val city_pickup2: String? = null,
    val created_at: String? = null,
    val date: String? = null,
    val driver_id_for_future_ride: String? = null,
    val driver_status: String? = null,
    val end_date: String? = null,
    val end_time: String? = null,
    val extra_charges: String? = null,
    val feedback_status: String? = null,
    val future_accept: String? = null,
    val future_ride_start: String? = null,
    val hourly_rate_while_ride_completed: String? = null,
    val id: String? = null,
    val notes: String? = null,
    val otherdate: String? = null,
    val pickup_address: String? = null,
    val pickup_lat: String? = null,
    val pickup_long: String? = null,
    val platform: String? = null,
    val promo: String? = null,
    val reason: String? = null,
    val ride_assign_status: String? = null,
    val ride_cancel_by: String? = null,
    val ride_end_time: String? = null,
    val ride_start_time: String? = null,
    val ride_total_minute: String? = null,
    val ride_total_time: String? = null,
    val second: String? = null,
    val status: String? = null,
    val time: String? = null,
    val tip_status: String? = null,
    val user_id: String? = null,
    val distance: String? = null,
    val hourly_rate: String? = null,
    var future_edit_ride_status: String? = null,
    var future_edit_ride_id: String? = null
): Parcelable