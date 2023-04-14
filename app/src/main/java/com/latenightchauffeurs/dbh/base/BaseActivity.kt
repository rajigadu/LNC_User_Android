package com.latenightchauffeurs.dbh.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.dbh.utils.AlertDialogMessageFragment
import com.latenightchauffeurs.dbh.utils.ProgressCaller

/**
 * Create by Siru Malayil on 14-04-2023.
 */
abstract class BaseActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        ProgressCaller.hideProgressDialog()
        super.onPause()
    }

    fun showAlertMessageDialog(
        message: String?,
        negativeButton: Boolean = true,
        title: String? = null,
        showRetry: Boolean = false,
        callBack: FragmentCallBack? = null
    ) {
        val msgFragment = AlertDialogMessageFragment.newInstance(
            message = message,
            showRetry = showRetry,
            showNegativeBtn = negativeButton,
            title = title,
            callBack = callBack
        )
        msgFragment.show(supportFragmentManager, "msg")
    }


}