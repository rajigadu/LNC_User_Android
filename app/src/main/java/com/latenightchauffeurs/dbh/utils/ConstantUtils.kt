package com.latenightchauffeurs.dbh.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by Siru Malayil on 28-04-2023.
 */
object ConstantUtils {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateAndTime(): String? {
        val todayDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm a")
        return formatter.format(todayDate)
    }
}