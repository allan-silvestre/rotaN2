package com.ags.controlekm.database.firebaseRepositories

import com.ags.controlekm.database.models.database.Address
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
class FirebaseAddressRepository @Inject constructor(
    private var databaseReference: DatabaseReference
) {
    suspend fun getAllAddress() = callbackFlow<List<Address>> {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<Address>()
                for (childSnapshot in snapshot.children) {
                    val data = childSnapshot.getValue(Address::class.java)
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

    fun insert(address: Address) {
        databaseReference.child(address.id).setValue(address)
    }

    fun update(address: Address) {
        databaseReference.child(address.id).setValue(address)
    }

    fun delete(address: Address) {
        databaseReference.child(address.id).removeValue()
    }
}