package com.latenightchauffeurs.dbh.model.response

data class DbhUpcomingRides(
    val bannercount: String,
    val data: List<DbhUpcomingRidesData>,
    val message: String,
    val rating: String,
    val status: String
)