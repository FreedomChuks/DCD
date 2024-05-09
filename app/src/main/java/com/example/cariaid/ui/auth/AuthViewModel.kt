package com.example.cariaid.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cariaid.data.AuthRepository
import com.example.cariaid.data.model.User
import com.example.cariaid.utils.ResultState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private var _UiState = MutableStateFlow<ResultState<FirebaseUser>>(ResultState.Empty())
    val UiState :StateFlow<ResultState<FirebaseUser>> = _UiState.asStateFlow()

    fun authUser(email:String,password:String){
        viewModelScope.launch {
            authRepository.signIn(email, password).collect {
                _UiState.value = it
            }
        }
    }

    fun signUpUser(email: String,password: String,user: User){
        viewModelScope.launch {
            authRepository.register(email, password,user).collect {
                _UiState.value = it
            }
        }
    }


    fun saveUserCred(user:User){
        authRepository.createUser(user)
    }

}