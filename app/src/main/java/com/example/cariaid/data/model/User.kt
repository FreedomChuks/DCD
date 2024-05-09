package com.example.cariaid.data.model

data class User(
    val firstName:String,
    val lastName:String,
)

data class UserResponse(
    val firstName:String="",
    val lastName:String="",
)