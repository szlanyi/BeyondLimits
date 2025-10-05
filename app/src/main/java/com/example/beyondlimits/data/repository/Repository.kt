package com.example.beyondlimits.data.repository

import com.example.beyondlimits.data.remote.AuthDataSource
import com.example.beyondlimits.data.remote.SessionDataSource
import com.example.beyondlimits.data.remote.model.Session

object Repository {

    private val authDataSource = AuthDataSource()
    private val sessionDataSource = SessionDataSource()

    val currentUser get() = authDataSource.currentUser

    suspend fun register(email: String, password: String, displayName: String?) =
        authDataSource.register(email, password)

    suspend fun login(email: String, password: String) =
        authDataSource.login(email, password)

    fun logout() = authDataSource.logout()

    suspend fun addSession(session: Session) =
        sessionDataSource.addSession(session)

    suspend fun getSessions() =
        sessionDataSource.getSessions()

    suspend fun deleteSession(id: String) =
        sessionDataSource.deleteSession(id)
}
