package com.latenightchauffeurs.dbh.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.DbhRideHistoryViewBinding
import com.latenightchauffeurs.dbh.model.response.RideHistory

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class HistoryDbhRidesAdapter(val callback: FragmentCallBack? = null) :
    ListAdapter<RideHistory, HistoryDbhRidesAdapter.ViewHolder>(DiffCallBack()) {


    class DiffCallBack : DiffUtil.ItemCallback<RideHistory>() {
        override fun areItemsTheSame(
            oldItem: RideHistory,
            newItem: RideHistory
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: RideHistory,
            newItem: RideHistory
        ): Boolean = oldItem == newItem

    }

    inner class ViewHolder(private val binding: DbhRideHistoryViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindView(rideHistory: RideHistory?) {
            binding.bookingDate.text = "${rideHistory?.date} ${rideHistory?.time}"
            binding.paymentDate.text = rideHistory?.payment_date
            binding.pickupLocation.text = rideHistory?.pickup_address
            binding.tipAmount.text = "$${rideHistory?.tip_ammount ?: "0.00"}"
            binding.rideCost.text = "$${rideHistory?.ride_amt ?: "0.00"}"
            binding.promoCode.text = "$${rideHistory?.promo_amt ?: "0.00"}"
            binding.transactionId.text = rideHistory?.transaction_id
            binding.distance.text = "Miles"

            binding.btnAddTip.setOnClickListener {
                callback?.onResult("add-tip", rideHistory)
            }
            binding.btnFeedBack.setOnClickListener {
                callback?.onResult("feedback", rideHistory)
            }
            binding.btnPaymentSummary.setOnClickListener {
                callback?.onResult("payment-summary", rideHistory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            DbhRideHistoryViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}