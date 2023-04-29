package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.dbh.model.response.DbhRide

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhRideFeedback : AppCompatActivity() {

    private var binding: FragmentDbhFeedbackBinding? = null
    private var rideInfo: DbhRide? = null

    companion object {
        fun newInstance(rideInfo: DbhRide? = null) = DbhRideFeedback().apply {
            this.rideInfo = rideInfo
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentDbhFeedbackBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)

        binding?.toolbarFeedback?.setNavigationOnClickListener {
            finish()
        }

        binding?.btnSubmit?.setOnClickListener {
            if (validated()) submitFeedback()
        }

    }

    private fun validated(): Boolean {

        return true
    }

    private fun submitFeedback() {

    }

}