package com.ags.controlekm.database.remote.repositories

import com.ags.controlekm.database.models.CurrentUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCurrentUserRepository @Inject constructor(
    private val databaseReference: DatabaseReference
) {
    suspend fun getCurrentUser(): Flow<CurrentUser> = callbackFlow{
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(CurrentUser::class.java)
                if (user != null) {
                    trySend(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
        awaitClose { databaseReference.removeEventListener(valueEventListener) }
    }.flowOn(Dispatchers.IO)

    fun updateEmailVerification(emailIsVerifield: Boolean){
        databaseReference.child("emailVerification").setValue(emailIsVerifield)
    }

    fun updateLastKm(km: Int){
        databaseReference.child("lastKm").setValue(km)
    }

    fun updateKmBackup(km: Int){
        databaseReference.child("kmBackup").setValue(km)
    }
}