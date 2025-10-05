package com.example.beyondlimits.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beyondlimits.data.remote.model.User
import com.example.beyondlimits.data.repository.Repository

import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var state by mutableStateOf(AuthState())
        private set

    var currentUser by mutableStateOf<User?>(Repository.currentUser)
        private set

    fun register(email: String, password: String) {
        state = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = Repository.register(email, password)
            state = if (result.isSuccess) {
                currentUser = result.getOrNull()
                AuthState(isSuccess = true)
            } else {
                AuthState(errorMessage = result.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun login(email: String, password: String) {
        state = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = Repository.login(email, password)
            state = if (result.isSuccess) {
                currentUser = result.getOrNull()
                AuthState(isSuccess = true)
            } else {
                AuthState(errorMessage = result.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun logout() {
        Repository.logout()
        currentUser = null
    }
}