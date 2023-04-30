package com.latenightchauffeurs.dbh.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DbhRideHistoryData(
    val future_edit_ride_status: List<String>,
    val ride: List<RideHistory>
): Parcelable