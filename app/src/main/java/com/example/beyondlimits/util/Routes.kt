package com.example.beyondlimits.util

import kotlinx.serialization.Serializable

interface Route

@Serializable
object Home : Route
@Serializable
object Gallery : Route
@Serializable
object Slideshow : Route

@Serializable
object Login: Route

@Serializable
object Register: Route

@Serializable
object AuthNav : Route

@Serializable
object Main:Route