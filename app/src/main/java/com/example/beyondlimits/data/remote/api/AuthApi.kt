package com.example.beyondlimits.data.remote.api

import com.example.beyondlimits.data.remote.model.User
import kotlinx.coroutines.flow.Flow

interface AuthApi {
    suspend fun register(email: String, password: String, displayName: String? = null): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    fun logout()
    val currentUser: User?
    val authStateChanges: Flow<User?>
}
