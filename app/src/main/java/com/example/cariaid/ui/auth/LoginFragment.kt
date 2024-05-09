package com.example.cariaid.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cariaid.R
import com.example.cariaid.databinding.FragmentLoginBinding
import com.example.cariaid.ui.dashboard.DashBoardActivity
import com.example.cariaid.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    lateinit var dialog: ProgressDialog
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setUpAuth()
        subscribeObserver()
        return binding.root
    }

    private fun subscribeObserver() {
       viewLifecycleOwner.lifecycleScope.launch {
           viewModel.UiState.collect {
               when(it){
                   is ResultState.Empty -> {

                   }
                   is ResultState.Error -> {
                       hideDialog()
                       showMaterialDialog(message = it.error.toString())
                   }
                   is ResultState.Loading -> {showProgressDialog()}
                   is ResultState.Success -> {
                       startActivity(Intent(requireContext(),DashBoardActivity::class.java))
                       hideDialog()
                   }
               }
           }
       }
    }

    private fun setUpAuth() {
        with(binding){
            emailLayout.autoClearError()
            passwordLayout.autoClearError()
            close.setOnClickListener {
                activity?.finishAffinity()
            }
            signupBtn.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            LoginBtn.setOnClickListener {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                if (isValidated()){
                    viewModel.authUser(email, password)
                }
            }
        }
    }

    private fun isValidated():Boolean{
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        return when{
            email.isEmpty()->{
                binding.emailLayout.error ="Email cannot be empty"
                false
            }
            !isValidEmail(email) ->{
                binding.passwordLayout.error ="Invalid email format"
                false
            }
            password.isEmpty()->{
                binding.passwordLayout.error ="Password cannot be empty"
                false
            }
            password.count()<6-> {
                binding.passwordLayout.error = " "
                binding.passwordLayout.helperText = "Min of 6 char required"
                false
            }

            else->{
                true
            }
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