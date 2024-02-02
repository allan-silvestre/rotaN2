package com.ags.controlekm.database.FirebaseServices

import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EnderecoAtendimentoServices {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance().reference.child("rotaN2").child("enderecos")

    fun insert(enderecoAtendimento: EnderecoAtendimento) {
        databaseReference.child(enderecoAtendimento.id).setValue(enderecoAtendimento)
    }

    fun update(enderecoAtendimento: EnderecoAtendimento) {
        databaseReference.child(enderecoAtendimento.id).setValue(enderecoAtendimento)
    }

    fun delete(enderecoAtendimento: EnderecoAtendimento) {
        databaseReference.child(enderecoAtendimento.id).removeValue()
    }
}