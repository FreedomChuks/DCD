package com.example.cariaid.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.databinding.FragmentDonateBinding
import com.example.cariaid.ui.dashboard.adapter.CharityAdapter
import com.example.cariaid.utils.ResultState
import com.example.cariaid.utils.showMaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DonateFragment : Fragment() {
    lateinit var binding: FragmentDonateBinding
    private var characterAdapter =CharityAdapter { character -> onClick(character) }
    private val viewModel by viewModels<DashboardVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonateBinding.inflate(inflater,container, false)
        setUpUI()
        subscribeObserver()
        return binding.root
    }

    private fun subscribeObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                when(it){
                    is ResultState.Empty -> {

                    }
                    is ResultState.Error -> {
//                        hideDialog()
                        showMaterialDialog(message = it.error.toString())
                    }
                    is ResultState.Loading -> {
//                        showProgressDialog()
                    }

                    is ResultState.Success -> {
                        characterAdapter.submitList(it.data)
                        binding.listView.adapter=characterAdapter
                    }
                }
            }
        }
    }

    private fun onClick(character: CharityData) {
        val action = DonateFragmentDirections.actionNavigationDonationToDonateDetailsFragment(character)
        findNavController().navigate(action)
    }


    private fun  setUpUI(){
        binding.listView.layoutManager= LinearLayoutManager(context, LinearLayout.VERTICAL,false)
        binding.listView.adapter = characterAdapter
    }


}