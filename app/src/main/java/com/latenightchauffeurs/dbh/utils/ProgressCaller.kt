package com.latenightchauffeurs.dbh.utils

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.view.Window
import com.latenightchauffeurs.R
import java.lang.Exception

/**
 * Create by Sirumalayil on 02-04-2023.
 */
object ProgressCaller : Application() {

    private var dialog: Dialog? = null

    fun showProgressDialog(context: Context){

        try {

            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

            if (dialog != null) {
                dialog?.setContentView(R.layout.dialog_view)
                dialog?.setCancelable(false)
                if (dialog?.isShowing != true)
                    dialog?.show()
                else dialog?.dismiss()
            } else {
                dialog = Dialog(context)
            }
        }
        catch (e: Exception) {
            dialog?.dismiss()
        }
    }

    fun hideProgressDialog(){
        try {
            dialog?.dismiss()
            dialog?.hide()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}