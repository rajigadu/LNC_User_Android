package com.latenightchauffeurs.dbh.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.CrideRowitemBinding
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

    inner class ViewHolder(private val binding: CrideRowitemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(item: DbhUpcomingRidesData?) {
            when(item?.booking_type) {
                "1" -> {
                    binding.btnEditRide.isVisible = false
                    binding.ridestatus.isVisible = false
                    binding.ridestatusTitle.isVisible = false
                    binding.viewdetails.isVisible = false
                }
                else -> {
                    binding.btnEditRide.isVisible = true
                    binding.ridestatus.isVisible = true
                    binding.ridestatusTitle.isVisible = true
                    binding.viewdetails.isVisible = true
                }
            }

            binding.date.text = "Date: ${item?.otherdate}\nTime: ${item?.time}\n"
            binding.distance.text = String.format(
                "%.2f",
                item?.distance?.let { java.lang.Double.valueOf(it) }) + " mi"
            binding.
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CrideRowitemBinding.inflate(
                LayoutInflater.from(parent.context),parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}