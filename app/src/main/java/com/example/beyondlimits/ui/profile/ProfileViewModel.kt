package com.example.beyondlimits.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val _text = MutableStateFlow("This is the Profile screen")
    val text: StateFlow<String> = _text
}
