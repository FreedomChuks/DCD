package com.example.cariaid.data

import com.example.cariaid.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class CardiaidRepository {
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun signIn(email:String, password:String)= callbackFlow<ResultState<FirebaseUser>>{
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                trySend(ResultState.Success(firebaseAuth.currentUser))
            }else{
                trySend(ResultState.Error(it.exception?.message))
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun register(email:String, password:String)= callbackFlow<ResultState<FirebaseUser>>{
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                trySend(ResultState.Success(firebaseAuth.currentUser))
            }else{
                trySend(ResultState.Error(it.exception?.message))
            }
        }
    }
}