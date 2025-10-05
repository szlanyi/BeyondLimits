package com.example.beyondlimits.feature_traning.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProgressViewModel : ViewModel() {

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text
}