package com.example.cariaid.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cariaid.R
import com.example.cariaid.data.model.User
import com.example.cariaid.databinding.FragmentLoginBinding
import com.example.cariaid.databinding.FragmentRegisterBinding
import com.example.cariaid.ui.dashboard.DashBoardActivity
import com.example.cariaid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding:FragmentRegisterBinding
    lateinit var dialog: ProgressDialog
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container, false)
        setUpUI()
        subscribeObserver()
        return binding.root
    }

    private fun subscribeObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.UiState.collect {
                Log.d("###","${it.data?.email} ${it.error}")
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


    private fun setUpUI() {
        with(binding){
            emailLabel.autoClearError()
            passwordLabel.autoClearError()
            firstNameLabel.autoClearError()
            lastNameLabel.autoClearError()
            back.setOnClickListener {
                findNavController().navigateUp()
            }
            SignUp.setOnClickListener {
                val email = binding.emailInput.text.toString()
                val password = binding.passwordInput.text.toString()
                val firstName = binding.firstNameInput.text.toString()
                val lastName = binding.lastNameInput.text.toString()
                val user = User(firstName=firstName, lastName=lastName)
                if (isValidated()){
                    viewModel.signUpUser(email, password ,user)
                }
            }

        }
    }


    private fun isValidated():Boolean{
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val firstName = binding.firstNameInput.text.toString()
        val lastName = binding.lastNameInput.text.toString()
        return when{
            firstName.isEmpty()->{
                binding.firstNameLabel.error = "Firstname cannot be empty "
               false
            }
            lastName.isEmpty()->{
                binding.lastNameLabel.error = "Lastname cannot be empty "
                false
            }
            email.isEmpty()->{
                binding.emailLabel.error ="Email cannot be empty"
                false
            }
            !isValidEmail(email) ->{
                binding.passwordLabel.error ="Invalid email format"
                false
            }
            password.isEmpty()->{
                binding.passwordLabel.error ="Password cannot be empty"
                false
            }
            password.count()<6-> {
                binding.passwordLabel.error = " "
                binding.passwordLabel.helperText = "Min of 6 char required"
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