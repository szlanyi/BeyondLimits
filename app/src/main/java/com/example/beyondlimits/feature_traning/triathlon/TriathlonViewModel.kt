package com.example.beyondlimits.ui.triathlon

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TriathlonViewModel : ViewModel() {
    private val _text = MutableStateFlow("This is the Triahtlon screen")
    val text: StateFlow<String> = _text
}
