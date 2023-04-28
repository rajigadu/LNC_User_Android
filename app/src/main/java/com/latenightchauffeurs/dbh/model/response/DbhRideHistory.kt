package com.latenightchauffeurs.dbh.model.response

data class DbhRideHistory(
    val data: DbhRideHistoryData,
    val message: String,
    val status: String
)