package com.example.cariaid.ui.dashboard.donation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cariaid.databinding.FragmentDonateDetailsBinding
import com.example.cariaid.utils.removeCurrency
import kotlin.math.roundToInt


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

    private fun setUpUI() {
       with(binding){
           toolbar.setNavigationOnClickListener {
               findNavController().navigateUp()
           }
       }
    }

    private fun retrieveFromArgs(){
        arguments?.let {
            val charity= DonateDetailsFragmentArgs.fromBundle(it).CharityData
            with(binding){
                donate.setOnClickListener {
                    val action = DonateDetailsFragmentDirections.actionDonateDetailsFragmentToDonationTypeFragment(charity)
                    findNavController().navigate(action)
                }
                charityNames.text = charity.charityName
                charityDesc.text = charity.donationDesc
                charityImage.load(charity.imageUrl)
                targetAmount.text = charity.donationAmount
                raisedAmount.text = "£${charity.donationRaised}"
                foundationName.text = charity.organisedBy
                foundationImage.load(charity.charityIcon)
                if (charity.donationRaised.removeCurrency.toInt()==0){
                    progress.setProgress(0,true)
                    percentageText.text ="0%"
                }else{
                    val percentage = charity.donationRaised.removeCurrency.toDouble().div(charity.donationAmount.removeCurrency.toDouble()).times(100)
                    percentageText.text = "${percentage.roundToInt()}%"
                    progress.setProgress(percentage.roundToInt(),true)
                }
            }
        }
    }

}