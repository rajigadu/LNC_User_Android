package com.latenightchauffeurs.dbh.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.latenightchauffeurs.Utils.ConstantUtil
import com.latenightchauffeurs.databinding.FragmentDbhFeedbackBinding
import com.latenightchauffeurs.databinding.FragmentDbhPaymentSummaryBinding
import com.latenightchauffeurs.dbh.model.response.DbhPaymentDetails
import com.latenightchauffeurs.dbh.model.response.RideHistory
import com.latenightchauffeurs.dbh.utils.ProgressCaller
import com.latenightchauffeurs.dbh.utils.Resource
import com.latenightchauffeurs.dbh.viewmodel.DbhViewModel

/**
 * Create by Siru Malayil on 28-04-2023.
 */
class DbhPaymentSummary: AppCompatActivity() {

    private var binding: FragmentDbhPaymentSummaryBinding? = null
    private var dbhViewModel: DbhViewModel? = null
    private var rideHistory: RideHistory? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentDbhPaymentSummaryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        super.onCreate(savedInstanceState)

        dbhViewModel = ViewModelProvider(this)[DbhViewModel::class.java]
        rideHistory = intent?.extras?.getParcelable(ConstantUtil.RIDE_HISTORY) as? RideHistory

        paymentSummaryDetails()

        binding?.toolbarPaymentSummary?.setNavigationOnClickListener {
            finish()
        }

        binding?.btnOk?.setOnClickListener {
            finish()
        }

    }

    private fun paymentSummaryDetails() {
        dbhViewModel?.dbhPaymentDetails(
            userId = rideHistory?.user_id,
            rideId = rideHistory?.id
        )?.observe(this) { result ->
            when(result.status) {
                Resource.Status.LOADING -> { ProgressCaller.showProgressDialog(this)}
                Resource.Status.SUCCESS -> {
                    if (result.data?.status == "1") {
                        setPaymentData(result?.data)
                    }
                    ProgressCaller.hideProgressDialog()
                }
                Resource.Status.ERROR -> {
                    ProgressCaller.hideProgressDialog()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPaymentData(data: DbhPaymentDetails) {
        binding?.transactionId?.text = data.data.transaction_id
        binding?.basePrice?.text = "$ ${data.base_price}.00"
        binding?.promoCode?.text = "$ ${data.data.promo_amt}.00"
        binding?.waitTime?.text = "$0.00"
        binding?.unplannedStops?.text = "$0.00"
        binding?.plannedStops?.text = "$0.00"
        binding?.totalFare?.text = "$ ${data.total_fare}.00"
    }
}