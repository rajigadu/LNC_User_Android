package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.latenightchauffeurs.databinding.FragmentDbhPaymentSummaryBinding

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhPaymentSummaryFragment: Fragment() {

    private var binding: FragmentDbhPaymentSummaryBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDbhPaymentSummaryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}