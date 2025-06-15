package com.example.beyondlimits.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SlideshowViewModel : ViewModel() {

    private val _text = MutableStateFlow("This is the Slide screen")
    val text: StateFlow<String> = _text
}