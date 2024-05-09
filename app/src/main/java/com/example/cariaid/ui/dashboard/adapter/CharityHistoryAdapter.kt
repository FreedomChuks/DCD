package com.example.cariaid.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.data.model.CharityHistory
import com.example.cariaid.databinding.DonationLayoutBinding
import com.example.cariaid.databinding.LayoutHistoryBinding

class CharityHistoryAdapter(private val onClick:(CharityHistory)->Unit): ListAdapter<CharityHistory, CharityHistoryAdapter.CharityViewHolder>(CharacterDiffCallback) {

    inner class CharityViewHolder(private var binding: LayoutHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(charity: CharityHistory){
            with(binding){
                charityIcon.load(charity.charityIcon)
                charityName.text = charity.charityName
                charityDonatedAmount.text = "Donated £${charity.amountDonated}"
                binding.root.setOnClickListener {
                        onClick(charity)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharityViewHolder {
        return CharityViewHolder(LayoutHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CharityViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    object CharacterDiffCallback : DiffUtil.ItemCallback<CharityHistory>() {
        override fun areItemsTheSame(oldItem: CharityHistory, newItem: CharityHistory): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: CharityHistory, newItem: CharityHistory): Boolean {
            return oldItem.charityName == newItem.charityName
        }
    }
}