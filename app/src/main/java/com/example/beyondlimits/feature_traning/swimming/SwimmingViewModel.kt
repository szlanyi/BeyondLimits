package com.example.beyondlimits.ui.swimming

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SwimmingViewModel : ViewModel() {
    private val _text = MutableStateFlow("This is the Swim screen")
    val text: StateFlow<String> = _text
}
