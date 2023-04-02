package com.latenightchauffeurs.dbh

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latenightchauffeurs.FragmentCallBack
import com.latenightchauffeurs.databinding.CardDetailsViewLayoutBinding
import com.latenightchauffeurs.model.ItemCardList

/**
 * Create by Sirumalayil on 01-04-2023.
 */
class CardListAdapter(private var callback: FragmentCallBack? = null): ListAdapter<
        ItemCardList,CardListAdapter.ViewHolder>(DiffCallBack()) {

    private var cardExpMonth = ""
    private var cardExpYear = ""
    private var cardExpDate = ""
    private var selectedPosition = -1

    class DiffCallBack : DiffUtil.ItemCallback<ItemCardList>() {
        override fun areItemsTheSame(oldItem: ItemCardList, newItem: ItemCardList): Boolean  {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ItemCardList, newItem: ItemCardList): Boolean {
            return oldItem == newItem
        }

    }



    inner class ViewHolder(private var binding: CardDetailsViewLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(card: ItemCardList?) {
            val number = card?.token
            val mask: String? = number?.replace("\\w(?=\\w{4})".toRegex(), "*")

            if (card?.expiry.toString().length >= 2) {
                cardExpMonth = card?.expiry.toString().substring(0, 2)
            }
            if (card?.expiry.toString().length >= 4) {
                cardExpYear = card?.expiry.toString()
                    .substring((card?.expiry.toString().length - 2).coerceAtLeast(0))
                cardExpDate = "$cardExpMonth/$cardExpYear"
            }
            binding.btnCardSelected.isChecked = absoluteAdapterPosition == selectedPosition

            binding.cardExpiry.text = cardExpDate
            binding.cardNumber.text = "${card?.accttype} $mask"
            binding.cardView.setOnClickListener {
                if (absoluteAdapterPosition == selectedPosition) {
                    binding.btnCardSelected.isChecked = false
                    selectedPosition = -1
                } else {
                    selectedPosition = absoluteAdapterPosition
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CardDetailsViewLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

}