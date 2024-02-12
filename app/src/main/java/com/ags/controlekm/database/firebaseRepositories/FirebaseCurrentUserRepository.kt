package com.ags.controlekm.database.firebaseRepositories

import com.ags.controlekm.models.CurrentUser
import com.ags.controlekm.models.Service
import com.ags.controlekm.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCurrentUserRepository @Inject constructor(
    private val id: String,
    private val databaseReference: DatabaseReference
) {
    suspend fun getCurrentUser(): Flow<CurrentUser?> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(CurrentUser::class.java)
                trySend(userData)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        databaseReference.child(id).addValueEventListener(valueEventListener)
        awaitClose { databaseReference.removeEventListener(valueEventListener) }
    }

    fun insert(user: User) {
        databaseReference.child(id).setValue(user)
    }
    fun update(user: User) {
        databaseReference.child(id).setValue(user)
    }
    fun emailIsVerifield(emailIsVerifield: Boolean){
        databaseReference.child(id).child("emailVerification").setValue(emailIsVerifield)
    }

    fun addUltimoKm(km: String){
        databaseReference.child(id).child("lastKm").setValue(km)
    }

    fun addKmBackup(km: String){
        databaseReference.child(id).child("kmBackup").setValue(km)
    }
}