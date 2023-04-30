package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.ActivityCancelrideBinding
import com.latenightchauffeurs.databinding.FragmentCancelRideBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.utils.ConstantUtils
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel

/**
 * Create by Siru Malayil on 30-04-2023.
 */
class CancelRideFragment : Fragment() {

    private var binding: FragmentCancelRideBinding? = null
    private var dbhViewModel: DbhViewModel? = null
    private var userId: String? = null
    private var rideId: String? = null

    companion object {
        fun newInstance(rideId: String? = null, userId: String? = null) = CancelRideFragment().apply {
            this.rideId = rideId
            this.userId = userId
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCancelRideBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        binding?.btnSubmit?.setOnClickListener {
            if (validated()) cancelRide()
        }
        binding?.btnCancel?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding?.toolbarCancelRide?.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun validated(): Boolean {
        val rideCancelReason = binding?.reason?.text?.toString()?.trim()
        if (TextUtils.isEmpty(rideCancelReason)) {
            binding?.reason?.error = "Please enter a reason for cancellation"
            return false
        }
        return true
    }

    private fun cancelRide() {
        dbhViewModel?.cancelDbhRide(
            userId = userId,
            rideId = rideId,
            cancelTime = ConstantUtils.getCurrentDateAndTime() ?: ""
        )?.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    (activity as? BaseActivity)?.showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg,
                        callBack = object : FragmentCallBack {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                activity?.finish()
                            }
                        }
                    )
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }
}