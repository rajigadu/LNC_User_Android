package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.databinding.FragmentRidesViewLayoutBinding
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
        //getDbhRidesHistory()
    }

    private fun getDbhRidesHistory() {

        bookingViewModel?.dbhRidesHistory(preferences?.userId)?.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { activity ?.let { ProgressCaller.showProgressDialog(it) }}
                Resource.Status.SUCCESS -> {

                }
                Resource.Status.ERROR -> {

                }
            }
        }
    }
}