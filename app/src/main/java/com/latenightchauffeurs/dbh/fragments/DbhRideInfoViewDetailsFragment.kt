package com.latenightchauffeurs.dbh.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.FragmentDbhRideDetailsBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhDriverData
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.utils.AlertDialogMessageFragment.Companion.ACTION_OK
import com.latenightchauffeurs.dbh.utils.ConstantUtils
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.model.SavePref

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhRideInfoViewDetailsFragment : Fragment() {

    private var binding: FragmentDbhRideDetailsBinding? = null
    private var rideInfo: DbhRide? = null
    private var dbhViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null

    companion object {
        fun newInstance(rideInfo: DbhRide? = null) = DbhRideInfoViewDetailsFragment().apply {
            this.rideInfo = rideInfo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhRideDetailsBinding.inflate(layoutInflater, container, false)
        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SavePref()
        preferences?.SavePref(activity)

        setDViewDetails()
        onClickListeners()
    }

    private fun setDViewDetails() {
        getDriverDetails()
        val status = if (rideInfo?.future_accept == "0") "Pending" else "Accepted"
        binding?.driverName?.text = ""
        binding?.driverNumber?.text = ""
        binding?.rideStatus?.text = status
        binding?.date?.text = rideInfo?.ride_start_time

        binding?.layoutDriverDetails?.isVisible = rideInfo?.status == "1"
    }

    private fun getDriverDetails() {
        dbhViewModel?.dbhDriverDetails(rideInfo?.driver_id_for_future_ride ?: "")
            ?.observe(viewLifecycleOwner) { result ->
                when(result.status) {
                    Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                    Resource.Status.SUCCESS -> {
                        if (result.data?.status == "1") {
                            updateDriverDetails(result.data.data)
                        }
                        ProgressCaller.hideProgressDialog()
                    }
                    Resource.Status.ERROR -> {
                        ProgressCaller.hideProgressDialog()
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateDriverDetails(driver: DbhDriverData?) {
        binding?.driverName?.text = "${driver?.first_name} ${driver?.last_name}"
        binding?.driverNumber?.text = driver?.mobile
    }

    private fun onClickListeners() {
        binding?.btnCancelRide?.setOnClickListener {
            (activity as? BaseActivity)?.showAlertMessageDialog(message = "Are you sure want to cancel this ride ?," +
                    "please note if you are cancelling within four hours you are subject to being billed for your ride. To keep the ride please press cancel.",
                negativeButton = true,
                callBack = object : FragmentCallBack {
                    override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                        when (param1) {
                            ACTION_OK -> {
                                cancelDbhRideAmount()
                            }
                        }
                    }
                })
        }
    }

    private fun cancelDbhRideAmount() {
        dbhViewModel?.cancelDbhRideAmount(
            rideId = rideInfo?.id ?: "",
            cancelTime = ConstantUtils.getCurrentDateAndTime() ?: ""
        )?.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {

                    val message = if (result.data?.status == "1") {
                        "will be charged for Ride Cancellation. \n Are you sure want you to cancel this ride ?"
                    } else {
                        result?.data?.data?.firstOrNull()?.msg ?: "You can cancel this ride without any charges"
                    }
                    (activity as? BaseActivity)?.showAlertMessageDialog(
                        message = message,
                        negativeButton = true,
                        callBack = object : FragmentCallBack {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                when (param1) {
                                    ACTION_OK -> {
                                        cancelDbhRide()
                                    }
                                }
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

    private fun cancelDbhRide() {
        dbhViewModel?.cancelDbhRide(
            userId = preferences?.userId ?: "",
            rideId = rideInfo?.id ?: "",
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