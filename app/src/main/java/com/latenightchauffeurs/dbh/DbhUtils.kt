package com.latenightchauffeurs.dbh

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.latenightchauffeurs.FragmentCallBack

/**
 * Create by Siru Malayil on 12-04-2023.
 */
object DbhUtils {


    fun showAlertDialog(context: Context, message: String, title: String, callback: FragmentCallBack) {
        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton("Ok") { dialogInterface, i ->
                callback.onResult(true)
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }
}