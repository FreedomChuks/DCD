package com.example.cariaid.ui.dashboard.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.databinding.DonationLayoutBinding
import com.example.cariaid.databinding.LayoutDashboardItemBinding
import com.example.cariaid.utils.removeCurrency
import kotlin.math.roundToInt

class CharityDashboardAdapter(private val onClick:(CharityData)->Unit): ListAdapter<CharityData, CharityDashboardAdapter.CharityViewHolder>(CharacterDiffCallback) {

    inner class CharityViewHolder(private var binding: LayoutDashboardItemBinding) : RecyclerView.ViewHolder(binding.root) {


        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(charity: CharityData){
            with(binding){
                charityImage.load(charity.imageUrl)
                charityHeading.text = charity.donationHeading
                amountRaised.text = "£${charity.donationRaised}"
                if (charity.donationRaised.removeCurrency.toInt()==0){
                    progress.setProgress(0,true)
                    percentageText.text ="0%"
                }else{
                    val percentage = charity.donationRaised.removeCurrency.toDouble().div(charity.donationAmount.removeCurrency.toDouble()).times(100)
                    percentageText.text = "${percentage.roundToInt()}%"
                    progress.setProgress(percentage.roundToInt(),true)
                }
                binding.root.setOnClickListener {
                        onClick(charity)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharityViewHolder {
        return CharityViewHolder(LayoutDashboardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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