package com.example.beyondlimits.ui.progress

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProgressViewModel : ViewModel() {

    private val _text = MutableStateFlow("This is the Progress screen")
    val text: StateFlow<String> = _text
}