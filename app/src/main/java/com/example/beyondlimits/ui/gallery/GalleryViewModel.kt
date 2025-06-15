package com.example.beyondlimits.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GalleryViewModel : ViewModel() {
    private val _text = MutableStateFlow("This is the Gallery screen")
    val text: StateFlow<String> = _text
}
