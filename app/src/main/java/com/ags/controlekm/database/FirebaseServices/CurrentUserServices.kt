package com.ags.controlekm.database.FirebaseServices

import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.ags.controlekm.database.Models.User
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
        databaseReference.child("emailVerificado").setValue(emailIsVerifield)
    }

    fun addUltimoKm(km: String){
        databaseReference.child("ultimoKm").setValue(km)
    }

    fun addKmBackup(km: String){
        databaseReference.child("kmBackup").setValue(km)
    }
}