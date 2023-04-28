package com.latenightchauffeurs.dbh.model.response

data class DbhBookingResponse(
    val data: List<DbhBookingResponseData>,
    val status: String
)