package com.example.cariaid.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cariaid.R
import com.example.cariaid.databinding.FragmentProfileBinding
import com.example.cariaid.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding:FragmentProfileBinding
    private val viewModel by viewModels<DashboardVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container, false)
        setUpUI()
        subscribeObserver()
        return binding.root
    }

    private fun subscribeObserver() {
        val email = FirebaseAuth.getInstance().currentUser?.email
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userUiState.collect {
                it.data?.let { user->
                    with(binding){
                        userName.text = "${user.firstName} ${user.lastName}"
                        charityIcon.text ="${user.firstName.first().uppercase()}${user.lastName.first().uppercase()}"
                        userEmail.text = email
                        firstNameInput.setText(user.firstName)
                        lastNameInput.setText(user.lastName)
                        emailInput.setText(email)
                    }


                }
            }
        }
    }

    private fun setUpUI() {
        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context , AuthActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }
    }
}


