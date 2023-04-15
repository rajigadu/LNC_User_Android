package com.latenightchauffeurs.dbh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.FragmentRidesViewLayoutBinding
import com.latenightchauffeurs.dbh.adapter.UpcomingDbhRidesAdapter
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhUpcomingRidesData
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


    private var upcomingDbhRodesAdapter = UpcomingDbhRidesAdapter(callback = object : FragmentCallBack {
        override fun onResult(param1: Any?, param2: Any?, param3: Any?) {

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
        preferences = SavePref()
        preferences?.SavePref(activity)
        getUpcomingDbhRides()
    }

    private fun getUpcomingDbhRides() {
        bookingViewModel?.upcomingDbhRides(preferences?.userId)?.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { activity?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {
                    if (result.data?.status == "1") {
                        val upcomingRides = result?.data.data
                        initializeAdapter(upcomingRides)
                    }
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    (activity as? BaseActivity)?.showAlertMessageDialog(
                        message = result.message
                    )
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

    private fun initializeAdapter(upcomingRides: List<DbhUpcomingRidesData>) {
        binding?.recyclerviewRides?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = upcomingDbhRodesAdapter
        }
        upcomingDbhRodesAdapter.submitList(upcomingRides)
    }
}