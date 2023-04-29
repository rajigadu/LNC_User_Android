package com.latenightchauffeurs.dbh.model.response

data class DbhDriver(
    val `data`: DbhDriverData,
    val message: String,
    val status: String
)