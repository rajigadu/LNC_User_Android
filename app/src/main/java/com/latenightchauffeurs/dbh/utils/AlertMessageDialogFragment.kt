package com.latenightchauffeurs.dbh.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.FragmentDialogViewBinding


/**
 * Create by Siru Malayil on 14-04-2023.
 */
class AlertDialogMessageFragment : DialogFragment() {

    private var binding: FragmentDialogViewBinding? = null
    private var callBack: FragmentCallBack? = null
    private var showRetry: Boolean = false
    private var showNegativeBtn: Boolean = false
    private var title: String? = "LateNightChauffeurs"
    private var message: String? = "Oops! Something went wrong, please try again later"


    companion object {
        const val ACTION_OK = true
        const val ACTION_CANCEL = false
        const val ACTION_RETRY = true

        fun newInstance(
            showNegativeBtn: Boolean = false,
            showRetry: Boolean = false,
            title: String? = null,
            message: String? = null,
            callBack: FragmentCallBack? = null
        ) = AlertDialogMessageFragment().apply {
            this.callBack = callBack
            this.showNegativeBtn = showNegativeBtn
            this.showRetry = showRetry
            this.message = message
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            isCancelable = false
            setStyle(STYLE_NO_FRAME, android.R.style.Theme)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        binding = FragmentDialogViewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvHeaderTitle?.text = title
        binding?.tvMessage?.text = message
        binding?.btnNegative?.isVisible = showNegativeBtn

        if (showRetry) {
            binding?.btnPositive?.isVisible = false
            binding?.btnRetry?.isVisible = true
        }
        if (!showNegativeBtn) {
            binding?.btnNegative?.isVisible = false
        }

        binding?.btnPositive?.setOnClickListener {
            callBack?.onResult(ACTION_OK)
            dismissAllowingStateLoss()
        }
        binding?.btnNegative?.setOnClickListener {
            callBack?.onResult(ACTION_CANCEL)
            dismissAllowingStateLoss()
        }
        binding?.btnRetry?.setOnClickListener {
            callBack?.onResult(ACTION_RETRY)
            dismissAllowingStateLoss()
        }
    }
}