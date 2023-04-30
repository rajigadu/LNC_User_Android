package com.latenightchauffeurs.dbh.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.R
import com.latenightchauffeurs.Utils.ConstantUtil.RIDE_HISTORY
import com.latenightchauffeurs.Utils.ConstantUtil.RIDE_INFO
import com.latenightchauffeurs.databinding.FragmentRidesViewLayoutBinding
import com.latenightchauffeurs.dbh.activities.DbhPaymentSummary
import com.latenightchauffeurs.dbh.activities.DbhRideAddTip
import com.latenightchauffeurs.dbh.activities.DbhRideFeedback
import com.latenightchauffeurs.dbh.adapter.HistoryDbhRidesAdapter
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.RideHistory
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import com.latenightchauffeurs.model.SavePref

/**
 * Create by Siru Malayil on 15-04-2023.
 */
class DbhRideHistoryFragment : Fragment() {

    private var binding: FragmentRidesViewLayoutBinding? = null
    private var bookingViewModel: DbhViewModel? = null
    private var preferences: SavePref? = null



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
        preferences = SavePref()
        preferences?.SavePref(activity)

        binding?.refreshRides?.setOnRefreshListener {
            binding?.refreshRides?.isRefreshing = true
            getDbhRidesHistory()
        }
        getDbhRidesHistory()
    }

    private fun getDbhRidesHistory() {
        bookingViewModel?.dbhRidesHistory(preferences?.userId)?.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { activity ?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    val rideHistory = result.data?.data?.ride
                    if (result.data?.status == "1") {
                        if (result.data.data.ride.isNotEmpty()) {
                            initializeRideHistoryAdapter(rideHistory)
                        } else {
                            (activity as? BaseActivity)?.showAlertMessageDialog(
                                message = "There is no more ride history!"
                            )
                        }
                    } else (activity as? BaseActivity)?.showAlertMessageDialog(
                        message = result.data?.message ?: getString(R.string.something_went_wrong)
                    )
                    ProgressCaller.hideProgressDialog()
                    binding?.refreshRides?.isRefreshing = false
                }
                Resource.Status.ERROR -> {
                    (activity as? BaseActivity)?.showAlertMessageDialog(
                        message = result.data?.message ?: getString(R.string.something_went_wrong)
                    )
                    ProgressCaller.hideProgressDialog()
                    binding?.refreshRides?.isRefreshing = false
                }
            }
        }
    }

    private fun initializeRideHistoryAdapter(rideHistoryList: List<RideHistory>?) {
        val rideHistoryAdapter = HistoryDbhRidesAdapter(
            callback = object : FragmentCallBack {
                override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                    val rideHistory = param2 as RideHistory
                    when(param1) {
                        "add-tip" -> {
                            startActivity(
                                Intent(activity, DbhRideAddTip::class.java).apply {  }
                            )
                        }
                        "feedback" -> {
                            startActivity(
                                Intent(activity, DbhRideFeedback::class.java).apply {
                                    putExtra(RIDE_HISTORY, rideHistory)
                                }
                            )
                        }
                        "payment-summary" -> {
                            startActivity(
                                Intent(activity, DbhPaymentSummary::class.java).apply {  }
                            )
                        }
                    }
                }
            }
        )
        binding?.recyclerviewRides?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = rideHistoryAdapter
        }
        rideHistoryAdapter.submitList(rideHistoryList)
    }
}