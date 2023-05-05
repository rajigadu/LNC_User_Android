package com.latenightchauffeurs.dbh.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.activity.ActivityChat
import com.latenightchauffeurs.databinding.FragmentDbhRideDetailsBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhDriverData
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.utils.AlertDialogMessageFragment.Companion.ACTION_OK
import com.latenightchauffeurs.dbh.utils.ConstantUtils
import com.latenightchauffeurs.dbh.utils.ConstantUtils.capitalizeWords
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.extension.navigate
import com.latenightchauffeurs.extension.replaceFragment
import com.latenightchauffeurs.model.SavePref

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhRideInfoViewDetailsFragment : Fragment() {

    private var binding: FragmentDbhRideDetailsBinding? = null
    private var rideInfo: DbhRide? = null
    private var dbhViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null
    private var callBack: FragmentCallBack? = null
    private var dbhDriverData: DbhDriverData? = null

    companion object {
        fun newInstance(rideInfo: DbhRide? = null,callBack: FragmentCallBack? = null) = DbhRideInfoViewDetailsFragment().apply {
            this.rideInfo = rideInfo
            this.callBack = callBack
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

    @SuppressLint("SetTextI18n")
    private fun setDViewDetails() {
        getDriverDetails()
        binding?.rideStatus?.text = ConstantUtils.getRideStatus(rideInfo)
            .replace("_", " ")
            .capitalizeWords()
        binding?.date?.text = "${rideInfo?.otherdate} ${rideInfo?.time}"
    }

    private fun getDriverDetails() {
        dbhViewModel?.dbhDriverDetails(rideInfo?.driver_id_for_future_ride ?: "")
            ?.observe(viewLifecycleOwner) { result ->
                when(result.status) {
                    Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                    Resource.Status.SUCCESS -> {
                        if (result.data?.status == "1") {
                            dbhDriverData = result.data.data
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

        binding?.call?.setOnClickListener {
            (activity as? BaseActivity)?.showAlertMessageDialog(
                message = "Are you sure want to call ?",
                callBack = object : FragmentCallBack {
                    override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                        when(param1) {
                            ACTION_OK -> {
                                callPermission(dbhDriverData?.mobile)
                            }
                        }
                    }
                }
            )
        }

        binding?.msg?.setOnClickListener {
            val dmap = HashMap<String, Any>()
            dmap["driver_id"] = rideInfo?.driver_id_for_future_ride ?: ""
            dmap["id"] = rideInfo?.id ?: ""
            startActivity(
                Intent(activity,ActivityChat::class.java).apply {
                    putExtra("map",dmap)
                }
            )
        }
    }
    /**
     * Phone Call permission will request here, once request accepted
     * will invoke dialer dashboard
     */
    private fun callPermission(number: String?) {
        activity?.let { activity ->
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getCall(number)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CALL_PHONE),
                        100
                    )
                }
            }
        }
    }

    private fun getCall(number: String?) {
        if (number?.trim { it <= ' ' }?.isNotEmpty() == true) startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", number, null)
            )
        )
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
                        "$${result.data.data.firstOrNull()?.amount.toString()} will be charged for Ride Cancellation. \n Are you sure want you to cancel this ride ?"
                    } else {
                        result?.data?.data?.firstOrNull()?.msg ?: "You can cancel ride without any charges"
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
        (activity as? AppCompatActivity)?.navigate(
            fragment = CancelRideFragment.newInstance(
                rideId = rideInfo?.id,
                userId = rideInfo?.user_id
            )
        )
    }
}