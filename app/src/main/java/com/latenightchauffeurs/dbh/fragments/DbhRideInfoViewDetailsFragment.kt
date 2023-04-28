package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.databinding.FragmentDbhRideDetailsBinding
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhRideInfoViewDetailsFragment : Fragment() {

    private var binding: FragmentDbhRideDetailsBinding? = null
    private var rideInfo: DbhRide? = null
    private var dbhViewModel: DbhViewModel? = null

    companion object {
        fun newInstance(rideInfo: DbhRide? = null) = DbhRideInfoViewDetailsFragment().apply {
            this.rideInfo = rideInfo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhRideDetailsBinding.inflate(layoutInflater, container, false)
        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickListeners()
    }

    private fun onClickListeners() {

    }
}