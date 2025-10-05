package com.example.beyondlimits.feature_traning.running

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.beyondlimits.data.remote.model.*
import com.example.beyondlimits.data.repository.Repository
import com.example.beyondlimits.feature_location.LocationTracker
import com.google.firebase.Timestamp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng
import kotlin.math.*

class RunningViewModel(application: Application) : AndroidViewModel(application) {

    // ---- States ----
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

    // Routepunkte
    private val routePoints = mutableListOf<RoutePoint>()

    init {
        // 🛰️ Echtzeit-GPS
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
        distanceKm.value = 0.0
        timeSec.value = 0
        pace.value = "–"
        routePoints.clear()
        lastLocation = null

        timerJob = viewModelScope.launch {
            while (isRunning.value) {
                delay(1000)
                timeSec.value++
                updatePace()
            }
        }
    }

    fun stopRun() {
        if (!isRunning.value) return
        isRunning.value = false
        timerJob?.cancel()

        // 📦 Session speichern
        viewModelScope.launch {
            try {
                val session = Session(
                    id = "",
                    date = Timestamp.now(),
                    duration = timeSec.value,
                    totalDistance = distanceKm.value,
                    weather = Weather("sunny"),
                    deviceInfo = DeviceInfo(
                        deviceModel = android.os.Build.MODEL,
                        osVersion = "Android ${android.os.Build.VERSION.RELEASE}"
                    ),
                    segments = listOf(
                        Segment(
                            distance = distanceKm.value,
                            duration = timeSec.value,
                            metrics = Metrics(
                                avgPace = calculateAvgPace(),
                                hearthRate = (120..160).random() // Fake HR bis Sensor kommt ;)
                            ),
                            route = routePoints
                        )
                    ),
                    tags = listOf("run", "training"),
                    temperature = 21,
                    windSpeed = 10
                )

                val result = Repository.addSession(session)
                if (result.isSuccess) {
                    resetAfterSave()
                } else {
                    println("❌ Fehler beim Speichern: ${result.exceptionOrNull()?.message}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun resetAfterSave() {
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
            if (dist > 0.003) { // Filter für GPS-Rauschen
                distanceKm.value += dist
                routePoints.add(
                    RoutePoint(
                        lat = newLocation.latitude,
                        lng = newLocation.longitude,
                        type = "RUN"
                    )
                )
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

    private fun calculateAvgPace(): Double {
        val distance = distanceKm.value
        val time = timeSec.value
        return if (distance > 0) (time / 60.0) / distance else 0.0
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
}
