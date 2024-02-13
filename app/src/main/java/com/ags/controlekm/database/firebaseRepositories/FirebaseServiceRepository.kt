package com.ags.controlekm.database.firebaseRepositories

import com.ags.controlekm.models.database.Service
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
class FirebaseServiceRepository @Inject constructor(
    private val databaseReference: DatabaseReference
) {
    suspend fun getAllServices() = callbackFlow<List<Service>> {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<Service>()
                for (childSnapshot in snapshot.children) {
                    val data = childSnapshot.getValue(Service::class.java)
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

    fun insert(service: Service) {
        databaseReference.child(service.id).setValue(service)
    }

    fun update(service: Service) {
        databaseReference.child(service.id).setValue(service)
    }

    fun delete(service: Service) {
        databaseReference.child(service.id).removeValue()
    }
}