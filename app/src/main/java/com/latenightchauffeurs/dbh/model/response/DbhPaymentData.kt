package com.latenightchauffeurs.dbh.model.response

data class DbhPaymentData(
    val PayDateTime: String,
    val amount: String,
    val booking_id: String,
    val city_charges: String,
    val extra_charge: String,
    val id: String,
    val payment: String,
    val payment_date: String,
    val pre_share: String,
    val promo_amt: String,
    val ride_amt: String,
    val ride_cancel_by: String,
    val status: String,
    val tip_amount: Any,
    val transaction_id: String,
    val user_id: String
)