package com.ags.controlekm.database.firebaseRepositories

import com.ags.controlekm.models.Service
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseServiceRepository @Inject constructor(
    private val databaseReference: DatabaseReference
) {
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