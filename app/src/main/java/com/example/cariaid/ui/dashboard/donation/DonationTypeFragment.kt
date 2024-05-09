package com.example.cariaid.ui.dashboard.donation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cariaid.R
import com.example.cariaid.databinding.FragmentDonateDetailsBinding
import com.example.cariaid.databinding.FragmentDonationTypeBinding

class DonationTypeFragment : Fragment() {
    lateinit var binding:FragmentDonationTypeBinding
    var isCash:Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationTypeBinding.inflate(inflater,container, false)
        retrieveData()
        setUpUI()
        return binding.root
    }

    private fun setUpUI() {
        with(binding){
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            cash.isChecked = true
            cash.setOnClickListener {
                isCash=true
                cash.isChecked = true
            }
            item.setOnClickListener {
                isCash=false
                item.isChecked = true
            }
            cash.setOnCheckedChangeListener { card, isChecked ->
                if (isChecked){
                    isCash=true
                    item.isCheckable=true
                    item.isChecked =false

                }
            }
            item.setOnCheckedChangeListener { card, isChecked ->
                if (isChecked){
                    isCash=false
                    cash.isCheckable=true
                    cash.isChecked =false
                }
            }
        }

    }

    private fun retrieveData(){
        arguments?.let { arg->
            val data=DonationTypeFragmentArgs.fromBundle(arg).CharityData
            binding.next.setOnClickListener {
                val action = DonationTypeFragmentDirections.actionDonationTypeFragmentToDonationCompletionFragment(data,isCash)
                findNavController().navigate(action)
            }
        }
    }


}