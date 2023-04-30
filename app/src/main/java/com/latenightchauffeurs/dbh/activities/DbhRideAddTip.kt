package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.latenightchauffeurs.Utils.Utils
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.databinding.FragmentTipLayoutBinding
import org.json.JSONObject

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhRideAddTip : AppCompatActivity() {

    private var binding: FragmentTipLayoutBinding? = null
    private var tipPercentage: String? = null


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

        binding?.btnAddTip?.setOnClickListener {
            if (validated()) submitTipDetails()
        }

        binding?.btnClear?.setOnClickListener {
            binding?.radioGroup?.clearCheck()
        }
    }

    private fun validated(): Boolean {
        val tipAmount = binding?.edtTipAmount?.text?.toString()?.trim()
        if (tipPercentage == null && tipAmount.isNullOrEmpty()) {
            Utils.toastTxt("Please enter your tip as a percentage or amount", this)
            return false
        }
        return true
    }

    private fun submitTipDetails() {
        /*val json = JSONObject()
        json.put("userid", rideInfo?.user_id)
        json.put("driverid", rideInfo?.driver_id_for_future_ride)
        json.put("rideid", rideInfo?.id)
        json.put("msg", binding?.edtTextNotes?.text?.toString()?.trim())
        json.put("rating", startRating)*/
    }
}