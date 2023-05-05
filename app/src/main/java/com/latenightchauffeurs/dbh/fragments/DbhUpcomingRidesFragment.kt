package com.latenightchauffeurs.dbh.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.R
import com.latenightchauffeurs.Utils.Utils
import com.latenightchauffeurs.databinding.FragmentRidesViewLayoutBinding
import com.latenightchauffeurs.dbh.activities.DriveByHourActivity
import com.latenightchauffeurs.dbh.adapter.UpcomingDbhRidesAdapter
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.model.response.DbhUpcomingRidesData
import com.latenightchauffeurs.dbh.utils.DbhUtils
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.model.SavePref

/**
 * Create by Siru Malayil on 15-04-2023.
 */
class DbhUpcomingRidesFragment : Fragment() {

    private var binding: FragmentRidesViewLayoutBinding? = null
    private var bookingViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null


    private var upcomingDbhRidesAdapter =
        UpcomingDbhRidesAdapter(callback = object : FragmentCallBack {
            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                val rideInfo = param2 as? DbhRide
                when (param1 as? String) {
                    DbhUtils.EDIT_RIDE -> {
                        startActivity(
                            Intent(activity, DriveByHourActivity::class.java).apply {
                                putExtra(DbhUtils.RIDE_INFO, rideInfo)
                                putExtra(DbhUtils.EDIT_RIDE_INFO, DbhUtils.VALUE_EDIT)
                            }
                        )
                    }
                    DbhUtils.VIEW_DETAILS -> {
                        startActivity(
                            Intent(activity, DriveByHourActivity::class.java).apply {
                                putExtra(DbhUtils.RIDE_INFO, rideInfo)
                                putExtra(DbhUtils.EDIT_RIDE_INFO, DbhUtils.VALUE_VIEW)
                            }
                        )
                    }
                }
            }
        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRidesViewLayoutBinding.inflate(layoutInflater, container, false)
        bookingViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.refreshRides?.setOnRefreshListener {
            if (Utils.isNetworkAvailable(activity)) {
                binding?.refreshRides?.isRefreshing = true
                getUpcomingDbhRides()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferences = SavePref()
        preferences?.SavePref(activity)
        if (Utils.isNetworkAvailable(activity)) {
            initializeAdapter()
            getUpcomingDbhRides()
        }
    }

    private fun getUpcomingDbhRides() {
        bookingViewModel?.upcomingDbhRides(preferences?.userId)
            ?.observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Resource.Status.LOADING -> {
                        activity?.let { ProgressCaller.showProgressDialog(it) }
                    }
                    Resource.Status.SUCCESS -> {
                        val upcomingRides = result?.data?.data
                        if (result.data?.status == "1") {
                            submitRidesList(upcomingRides)
                        } else (activity as? BaseActivity)?.showAlertMessageDialog(
                                message = result.data?.message ?: getString(R.string.something_went_wrong)
                            )
                        ProgressCaller.hideProgressDialog()
                        binding?.refreshRides?.isRefreshing = false
                    }
                    Resource.Status.ERROR -> {
                        (activity as? BaseActivity)?.showAlertMessageDialog(
                            message = result.message
                        )
                        ProgressCaller.hideProgressDialog()
                        binding?.refreshRides?.isRefreshing = false
                    }
                }
            }
    }

    private fun submitRidesList(upcomingRides: DbhUpcomingRidesData?) {
        upcomingRides?.ride?.forEach { rides ->
            upcomingRides.future_edit_ride_status.forEach { futureEditRideStatus ->
                if (rides.id == futureEditRideStatus.id) {
                    rides.future_edit_ride_status = futureEditRideStatus.future_edit_ride_status
                    rides.future_edit_ride_id = futureEditRideStatus.id
                }
            }
        }
        upcomingDbhRidesAdapter.submitList(upcomingRides?.ride)
    }

    private fun initializeAdapter(upcomingRides: DbhUpcomingRidesData?= null) {
        binding?.recyclerviewRides?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = upcomingDbhRidesAdapter
        }
        upcomingDbhRidesAdapter.submitList(upcomingRides?.ride)
    }
}