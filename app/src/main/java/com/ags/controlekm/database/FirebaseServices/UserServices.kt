package com.ags.controlekm.database.FirebaseServices

import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.User
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