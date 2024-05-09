package com.example.cariaid.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class History(
    val totalDonatedAmount:String="",
    val totalDonated:String="",
){
    @Exclude
    fun toMap():Map<String,Any>{
        return mapOf(
            "totalDonatedAmount" to totalDonatedAmount,
            "totalDonated" to totalDonated
        )
    }
}

data class CharityHistory(
    val amountDonated:String="",
    val charityName:String="",
    val charityIcon:String="",
    val timeStamp:String=""
)