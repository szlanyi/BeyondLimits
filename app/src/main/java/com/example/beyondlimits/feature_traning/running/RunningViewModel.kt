package com.example.beyondlimits.feature_traning.running

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.beyondlimits.feature_location.LocationTracker
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng
import kotlin.math.*

class RunningViewModel(application: Application) : AndroidViewModel(application) {

    // UI-States
    var isRunning = mutableStateOf(false)
        private set
    var distanceKm = mutableStateOf(0.0)
        private set
    var timeSec = mutableStateOf(0)
        private set
    var pace = mutableStateOf("–")
        private set

    private val tracker = LocationTracker(getApplication())
    private var timerJob: Job? = null
    private var lastLocation: LatLng? = null

    // Liste der GPS-Punkte (Route)
    val routePoints = mutableListOf<LatLng>()

    init {
        // ✅ Echtzeit-Position überwachen
        viewModelScope.launch {
            tracker.getLocationUpdates().collectLatest { location ->
                if (isRunning.value) {
                    updateDistance(location)
                }
            }
        }
    }

    // ---------------- START / STOP ----------------

    fun toggleRun() {
        if (isRunning.value) stopRun() else startRun()
    }

    fun startRun() {
        isRunning.value = true
        lastLocation = null
        routePoints.clear()
        timeSec.value = 0
        distanceKm.value = 0.0
        pace.value = "–"

        timerJob = viewModelScope.launch {
            while (isRunning.value) {
                delay(1000)
                timeSec.value += 1
                updatePace()
            }
        }
    }

    fun stopRun() {
        isRunning.value = false
        timerJob?.cancel()
        lastLocation = null
    }

    fun reset() {
        stopRun()
        distanceKm.value = 0.0
        timeSec.value = 0
        pace.value = "–"
        routePoints.clear()
    }

    // ---------------- BERECHNUNG ----------------

    private fun updateDistance(newLocation: LatLng) {
        lastLocation?.let { prev ->
            val dist = haversine(
                prev.latitude, prev.longitude,
                newLocation.latitude, newLocation.longitude
            )
            if (dist > 0.003) { // filter rauschen < 3m
                distanceKm.value += dist
                routePoints.add(newLocation)
                updatePace()
            }
        }
        lastLocation = newLocation
    }

    private fun updatePace() {
        if (distanceKm.value > 0) {
            val minPerKm = (timeSec.value / 60.0) / distanceKm.value
            val mins = floor(minPerKm).toInt()
            val secs = ((minPerKm - mins) * 60).toInt()
            pace.value = String.format("%d:%02d min/km", mins, secs)
        }
    }

    // ---------------- DISTANZFORMEL ----------------
    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Erdradius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
}
