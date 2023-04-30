package com.latenightchauffeurs.dbh.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DbhRideHistory(
    val data: DbhRideHistoryData,
    val message: String,
    val status: String
): Parcelable