package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.databinding.FragmentDbhPaymentSummaryBinding
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhPaymentSummary: AppCompatActivity() {

    private var binding: FragmentDbhPaymentSummaryBinding? = null
    private var dbhViewModel: DbhViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentDbhPaymentSummaryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)

        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]

        binding?.toolbarPaymentSummary?.setNavigationOnClickListener {
            finish()
        }

    }
}