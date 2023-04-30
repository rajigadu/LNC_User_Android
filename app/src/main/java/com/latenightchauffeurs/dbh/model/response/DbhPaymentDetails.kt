package com.latenightchauffeurs.dbh.model.response

data class DbhPaymentDetails(
    val base_price: Int,
    val `data`: Any,
    val message: String,
    val status: String,
    val total_fare: Int
)