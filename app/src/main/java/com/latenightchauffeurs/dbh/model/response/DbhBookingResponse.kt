package com.latenightchauffeurs.dbh.model.response

data class DbhBookingResponse(
    val data: List<Data>,
    val status: String
)