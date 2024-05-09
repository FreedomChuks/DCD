package com.example.cariaid.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cariaid.R
import com.example.cariaid.databinding.FragmentDonateDetailsBinding

class DonateDetailsFragment : Fragment() {
lateinit var binding:FragmentDonateDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentDonateDetailsBinding.inflate(inflater,container, false)
        retrieveFromArgs()
        setUpUI()
        return binding.root
    }

    @SuppressLint("NewApi")
    private fun setUpUI() {
       with(binding){
           progress.setProgress(70,true)
           toolbar.setNavigationOnClickListener {
               findNavController().navigateUp()
           }
           donate.setOnClickListener {
               findNavController().navigate(R.id.action_donateDetailsFragment_to_donationTypeFragment)
           }

       }
    }

    private fun retrieveFromArgs(){
        arguments?.let {
            val charity= DonateDetailsFragmentArgs.fromBundle(it).CharityData
            with(binding){
                charityNames.text = charity.charityName
                charityDesc.text = charity.donationDesc
                charityImage.load(charity.imageUrl)
                targetAmount.text = charity.donationAmount
                raisedAmount.text = charity.donationRaised
                foundationName.text = charity.organisedBy
                foundationImage.load(charity.charityIcon)
            }
        }
    }

}