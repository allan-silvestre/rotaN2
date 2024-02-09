package com.ags.controlekm.database.firebaseServices

import com.ags.controlekm.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserServices {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference
            .child("rotaN2")
            .child("users")

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