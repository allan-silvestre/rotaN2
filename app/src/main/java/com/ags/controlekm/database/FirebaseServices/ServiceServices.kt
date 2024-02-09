package com.ags.controlekm.database.FirebaseServices

import com.ags.controlekm.database.Models.ViagemSuporteTecnico
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ServiceServices {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance().reference.child("rotaN2").child("atendimentos")

    fun insert(viagemSuporteTecnico: ViagemSuporteTecnico) {
        databaseReference.child(viagemSuporteTecnico.id).setValue(viagemSuporteTecnico)
    }

    fun update(viagemSuporteTecnico: ViagemSuporteTecnico) {
        databaseReference.child(viagemSuporteTecnico.id).setValue(viagemSuporteTecnico)
    }

    fun delete(viagemSuporteTecnico: ViagemSuporteTecnico) {
        databaseReference.child(viagemSuporteTecnico.id).removeValue()
    }
}