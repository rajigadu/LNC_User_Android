package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import org.json.JSONObject

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class DbhRideFeedback : AppCompatActivity() {

    private var binding: FragmentDbhFeedbackBinding? = null
    private var rideInfo: DbhRide? = null
    private var startRating: String? = null
    private var dbhViewModel: DbhViewModel? = null

    companion object {
        fun newInstance(rideInfo: DbhRide? = null) = DbhRideFeedback().apply {
            this.rideInfo = rideInfo
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentDbhFeedbackBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)

        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]

        binding?.toolbarFeedback?.setNavigationOnClickListener {
            finish()
        }

        binding?.btnSubmit?.setOnClickListener {
            if (validated()) submitFeedback()
        }

        binding?.edtTextNotes?.doOnTextChanged { text, start, before, count ->
            if (TextUtils.isEmpty(text)) {
                binding?.edtTextNotes?.error = "should not be empty"
            } else binding?.edtTextNotes?.error = null
        }

    }

    private fun validated(): Boolean {
        val textNotes = binding?.edtTextNotes?.text?.toString()?.trim()
        startRating = binding?.rating?.rating.toString()

        if (TextUtils.isEmpty(textNotes)) {
            binding?.edtTextNotes?.error = "should not be empty"
            return false
        }
        if (startRating == null) {
            Toast.makeText(this, "Please rate your ride", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun submitFeedback() {
        val json = JSONObject()
        json.put("userid", rideInfo?.user_id)
        json.put("driverid", rideInfo?.driver_id_for_future_ride)
        json.put("rideid", rideInfo?.id)
        json.put("msg", binding?.edtTextNotes?.text?.toString()?.trim())
        json.put("rating", startRating)

        dbhViewModel?.dbhRideFeedback(json)?.observe(this) { result ->

        }
    }

}