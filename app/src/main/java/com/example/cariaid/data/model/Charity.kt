package com.example.cariaid.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class CharityData(
    val charityName:String="",
    val charityIcon:String="",
    val donationHeading:String="",
    val donationDesc:String="",
    val donationAmount:String="",
    val donationRaised:String="",
    val organisedBy:String="",
    val charityType:String="",
    val imageUrl:String="",
    val createdOn:String="",
    val key:String=""
):Serializable{
    @Exclude
    fun toMap():Map<String,Any>{
        return mapOf(
            "donationRaised" to donationRaised
        )
    }
}