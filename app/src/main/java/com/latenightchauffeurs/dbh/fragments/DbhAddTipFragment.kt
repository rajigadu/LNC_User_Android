package com.latenightchauffeurs.dbh.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.latenightchauffeurs.databinding.FragmentTipLayoutBinding

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhAddTipFragment : Fragment() {

    private var binding: FragmentTipLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTipLayoutBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.radioGroup?.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
            }
        }
    }
}