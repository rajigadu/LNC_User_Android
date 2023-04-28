package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.dbh.model.response.DbhRide

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhRideFeedbackFragment : Fragment() {

    private var binding: FragmentDbhFeedbackBinding? = null
    private var rideInfo: DbhRide? = null

    companion object {
        fun newInstance(rideInfo: DbhRide? = null) = DbhRideFeedbackFragment().apply {
            this.rideInfo = rideInfo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhFeedbackBinding.inflate(layoutInflater,container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}