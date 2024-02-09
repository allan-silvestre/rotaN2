package com.ags.controlekm.database.firebaseServices

import com.ags.controlekm.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CurrentUserServices(id: String) {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference
        .child("rotaN2")
        .child("users")
        .child(id)

    fun insert(user: User) {
        databaseReference.child(user.id).setValue(user)
    }

    fun update(user: User) {
        databaseReference.child(user.id).setValue(user)
    }

    fun emailIsVerifield(emailIsVerifield: Boolean){
        databaseReference.child("emailVerification").setValue(emailIsVerifield)
    }

    fun addUltimoKm(km: String){
        databaseReference.child("lastKm").setValue(km)
    }

    fun addKmBackup(km: String){
        databaseReference.child("kmBackup").setValue(km)
    }
}