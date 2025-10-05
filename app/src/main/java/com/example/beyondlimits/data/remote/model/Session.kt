package com.example.beyondlimits.data.remote.model

import com.google.firebase.Timestamp

data class Session(
    val id: String = "",
    val date: Timestamp = Timestamp.now(),
    val duration: Int = 0,
    val totalDistance: Double = 0.0,
    val weather: Weather? = null,
    val deviceInfo: DeviceInfo? = null,
    val segments: List<Segment> = emptyList(),
    val tags: List<String> = emptyList(),
    val temperature: Int? = null,
    val windSpeed: Int? = null
)

data class Weather(
    val condition: String = ""
)

data class DeviceInfo(
    val deviceModel: String = "",
    val osVersion: String = ""
)

data class Segment(
    val distance: Double = 0.0,
    val duration: Int = 0,
    val metrics: Metrics? = null,
    val route: List<RoutePoint> = emptyList()
)

data class Metrics(
    val avgPace: Double = 0.0,
    val hearthRate: Int = 0
)

data class RoutePoint(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val type: String = ""
)
