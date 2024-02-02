package com.ags.controlekm.objects

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Service(
    val atendimentoId: String? = null,
    val data: String? = null,
    val horaSaida: String? = null,
    val horaChegada: String? = null,
    val horaConclusao: String? = null,
    val localSaida: String? = null,
    val localAtendimento: String? = null,
    val kmSaida: String? = null,
    val kmChegada: String? = null,
    val kmRodado: String? = null,
    val imgPerfil: String? = null,
    val tecnicoId: String? = null,
    val tecnicoNome: String? = null,
    val statusService: String? = null,
)