package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.databinding.FragmentTipLayoutBinding

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhRideAddTip : AppCompatActivity() {

    private var binding: FragmentTipLayoutBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentTipLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)

        binding?.radioGroup?.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
            }
        }
        binding?.toolbarAddTip?.setNavigationOnClickListener {
            finish()
        }


    }
}