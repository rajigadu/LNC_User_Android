package com.latenightchauffeurs.dbh.model.response

data class DbhUpcomingRidesData(
    val ride: List<DbhRide>,
    val future_edit_ride_status: List<FutureEditRideStatus>

)