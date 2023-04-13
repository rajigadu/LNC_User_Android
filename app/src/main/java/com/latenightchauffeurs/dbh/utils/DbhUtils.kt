package com.latenightchauffeurs.dbh.utils

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.latenightchauffeurs.FragmentCallBack

/**
 * Create by Siru Malayil on 12-04-2023.
 */
object DbhUtils {

    const val ACTION_OK = true
    const val ACTION_CANCEL = false

    fun showAlertDialog(context: Context, message: String? = null, title: String? = null,
                        callback: FragmentCallBack, showCancel: Boolean = false) {
        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setMessage(message ?: "Oops!... Something went wrong, please try again later!")
            .setTitle(title ?: "Please note")
            .setPositiveButton("Ok") { dialogInterface, i ->
                callback.onResult(ACTION_OK)
                dialogInterface.dismiss()
            }
            .setPositiveButton("Cancel") { dialogInterface, i ->
                callback.onResult(ACTION_CANCEL)
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }
}