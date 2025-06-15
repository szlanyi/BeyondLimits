package com.example.beyondlimits.ui.cycling

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CyclingViewModel : ViewModel() {
    private val _text = MutableStateFlow("This is the Cycling screen")
    val text: StateFlow<String> = _text
}
