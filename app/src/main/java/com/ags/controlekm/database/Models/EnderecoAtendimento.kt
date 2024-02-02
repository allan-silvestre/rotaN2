package com.ags.controlekm.database.Models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class EnderecoAtendimento(
    @PrimaryKey
    @NonNull
    val id: String = UUID.randomUUID().toString(),
    var nome: String? = "",
    var estado: String? = "",
    var cidade: String? = "",
    var bairro: String? = "",
    var logradouro: String? = "",
    var numero: String? = "",
) {
    fun toStringEnderecoAtendimento(): String {
        return "$nome"
    }
}

