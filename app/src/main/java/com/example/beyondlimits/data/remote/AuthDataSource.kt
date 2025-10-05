package com.example.beyondlimits.data.remote

import com.example.beyondlimits.data.remote.api.AuthApi
import com.example.beyondlimits.data.remote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthDataSource : AuthApi {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun register(
        email: String,
        password: String,
        displayName: String?
    ): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("User creation failed"))

            val user = firebaseUser.toUser()

            // ✅ Benutzer-Dokument in Firestore anlegen
            val userDoc = hashMapOf(
                "uid" to user.uid,
                "email" to user.email,
                "displayName" to (user.displayName ?: "New Athlete"),
                "createdAt" to com.google.firebase.Timestamp.now(),
                "preferences" to mapOf(
                    "unitSystem" to "metric",
                    "profileImage" to "default"
                )
            )

            firestore.collection("users")
                .document(user.uid)
                .set(userDoc)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user?.toUser()
            if (user != null) Result.success(user) else Result.failure(Exception("Login failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override val currentUser: User?
        get() = firebaseAuth.currentUser?.toUser()

    override val authStateChanges: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    private fun FirebaseUser.toUser() = User(
        uid = uid,
        email = email,
        displayName = displayName,
        profileImageUrl = photoUrl?.toString()
    )
}
