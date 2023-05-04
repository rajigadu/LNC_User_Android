package com.latenightchauffeurs.dbh.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.R
import com.latenightchauffeurs.databinding.LayoutUpcomingDbhRidesBinding
import com.latenightchauffeurs.dbh.model.response.DbhRide
import com.latenightchauffeurs.dbh.utils.ConstantUtils
import com.latenightchauffeurs.dbh.utils.ConstantUtils.capitalizeWords

/**
 * Create by Siru Malayil on 15-04-2023.
 */
class UpcomingDbhRidesAdapter(val callback: FragmentCallBack? = null) :
    ListAdapter<DbhRide, UpcomingDbhRidesAdapter.ViewHolder>(DiffCallBack()) {


    class DiffCallBack : DiffUtil.ItemCallback<DbhRide>() {
        override fun areItemsTheSame(
            oldItem: DbhRide,
            newItem: DbhRide
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DbhRide,
            newItem: DbhRide
        ): Boolean = oldItem == newItem

    }

    inner class ViewHolder(private val binding: LayoutUpcomingDbhRidesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindView(ride: DbhRide) {
            binding.valueRideDate.text = "${ride.otherdate}" +
                    " ${ride.time}"
            binding.valuePickupLocation.text = ride.pickup_address
            binding.valueRate.text = "$${ride.hourly_rate} Per Hour"
            binding.valueRideStatus.text = ConstantUtils.getRideStatus(ride)
                .replace("_", " ")
                .capitalizeWords()
            binding.valueRideStatus.setTextColor(
                ConstantUtils.rideStatusTextColor(ride, binding.root.context))

            binding.btnEditRideInfo.setOnClickListener {
                callback?.onResult("edit_ride", ride)
            }
            binding.btnViewDetails.setOnClickListener {
                callback?.onResult("view_details", ride)
            }
            binding.btnEditRideInfo.isVisible =
                ride.future_edit_ride_status == "1" && ride.status == "0"
            binding.btnViewDetails.isVisible =
                ride.future_edit_ride_status == "1" && ride.status == "1" ||
                        ride.future_edit_ride_status == "0" && ride.status == "0"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutUpcomingDbhRidesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}