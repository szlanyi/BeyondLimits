package com.example.beyondlimits.data.remote

import com.example.beyondlimits.data.remote.api.SessionApi
import com.example.beyondlimits.data.remote.model.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SessionDataSource : SessionApi {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun sessionRef() = db
        .collection("users")
        .document(auth.currentUser?.uid ?: throw Exception("User not logged in"))
        .collection("sessions")

    override suspend fun addSession(session: Session): Result<Unit> {
        return try {
            val newRef = sessionRef().document("session_${System.currentTimeMillis()}")
            val data = session.copy(id = newRef.id)
            newRef.set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSessions(): Result<List<Session>> {
        return try {
            val snapshot = sessionRef().get().await()
            val list = snapshot.toObjects(Session::class.java)
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSessionById(id: String): Result<Session?> {
        return try {
            val doc = sessionRef().document(id).get().await()
            Result.success(doc.toObject(Session::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSession(id: String): Result<Unit> {
        return try {
            sessionRef().document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
