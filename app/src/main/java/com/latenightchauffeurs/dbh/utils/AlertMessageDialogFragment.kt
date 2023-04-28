package com.latenightchauffeurs.dbh.utils

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
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
            message: String? = null,
            callBack: FragmentCallBack? = null
        ) = AlertDialogMessageFragment().apply {
            this.callBack = callBack
            this.showNegativeBtn = showNegativeBtn
            this.showRetry = showRetry
            this.message = message
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setStyle(STYLE_NO_FRAME, android.R.style.Theme)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        isCancelable = false
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

    override fun onResume() {
        super.onResume()
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}