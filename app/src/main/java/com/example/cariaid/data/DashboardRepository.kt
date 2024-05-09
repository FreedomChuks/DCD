package com.example.cariaid.data

import android.util.Log
import com.example.cariaid.data.model.*
import com.example.cariaid.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DashboardRepository @Inject constructor() {
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = Firebase.database.reference

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchCharities()= callbackFlow{
        trySend(ResultState.Loading())
        database.child("charity").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val response = snapshot.children.map {
                    it.getValue<CharityData>()
                }
                trySend(ResultState.Success(response))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
                trySend(ResultState.Empty())
            }

        })
        awaitClose {
            close()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchCharityWithLimit() = callbackFlow {
        trySend(ResultState.Loading())
        database.child("charity").limitToFirst(3).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val response = snapshot.children.map {
                    it.getValue<CharityData>()
                }
                trySend(ResultState.Success(response))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
                trySend(ResultState.Empty())
            }

        })
        awaitClose {
            cancel()
        }
    }

    //fetch list of charities
    //update


    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchUser() = callbackFlow {
        val currentUser = firebaseAuth.currentUser
        trySend(ResultState.Loading())
        database.child("users").child(currentUser?.uid.toString()).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val response = snapshot.getValue(UserResponse::class.java)
                trySend(ResultState.Success(response))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
                trySend(ResultState.Empty())
            }

        })
        awaitClose {
            cancel()
        }
    }

    fun updateCharity(donationRaised:String,key:String){
        val post = CharityData(
            donationRaised = donationRaised
        )
        val postValues = post.toMap()
        database.child("charity").child(key).updateChildren(postValues)
    }


    private fun updateHistory(totalDonatedAmount:String,totalDonated:String){
        val post = History(
            totalDonatedAmount = totalDonatedAmount,
            totalDonated = totalDonated
        )
        val postValues = post.toMap()
        database.child("history").updateChildren(postValues)
    }




    private fun createHistory(history: CharityHistory){
        database.child("history").child("charityHistory").push().setValue(history)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchHistory(charityHistory: CharityHistory) = callbackFlow{
        trySend(ResultState.Loading())
        createHistory(charityHistory)
        var totalAmount =0
        database.child("history").child("charityHistory").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val childCount = snapshot.childrenCount
                val list = snapshot.children.map { it.getValue(CharityHistory::class.java)!! }
                list.forEach {
                    totalAmount+=it.amountDonated.toInt()
                }
                updateHistory(totalAmount.toString(),childCount.toString())
                trySend(ResultState.Success(list))
                Log.d("###","totalAmount $totalAmount  $childCount  $list")
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
                trySend(ResultState.Empty())
            }

        })
        awaitClose {
            close()
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchHistoryList() = callbackFlow{
        trySend(ResultState.Loading())
        database.child("history").child("charityHistory").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.map { it.getValue(CharityHistory::class.java)!! }
                trySend(ResultState.Success(list))
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
                trySend(ResultState.Empty())
            }

        })
        awaitClose {
            close()
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchHistoryCount() = callbackFlow{
        trySend(ResultState.Loading())
        database.child("history").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list =  snapshot.getValue(History::class.java)
                trySend(ResultState.Success(list))
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.message))
                trySend(ResultState.Empty())
            }

        })
        awaitClose {
            close()
        }

    }




}