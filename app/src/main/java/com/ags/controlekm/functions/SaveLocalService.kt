package com.ags.controlekm.functions

import com.ags.controlekm.database.Models.EnderecoAtendimento
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

fun SaveLocalService(
    id: String,
    nome: String,
    estado: String,
    cidade: String,
    bairro: String,
    logradouro: String,
    numero: String,
) {
    lateinit var database: DatabaseReference
    database = Firebase.database.reference
    val newLocalService = EnderecoAtendimento(
        id,
        nome,
        estado,
        cidade,
        bairro,
        logradouro,
        numero,
    )

    database.child("rotaN2")
        .child("enderecosAtendimento")
        .child(id)
        .setValue(newLocalService)
}
