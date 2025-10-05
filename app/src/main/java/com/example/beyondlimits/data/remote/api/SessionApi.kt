package com.example.beyondlimits.data.remote.api

import com.example.beyondlimits.data.remote.model.Session

interface SessionApi {
    suspend fun addSession(session: Session): Result<Unit>
    suspend fun getSessions(): Result<List<Session>>
    suspend fun getSessionById(id: String): Result<Session?>
    suspend fun deleteSession(id: String): Result<Unit>
}
