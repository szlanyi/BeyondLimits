package com.example.beyondlimits.data.remote.model

data class User(
    val uid: String,
    val email: String?,
    val displayName: String? = null,
    val profileImageUrl: String? = null
)