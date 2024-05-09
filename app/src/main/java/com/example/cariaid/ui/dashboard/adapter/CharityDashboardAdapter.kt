package com.example.cariaid.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.databinding.DonationLayoutBinding

class CharityAdapter(private val onClick:(CharityData)->Unit): ListAdapter<CharityData, CharityAdapter.CharityViewHolder>(CharacterDiffCallback) {

    inner class CharityViewHolder(private var binding: DonationLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(charity: CharityData){
            with(binding){
                charityIcon.load(charity.charityIcon)
                charityName.text = charity.charityName
                charityType.text = charity.charityType
                binding.root.setOnClickListener {
                        onClick(charity)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharityViewHolder {
        return CharityViewHolder(DonationLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CharityViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    object CharacterDiffCallback : DiffUtil.ItemCallback<CharityData>() {
        override fun areItemsTheSame(oldItem: CharityData, newItem: CharityData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: CharityData, newItem: CharityData): Boolean {
            return oldItem.charityName == newItem.charityName
        }
    }
}