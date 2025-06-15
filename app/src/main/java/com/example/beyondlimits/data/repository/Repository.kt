package com.example.beyondlimits.data.repository

import com.example.beyondlimits.data.remote.AuthDataSource
import com.example.beyondlimits.data.remote.api.AuthApi

object Repository : AuthApi by AuthDataSource() {
}