package com.latenightchauffeurs.dbh.activities

import android.os.Bundle
import android.text.TextUtils
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.R
import com.latenightchauffeurs.Utils.Utils
import com.latenightchauffeurs.databinding.FragmentTipLayoutBinding
import com.latenightchauffeurs.dbh.base.BaseActivity
import com.latenightchauffeurs.dbh.model.response.DbhRideHistoryData
import com.latenightchauffeurs.dbh.utils.DbhUtils
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel
import org.json.JSONObject

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhRideAddTip : BaseActivity() {

    private var binding: FragmentTipLayoutBinding? = null
    private var tipPercentage: String? = null
    private var rideHistory: DbhRideHistoryData? = null
    private var dbhViewModel: DbhViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentTipLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)

        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        rideHistory = intent?.extras?.getParcelable(DbhUtils.RIDE_HISTORY) as? DbhRideHistoryData

        onClickListeners()

    }

    private fun onClickListeners() {
        binding?.radioGroup?.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.tip_15 -> {
                    tipPercentage = "15"
                }
                R.id.tip_20 -> {
                    tipPercentage = "20"
                }
                R.id.tip_25 -> {
                    tipPercentage = "25"
                }
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
            tipPercentage = ""
        }

        binding?.edtTipAmount?.doOnTextChanged { text, start, before, count ->
            if (TextUtils.isEmpty(text)) {
                binding?.radioGroup?.clearCheck()
                tipPercentage = ""
            }
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
        val json = JSONObject()
        json.put("userid", rideHistory?.user_id)
        json.put("driverid", rideHistory?.driver_id_for_future_ride)
        json.put("rideid", rideHistory?.id)
        json.put("tip", binding?.edtTipAmount?.text?.toString()?.trim())
        json.put("percentage", tipPercentage)
        json.put("rating", "")
        json.put("msg", "")

        dbhViewModel?.dbhRideFeedback(json)?.observe(this) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { ProgressCaller.showProgressDialog(this)}
                Resource.Status.SUCCESS -> {
                    showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg,
                        callBack = object : FragmentCallBack {
                            override fun onResult(param1: Any?, param2: Any?, param3: Any?) {
                                finish()
                            }
                        }
                    )
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    showAlertMessageDialog(
                        message = result.data?.data?.firstOrNull()?.msg)
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }
}