package com.latenightchauffeurs.dbh.model.response

data class DbhRideHistoryData(
    val future_edit_ride_status: List<Any>,
    val ride: List<RideHistory>
)