package com.ags.controlekm.database.firebaseServices

import com.ags.controlekm.models.Address
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddressServices {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance().reference.child("rotaN2").child("address")

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