package com.example.cariaid.data

import android.util.Log
import com.example.cariaid.data.model.User
import com.example.cariaid.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepository @Inject constructor() {
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference



    @OptIn(ExperimentalCoroutinesApi::class)
    fun signIn(email: String, password: String) = callbackFlow<ResultState<FirebaseUser>> {
        trySend(ResultState.Loading())
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResultState.Success(firebaseAuth.currentUser))
                trySend(ResultState.Empty())
            } else {
                trySend(ResultState.Error(it.exception?.message))
                trySend(ResultState.Empty())
            }
        }
        awaitClose {
            close()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun register(email: String, password: String,user: User) = callbackFlow<ResultState<FirebaseUser>> {
        trySend(ResultState.Loading())
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResultState.Success(firebaseAuth.currentUser))
                createUser(user)
            } else {
                trySend(ResultState.Error(it.exception?.message))
                trySend(ResultState.Empty())

            }
        }
        awaitClose {
            close()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun createUser(user:User){
        val currentUser = firebaseAuth.currentUser
        database = Firebase.database.reference
        database.child("users").child(currentUser?.uid.toString()).setValue(user).addOnSuccessListener {
            Log.d("###","data saved ")
        }.addOnFailureListener {
            Log.d("###","data failed ${it.message}")
        }

    }
}