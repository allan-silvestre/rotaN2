package com.ags.controlekm.database.firebaseRepositories

import com.ags.controlekm.database.models.database.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepository @Inject constructor(
    private var databaseReference: DatabaseReference
) {
    suspend fun getAllUser() = callbackFlow<List<User>> {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<User>()
                for (childSnapshot in snapshot.children) {
                    val data = childSnapshot.getValue(User::class.java)
                    data?.let { dataList.add(it) }
                }
                if (isActive) {
                    trySend(dataList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        databaseReference.addValueEventListener(valueEventListener)

        awaitClose { databaseReference.removeEventListener(valueEventListener) }
    }

    fun insert(user: User) {
        databaseReference.child(user.id).setValue(user)
    }

    fun update(user: User) {
        databaseReference.child(user.id).setValue(user)
    }

    fun delete(user: User) {
        databaseReference.child(user.id).removeValue()
    }
}