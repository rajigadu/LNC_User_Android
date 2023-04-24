package com.latenightchauffeurs.dbh.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FutureEditRideStatus(
    val future_edit_ride_status: String,
    val id: String
): Parcelable