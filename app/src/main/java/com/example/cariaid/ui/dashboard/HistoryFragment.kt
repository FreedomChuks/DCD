package com.example.cariaid.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cariaid.R
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.data.model.CharityHistory
import com.example.cariaid.databinding.FragmentHistoryBinding
import com.example.cariaid.ui.dashboard.adapter.CharityDashboardAdapter
import com.example.cariaid.ui.dashboard.adapter.CharityHistoryAdapter
import com.example.cariaid.utils.ResultState
import com.example.cariaid.utils.showMaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    lateinit var binding:FragmentHistoryBinding
    private var charityAdapter = CharityHistoryAdapter { charity -> onClick(charity) }

    private fun onClick(character: CharityHistory) {

    }

    private val viewModel by viewModels<DashboardVM>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater,container, false)
        subscribeObserver()
        setUpUI()
        return binding.root
    }

    private fun subscribeObserver() {
        viewModel.fetchHistoryMetadata()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.historyUiState.collect {
                when(it){
                    is ResultState.Empty -> {

                    }
                    is ResultState.Error -> {
                        showMaterialDialog(message = it.error.toString())
                    }
                    is ResultState.Loading -> {

                    }
                    is ResultState.Success -> {
                        charityAdapter.submitList(it.data)
                        binding.listView.adapter=charityAdapter
                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.historyMetaUiState.collect {
                Log.d("123","${it.data}")
                it.data?.let {res->
                    binding.TotalDonated.text = "£${res.totalDonatedAmount}"
                    binding.NumberOfDonations.text = "${res.totalDonated} Donations"
                }
            }
        }
    }

    private fun  setUpUI(){
        binding.listView.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        binding.listView.adapter = charityAdapter
    }

}