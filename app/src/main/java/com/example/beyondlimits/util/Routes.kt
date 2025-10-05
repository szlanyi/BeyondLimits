package com.example.beyondlimits.util

import kotlinx.serialization.Serializable

interface Route

@Serializable
object Home : Route
@Serializable
object Profile : Route

@Serializable
object Progress : Route

@Serializable
object Login: Route

@Serializable
object AuthNav : Route

@Serializable
object Main:Route

@Serializable
object Running : Route
@Serializable
object Cycling : Route
@Serializable
object Swimming : Route
@Serializable
object Triathlon : Route

@Serializable
object Splash : Route