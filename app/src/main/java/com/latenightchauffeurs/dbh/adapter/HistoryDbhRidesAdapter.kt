package com.latenightchauffeurs.dbh.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.DbhRideHistoryViewBinding
import com.latenightchauffeurs.dbh.model.response.DbhRideHistoryData
import com.latenightchauffeurs.dbh.utils.DbhUtils

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class HistoryDbhRidesAdapter(val callback: FragmentCallBack? = null) :
    ListAdapter<DbhRideHistoryData, HistoryDbhRidesAdapter.ViewHolder>(DiffCallBack()) {


    class DiffCallBack : DiffUtil.ItemCallback<DbhRideHistoryData>() {
        override fun areItemsTheSame(
            oldItem: DbhRideHistoryData,
            newItem: DbhRideHistoryData
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DbhRideHistoryData,
            newItem: DbhRideHistoryData
        ): Boolean = oldItem == newItem

    }

    inner class ViewHolder(private val binding: DbhRideHistoryViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindView(rideHistory: DbhRideHistoryData?) {
            binding.bookingDate.text = "${rideHistory?.date} ${rideHistory?.time}"
            binding.paymentDate.text = rideHistory?.payment_date
            binding.pickupLocation.text = rideHistory?.pickup_address
            binding.tipAmount.text = "$${rideHistory?.tip_ammount ?: "0.00"}"
            binding.rideCost.text = "$${rideHistory?.ride_amt ?: "0.00"}"
            binding.promoCode.text = "$${rideHistory?.promo_amt ?: "0.00"}"
            binding.transactionId.text = rideHistory?.transaction_id
            binding.distance.text = rideHistory?.distance

            binding.btnAddTip.setOnClickListener {
                callback?.onResult(DbhUtils.ADD_TIP, rideHistory)
            }
            binding.btnFeedBack.setOnClickListener {
                callback?.onResult(DbhUtils.FEEDBACK, rideHistory)
            }
            binding.btnPaymentSummary.setOnClickListener {
                callback?.onResult(DbhUtils.PAYMENT_SUMMARY, rideHistory)
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