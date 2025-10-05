package com.example.beyondlimits.feature_progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beyondlimits.data.remote.model.Session
import com.example.beyondlimits.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgressViewModel : ViewModel() {
    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions: StateFlow<List<Session>> = _sessions

    fun loadSessions() {
        viewModelScope.launch {
            val result = Repository.getSessions()
            if (result.isSuccess) _sessions.value = result.getOrNull().orEmpty()
        }
    }
}
