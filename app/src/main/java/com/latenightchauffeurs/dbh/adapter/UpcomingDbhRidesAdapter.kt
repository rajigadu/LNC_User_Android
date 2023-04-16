package com.latenightchauffeurs.dbh.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.R
import com.latenightchauffeurs.databinding.CrideRowitemBinding
import com.latenightchauffeurs.databinding.LayoutUpcomingDbhRidesBinding
import com.latenightchauffeurs.dbh.model.response.DbhUpcomingRidesData

/**
 * Create by Siru Malayil on 15-04-2023.
 */
class UpcomingDbhRidesAdapter(callback: FragmentCallBack? = null) : ListAdapter<DbhUpcomingRidesData, UpcomingDbhRidesAdapter.ViewHolder>(DiffCallBack()) {


    class DiffCallBack : DiffUtil.ItemCallback<DbhUpcomingRidesData>() {
        override fun areItemsTheSame(
            oldItem: DbhUpcomingRidesData,
            newItem: DbhUpcomingRidesData
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DbhUpcomingRidesData,
            newItem: DbhUpcomingRidesData
        ): Boolean = oldItem == newItem

    }

    inner class ViewHolder(private val binding: LayoutUpcomingDbhRidesBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindView(item: DbhUpcomingRidesData?) {
            binding.valueRideDate.text = "${item?.otherdate} ${item?.time}"
            binding.valuePickupLocation.text = item?.pickup_address
            binding.valueRate.text = "$ 10.00 Per Hour"
            if (item?.status == "0") {
                binding.valueRideStatus.text = "Pending"
                binding.valueRideStatus.setTextColor(ContextCompat.getColor(
                    binding.root.context!!,
                    R.color.red
                ))
            } else {
                binding.valueRideStatus.text = "Accepted"
                binding.valueRideStatus.setTextColor(ContextCompat.getColor(
                    binding.root.context!!,
                    R.color.green_color
                ))
            }

            binding.btnViewDetails.isVisible = item?.booking_type != "1"
            binding.btnEditRideInfo.isVisible = item?.booking_type == "1"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutUpcomingDbhRidesBinding.inflate(
                LayoutInflater.from(parent.context),parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}