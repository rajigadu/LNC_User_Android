package com.latenightchauffeurs.dbh.model.response

data class DbhCancelRideAmountResponse(
    val data: List<DbhCancelRideAmountResponseData>,
    val status: String
)