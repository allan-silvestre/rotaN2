package com.ags.controlekm.database.Models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ViagemSuporteTecnico(
    @PrimaryKey
    @NonNull
    var id: String = UUID.randomUUID().toString(),
    var dataSaida: String? = "",
    var dataChegada: String? = "",
    var dataConclusao: String? = "",
    var dataSaidaRetorno: String? = "",
    var dataChegadaRetorno: String? = "",
    var horaSaida: String? = "",
    var horaChegada: String? = "",
    var horaConclusao: String? = "",
    var horaSaidaRetorno: String? = "",
    var horaChegadaRetorno: String? = "",
    var localSaida: String? = "",
    var localAtendimento: String? = "",
    var localRetorno: String? = "",
    var kmSaida: String? = "",
    var kmChegada: String? = "",
    var kmRodado: String? = "",
    var imgPerfil: String? = "",
    var tecnicoId: String? = "",
    var tecnicoNome: String? = "",
    var statusService: String? = "",
    var descricao: String? = "",
)