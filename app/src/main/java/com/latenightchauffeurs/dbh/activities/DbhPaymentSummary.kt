package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.databinding.FragmentDbhPaymentSummaryBinding

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhPaymentSummary: AppCompatActivity() {

    private var binding: FragmentDbhPaymentSummaryBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentDbhPaymentSummaryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)


        binding?.toolbarPaymentSummary?.setNavigationOnClickListener {
            finish()
        }

    }
}