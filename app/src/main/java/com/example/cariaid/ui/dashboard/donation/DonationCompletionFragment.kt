package com.example.cariaid.ui.dashboard.donation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cariaid.R
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.data.model.CharityHistory
import com.example.cariaid.data.model.History
import com.example.cariaid.databinding.FragmentDonationCompletionBinding
import com.example.cariaid.ui.dashboard.DashboardVM
import com.example.cariaid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DonationCompletionFragment : Fragment() {
    private val viewModel by viewModels<DashboardVM>()
    lateinit var binding:FragmentDonationCompletionBinding
    lateinit var dialog: ProgressDialog
    lateinit var data:CharityData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationCompletionBinding.inflate(inflater,container, false)
        retrieveData()
        setUpUI()
        subscribeObserver()
        return binding.root
    }

    private fun subscribeObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
        viewModel.updateUserUiState.collect {
            when(it){
                is ResultState.Empty -> {}
                is ResultState.Error -> {
                    showMaterialDialog(message = it.error.toString())
                }
                is ResultState.Loading -> {
                    showProgressDialog()
                }
                is ResultState.Success -> {
                    val action = DonationCompletionFragmentDirections.actionDonationCompletionFragmentToSuccess(data,binding.Amount.text.toString())
                    findNavController().navigate(action)
                    hideDialog()
                }
            }
        }
        }
    }

    private fun setUpUI() {
        with(binding){
            Amount.amountFormattingTextWatcher()
            cardHolderNumberLabel.autoClearError()
            cardHolderNameLabel.autoClearError()
            AmountLabel.autoClearError()
            cardCvvLabel.autoClearError()

            btn1.setOnClickListener {
                Amount.setText("10")
            }
            btn2.setOnClickListener {
                Amount.setText("50")
            }
            btn3.setOnClickListener {
                Amount.setText("100")
            }
            btn4.setOnClickListener { Amount.setText("200") }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            Amount.doOnTextChanged { text, start, before, count ->
                amountConfirm.text = "£${text}"
            }

        }
    }


    private fun retrieveData(){
        arguments?.let {
            val charity= DonationCompletionFragmentArgs.fromBundle(it).CharityData
            val donationType = DonationCompletionFragmentArgs.fromBundle(it).isCash
            data = charity
            Log.d("###","donationType $donationType")
            binding.confirm.setOnClickListener {
                if (donationType){
                    if (isValidated()){
                        val result = +charity.donationRaised.removeCurrency.toInt()
                            .plus(binding.Amount.text.toString().toInt())
                        viewModel.updateDonationAmount(result.toString(), charity.key)
                        val history = CharityHistory(
                            charityName = charity.charityName,
                            charityIcon = charity.charityIcon,
                            amountDonated = binding.Amount.text.toString()
                        )
                        viewModel.updateHistory(history)
                    }
                }else{
                    val action = DonationCompletionFragmentDirections.actionDonationCompletionFragmentToSuccess(data,binding.itemDonation.text.toString())
                    findNavController().navigate(action)
                }


            }
            if (donationType){
                binding.cashDonationField.visibility= VISIBLE
                binding.linearLayout.visibility = VISIBLE
                binding.AmountLabel.visibility = VISIBLE
                binding.itemDonationLabel.visibility = GONE
            }else{
                 val item:ArrayList<String> = arrayListOf("Clothes", "Furniture",
                     "Food","Knitted items And Blankets",
                     "Shoes and Bags"
                     ,"Accessories And jewelry",
                     "Books","Children Toys","Paintings","Sports Equipment","Medical Instrument")
                val itemAdapter = ArrayAdapter(requireContext(), R.layout.donation_item, item)
                (binding.itemDonation as? AutoCompleteTextView)?.setAdapter(itemAdapter)

                binding.itemDonationField.visibility= VISIBLE
                binding.textView7.text = "What would you\nlike to donate"
                binding.linearLayout.visibility = GONE
                binding.AmountLabel.visibility = INVISIBLE
                binding.itemDonationLabel.visibility = VISIBLE
            }
            with(binding){
                charityName.text = charity.charityName
                charityType.text = charity.organisedBy
                charityIcon.load(charity.charityIcon)
            }



        }
    }

    private fun isValidated():Boolean{
        val amount = binding.Amount.text.toString()
        val cardPanNumber = binding.CardHolderNumber.text.toString()
        val cardHolderName = binding.CardHolderName.text.toString()
        val expDate = binding.cardExp.text.toString()
        val cvv = binding.cardCvv.text.toString()

        return when{
            amount.isEmpty()->{
                binding.AmountLabel.error = "Amount cannot be empty"
                false
            }
            binding.Amount.toString().length<10->{
                binding.AmountLabel.error = "Amount cannot be less than £1 0"
                false
            }
            cardHolderName.isEmpty()->{
                binding.cardHolderNameLabel.error= "card HolderName cannot be empty"
                false
            }
            cardPanNumber.isEmpty()->{
                binding.cardHolderNumberLabel.error = "cardPan number cannot be empty "
                false
            }
            expDate.isEmpty()->{
                binding.cardExpLabel.error = "Expiry date cannot be empty "
                false
            }
            cvv.isEmpty()->{
                binding.cardCvvLabel.error = "CVV cannot be empty "
                false
            }

            else->{true}
        }
    }

    private fun showProgressDialog() {
        dialog = ProgressDialog()
        dialog.show(parentFragmentManager, "tag")
        dialog.isCancelable = false
    }

    private fun hideDialog() {
        if (this::dialog.isInitialized) {
            dialog.dismiss()
        }
    }

}