package com.example.cariaid.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cariaid.R
import com.example.cariaid.databinding.FragmentSuccessBinding

class Success : Fragment() {
    lateinit var binding:FragmentSuccessBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessBinding.inflate(inflater,container,false)
        retrieveData()
        setUpUI()
        return binding.root
    }

    private fun retrieveData() {
        arguments?.let {
            val data = SuccessArgs.fromBundle(it).charity
            val amount = SuccessArgs.fromBundle(it).Amount
            binding.textView12.text = "You have Succesfully\n Donated ${amount} to ${data.charityName}"
        }
    }

    private fun setUpUI() {
        with(binding){
            button2.setOnClickListener {
                findNavController().navigate(R.id.navigation_home)
            }
        }
    }


}