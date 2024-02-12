package com.ags.controlekm.database.firebaseRepositories

import com.ags.controlekm.models.Service
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseServiceRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance().reference.child("rotaN2").child("services")

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