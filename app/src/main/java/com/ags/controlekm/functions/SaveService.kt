package com.ags.controlekm.functions

import com.ags.controlekm.objects.Service
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

fun SaveService(
    atendimentoId: String,
    data: String,
    horaSaida: String,
    horaChegada: String,
    horaConclusão: String,
    localSaida: String,
    localAtendimento: String,
    kmSaida: String,
    kmChegada: String,
    kmRodado: String,
    imgPerfil: String,
    tecnicoId: String,
    tecnicoNome: String,
    statusService: String
) {

    lateinit var database: DatabaseReference
    database = Firebase.database.reference
    val service = Service(
        atendimentoId,
        data,
        horaSaida,
        horaChegada,
        horaConclusão,
        localSaida,
        localAtendimento,
        kmSaida,
        kmChegada,
        kmRodado,
        imgPerfil,
        tecnicoId,
        tecnicoNome,
        statusService,
    )

    database.child("UserClient")
        .child("Services")
        .child(atendimentoId)
        .setValue(service)
}
