package com.ags.controlekm.database.Models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class CurrentUser(
    @PrimaryKey
    @NonNull
    val id: String = UUID.randomUUID().toString(),
    var imagem: String? = "",
    var nome: String? = "",
    var sobrenome: String?= "",
    var nascimento: String? = "",
    var genero: String? = "",
    var nivelAcesso: String? = "",
    var matricula: String? = "",
    var telefone: String? = "",
    var cpf: String? = "",
    var email: String? = "",
    var cnpjEmpregador: String? = "",
    var cargo: String? = "",
    var setor: String? = "",
    var kmBackup: String? = "0",
    var ultimoKm: String? = "0",
    var emailVerificado: Boolean? = false
)
