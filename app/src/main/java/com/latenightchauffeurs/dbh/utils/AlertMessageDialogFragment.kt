package com.latenightchauffeurs.dbh.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var showNegativeBtn: Boolean = true
    private var title: String? = "Error"
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
            this.title = title
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogViewBinding.inflate(inflater, container, false)
        dialog?.apply {
            isCancelable = false
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvHeaderTitle?.text = title ?: "Error"
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