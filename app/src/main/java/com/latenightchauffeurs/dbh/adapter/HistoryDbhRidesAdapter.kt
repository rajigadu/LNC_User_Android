package com.latenightchauffeurs.dbh.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.DbhRideHistoryViewBinding
import com.latenightchauffeurs.dbh.model.response.DbhUpcomingRidesData

/**
 * Create by Siru Malayil on 20-04-2023.
 */
class HistoryDbhRidesAdapter(val callback: FragmentCallBack? = null) :
    ListAdapter<DbhUpcomingRidesData, HistoryDbhRidesAdapter.ViewHolder>(DiffCallBack()) {


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

    inner class ViewHolder(private val binding: DbhRideHistoryViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindView(item: DbhUpcomingRidesData?) {

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