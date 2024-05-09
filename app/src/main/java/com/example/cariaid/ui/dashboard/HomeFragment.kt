package com.example.cariaid.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cariaid.R
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.databinding.FragmentHomeBinding
import com.example.cariaid.ui.dashboard.adapter.CharityAdapter
import com.example.cariaid.ui.dashboard.adapter.CharityDashboardAdapter
import com.example.cariaid.utils.ResultState
import com.example.cariaid.utils.showMaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    private var charityAdapter = CharityDashboardAdapter { character -> onClick(character) }

    private val viewModel by viewModels<DashboardVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        setUpUI()
        subscribeObserver()
        return binding.root
    }

    private fun subscribeObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dashboardUiState.collect {
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
                        binding.list.adapter=charityAdapter
                    }
                }
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userUiState.collect {
                it.data?.let { user->
                    binding.Greetings.text = "Hello\n${user.firstName.uppercase()} "
                }
            }
        }
    }

    private fun setUpUI() {
        binding.list.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        binding.list.adapter = charityAdapter
    }

    private fun onClick(character: CharityData) {
        val action = HomeFragmentDirections.actionNavigationHomeToDonateDetailsFragment(character)
        findNavController().navigate(action)
    }

}