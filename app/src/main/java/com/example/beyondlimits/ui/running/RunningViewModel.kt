package com.example.beyondlimits.ui.running

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RunningViewModel : ViewModel() {
    private val _text = MutableStateFlow("This is the Run screen")
    val text: StateFlow<String> = _text
}