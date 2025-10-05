package com.example.beyondlimits.feature_home

import androidx.lifecycle.ViewModel
import com.example.beyondlimits.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _displayName = MutableStateFlow<String?>(null)
    val displayName = _displayName.asStateFlow()

    init {
        val user = Repository.currentUser
        print("message " + user?.displayName )

        _displayName.value = user?.displayName ?: user?.email ?: "Athlete"
    }
}
